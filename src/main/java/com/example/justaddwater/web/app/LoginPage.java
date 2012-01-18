package com.example.justaddwater.web.app;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;

import javax.inject.Inject;

@RequireHttps
public class LoginPage extends WebPage
{
    private static final long serialVersionUID = 1L;
    
    @Inject
    LoginUtil loginUtil;

    @Inject
    Logger log;

    private RequiredTextField<String> usernameField;
    private PasswordTextField passwordField;

    public LoginPage()
    {
        this(new PageParameters());
    }

    public LoginPage(final PageParameters parameters)
    {
        super(parameters);
        add(new Header("header"));

        // make the login form stateless so that users don't get "page expired" message
        // if they leave the page idle for a long time and then return
        Form form = new StatelessForm("form")
        {
            @Override
            protected void onSubmit()
            {
                String username = usernameField.getModelObject();
                String password = passwordField.getModelObject();

                if (! loginUtil.loginWithPassword(username, password, LoginPage.this))
                {
                    error("bad password");
                }
            }
        };

        FeedbackPanel feedback = new FeedbackPanel("feedback");
        form.add(feedback);

        usernameField = new RequiredTextField<String>("username", Model.of(""));
        usernameField.add(new DefaultFocusBehavior());
        form.add(usernameField);

        passwordField = new PasswordTextField("password", Model.of(""));
        form.add(passwordField);
        add(form);
    }

}
