package com.example.justaddwater.web.app;

/**
 * @author George Armhold armhold@gmail.com
 */

import com.example.justaddwater.model.DAO;
import com.example.justaddwater.model.OneTimeLogin;
import com.example.justaddwater.model.User;
import net.ftlines.blog.cdidemo.web.UserAction;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * page where users are sent to recover their passwords via a one-time login token
 */
@RequireHttps
public class RecoverPasswordPage extends WebPage
{
    private static final long serialVersionUID = 1L;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RecoverPasswordPage.class);

    private WebMarkupContainer successMessage;
    private PasswordTextField passwordField;
    private WebMarkupContainer enclosure;

    @Inject
    DAO dao;
    
    @Inject
    EntityManager em;

    @Inject
    MySession session;
    
    @Inject
    UserAction action;
    
    public RecoverPasswordPage(final PageParameters parameters)
    {
        super(parameters);
        add(new Header("header"));

        String token = parameters.get("token").toString();
        OneTimeLogin otl = dao.findOneTimeLoginByToken(token);
        if (otl == null)
        {
            error("login token invalid");
            throw new RestartResponseAtInterceptPageException(ForgotPasswordPage.class);
        }
        else if (otl.isExpired())
        {
            error("login token expired");
            throw new RestartResponseAtInterceptPageException(ForgotPasswordPage.class);
        }
        else
        {
            loginViaOneTimePassword(otl);
        }

        enclosure = new WebMarkupContainer("enclosure");
        enclosure.setOutputMarkupId(true);

        Form form = new Form("form")
        {
            @Override
            protected void onSubmit()
            {
                User user = session.getLoggedInUser();
                changePassword(user, passwordField.getModelObject());
                enclosure.setVisible(false);
                successMessage.setVisible(true);
            }
        };

        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);

        passwordField = new PasswordTextField("password", new Model<String>());
        passwordField.setRequired(true);
        passwordField.add(StringValidator.lengthBetween(6, 32));

        PasswordTextField confirmPasswordField = new PasswordTextField("confirmPassword", new Model<String>());
        confirmPasswordField.setRequired(true);

        form.add(passwordField);
        form.add(confirmPasswordField);
        form.add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));

        successMessage = new WebMarkupContainer("successMessage");
        successMessage.setOutputMarkupId(true);
        successMessage.setVisible(false);
        add(successMessage);

        enclosure.add(form);
        add(enclosure);
    }



    /**
     * log user in and revoke the one-time-login token
     */
    private void loginViaOneTimePassword(OneTimeLogin otl)
    {
        User user = otl.getUser();
        log.info("loginViaOneTimePassword for user: " + user.getEmail());

        session.setUsername(user.getEmail());
        em.remove(otl);
        action.apply();
    }

    private void changePassword(User user, String newPassword)
     {
        log.info("change password for user: " + user.getEmail());

        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashed);
        action.apply();
    }

}
