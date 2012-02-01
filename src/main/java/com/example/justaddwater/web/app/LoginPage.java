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
