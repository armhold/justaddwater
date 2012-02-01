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

import com.example.justaddwater.model.DAO;
import com.example.justaddwater.model.User;
import net.ftlines.blog.cdidemo.web.UserAction;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

import javax.inject.Inject;

@RequireHttps
/**
 * allow users to change their passwords
 */
public class ChangePasswordPage extends WebPage implements RequiresAuthentication
{
    private static final long serialVersionUID = 1L;

    private WebMarkupContainer enclosingDiv, successMessage;
    private TextField<String> currentPasswordField;
    private PasswordTextField passwordField;
    private FeedbackPanel feedback;

    @Inject
    Logger log;

    @Inject
    DAO dao;

    @Inject
    com.example.justaddwater.web.app.MySession session;
    
    @Inject
    UserAction action;

    public ChangePasswordPage(final PageParameters parameters)
    {
        super(parameters);

        add(new Header("header"));

        enclosingDiv = new WebMarkupContainer("enclosing-div");
        enclosingDiv.setOutputMarkupId(true);

        if (! session.isLoggedIn())
        {
            error("not logged in");
            throw new RestartResponseAtInterceptPageException(LoginPage.class);
        }

        Form form = new Form("form");
        form.setOutputMarkupId(true);

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);

        currentPasswordField = new PasswordTextField("current-password", new Model<String>());
        currentPasswordField.setRequired(true);

        passwordField = new PasswordTextField("password", new Model<String>());
        passwordField.setRequired(true);
        passwordField.add(StringValidator.lengthBetween(6, 32));

        PasswordTextField confirmPasswordField = new PasswordTextField("confirm-password", new Model<String>());
        confirmPasswordField.setRequired(true);

        form.add(currentPasswordField);
        form.add(passwordField);
        form.add(confirmPasswordField);

        form.add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));

        AjaxButton submit = new AjaxButton("submit")
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                String currentPassword = currentPasswordField.getModelObject();
                String newPassword = passwordField.getModelObject();
                User user = dao.findUserByEmail(session.getUsername());

                if (user == null)
                {
                    error("not logged in");
                    throw new RestartResponseAtInterceptPageException(LoginPage.class);
                }
                else if (currentPasswordIsValid(user, currentPassword))
                {
                    changePassword(user, newPassword);
                    action.apply();  // save changes
                    successMessage.setVisible(true);
                    form.setVisible(false);
                }
                else
                {
                    error("current password is invalid");
                }

                target.add(enclosingDiv);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
                target.add(feedback);
                target.add(enclosingDiv);
            }
        };

        form.add(submit);
        enclosingDiv.add(form);

        successMessage = new WebMarkupContainer("success-message");
        successMessage.setOutputMarkupId(true);
        successMessage.setVisible(false);
        enclosingDiv.add(successMessage);

        add(enclosingDiv);

    }

    private boolean currentPasswordIsValid(User user, String currentPassword)
    {
        return BCrypt.checkpw(currentPassword, user.getPassword());
    }

    private void changePassword(User user, String password)
    {
        log.info("change password for user: " + user.getEmail());

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashed);
        session.setUsername(user.getEmail());
    }

}
