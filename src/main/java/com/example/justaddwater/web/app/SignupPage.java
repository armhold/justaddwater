/*
 *  Copyright 2012 George Armhold
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.example.justaddwater.web.app;

import com.example.justaddwater.facebook.FacebookOAuthPage;
import com.example.justaddwater.model.AuthenticationType;
import com.example.justaddwater.model.DAO;
import com.example.justaddwater.model.User;
import net.ftlines.blog.cdidemo.web.UserAction;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;

@RequireHttps
public class SignupPage extends WebPage
{
    private static final long serialVersionUID = 1L;

    @Inject
    Logger log;

    @Inject
    EntityManager em;
    
    @Inject
    DAO dao;

    @Inject
    UserAction action;

    @Inject
    MySession session;
    
    private RequiredTextField<String> usernameField;
    private PasswordTextField passwordField;
    private PasswordTextField confirmPasswordField;
    private FeedbackPanel feedback;

    public SignupPage(final PageParameters parameters)
    {
        super(parameters);
        add(new Header("header"));

        Form form = new Form("form");
        form.setOutputMarkupId(true);

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);

        form.add(new ExternalLink("fbLink", FacebookOAuthPage.getFacebookLoginUrl()));

        usernameField = new RequiredTextField<String>("username", new Model<String>());
        passwordField = new PasswordTextField("password", new Model<String>());
        passwordField.setRequired(true);
        passwordField.add(StringValidator.lengthBetween(6, 32));

        confirmPasswordField = new PasswordTextField("password-confirm", new Model<String>());
        confirmPasswordField.setRequired(true);

        form.add(usernameField);
        form.add(passwordField);
        form.add(confirmPasswordField);

        form.add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));

        AjaxButton submit = new AjaxButton("submit")
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                String username = usernameField.getModelObject();
                String pw = passwordField.getModelObject();

                User existingUser = dao.findUserByEmail(username);
                if (existingUser != null) {
                    usernameField.error("a user with that username already exists, account type: " + existingUser.getAuthenticationType());
                    target.add(form);
                }
                else 
                {
                    User user = new User();
                    createAccount(user, username, pw);
                    setResponsePage(new AccountPage());
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
                target.add(feedback);
                target.add(form);
            }

        };
        form.add(submit);

        add(form);
    }

    private void createAccount(User user, String username, String password)
    {
        log.info("createAccount username: " + username);

        user.setEmail(username);

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashed);
        user.setAccountCreationDate(new Date());
        user.setAuthenticationType(AuthenticationType.local);

        em.persist(user);
        action.apply();

        session.setUsername(user.getEmail());

//        elasticEmail.sendEmail("yourapp account created", "yourapp account created: " + user.getEmail(), SearchPage.CONTACT_EMAIL_ADDRESS);
    }

}
