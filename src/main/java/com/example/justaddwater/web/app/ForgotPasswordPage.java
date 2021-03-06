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

import com.example.justaddwater.model.AuthenticationType;
import com.example.justaddwater.model.DAO;
import com.example.justaddwater.model.OneTimeLogin;
import com.example.justaddwater.model.User;
import com.example.justaddwater.util.ServerUtils;
import net.ftlines.blog.cdidemo.web.UserAction;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.UUID;

/**
 * For users who have forgotten their password- create and persist
 * a token for a one-time-login; email the user a link so that they can
 * then log in with the token, and change their password.
 *
 * @author George Armhold armhold@gmail.com
 */
public class ForgotPasswordPage extends WebPage
{
    private static final long serialVersionUID = 1L;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ForgotPasswordPage.class);

    private RequiredTextField<String> emailField;
    private WebMarkupContainer successMessage;
    private WebMarkupContainer resetDiv;
    private Form form;
    private Model<String> resetEmailModel = new Model<String>();

    @Inject
    DAO dao;

    @Inject
    EntityManager em;

    @Inject
    UserAction action;

    public ForgotPasswordPage(PageParameters parameters)
    {
        super(parameters);
        add(new Header("header"));

        form = new Form("form");
        form.setOutputMarkupId(true);

        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);

        emailField = new RequiredTextField<String>("email", new Model<String>());
        emailField.add(EmailAddressValidator.getInstance());

        form.add(emailField);

        AjaxButton submit = new AjaxButton("submit")
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                String email = emailField.getModelObject();

                User user = dao.findUserByEmail(email);
                if (user == null)
                {
                    error("no such account: " + email);
                }
                else if (user.getAuthenticationType() == AuthenticationType.facebook)
                {
                    error("can't reset password on Facebook account");
                }
                else
                {
                    createOneTimePassword(user);
                    action.apply();
                }

                target.add(resetDiv);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
                target.add(resetDiv);
            }
        };

        form.add(submit);

        successMessage = new WebMarkupContainer("successMessage");
        Label resetEmail = new Label("resetEmail", resetEmailModel);
        successMessage.add(resetEmail);
        successMessage.setOutputMarkupId(true);
        successMessage.setVisible(false);

        resetDiv = new WebMarkupContainer("resetDiv");
        resetDiv.setOutputMarkupId(true);
        resetDiv.add(form);
        resetDiv.add(successMessage);
        add(resetDiv);
    }

    /**
     * create and persist a token for a one-time-login, and email them a link
     * so they can access their account
     */
    private void createOneTimePassword(User user)
    {
        log.info("createOneTimePassword: " + user.getEmail());

        String token = UUID.randomUUID().toString();

        OneTimeLogin otl = new OneTimeLogin();
        otl.setUser(user);
        otl.setCreationDate(new Date());
        otl.setToken(token);
        em.persist(otl);

        String from = "noreply@example.com";
        String toAddress = user.getEmail();
        String subject = "Log in to Your App";
        String body = ServerUtils.readAsString(getClass().getResourceAsStream("reset-password-template.html"));
        String bcc = null;

        PageParameters params = new PageParameters();
        params.add("token", token);

        String resetURL = RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(urlFor(RecoverPasswordPage.class, params).toString()));
        body = body.replace("RESET_URL", resetURL);

        ElasticEmail.sendEmail(from, from, subject, body, toAddress, bcc);

        form.setVisible(false);
        resetEmailModel.setObject(user.getEmail());
        successMessage.setVisible(true);
    }

}
