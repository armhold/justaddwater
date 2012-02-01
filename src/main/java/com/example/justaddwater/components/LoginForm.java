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
package com.example.justaddwater.components;

import com.example.justaddwater.facebook.FacebookOAuthPage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;

/**
 * A form that POSTs directly to the LoginFormHandlerPage. Since LoginFormHandlerPage
 * is annotated with @RequireHttps, the form submission will go over SSL.
 *
 */
public class LoginForm extends StatelessForm
{
    public LoginForm(String id)
    {
        super(id);

        add(new TextField("username").setRequired(true));
        add(new PasswordTextField("password").setRequired(true));
        add(new ExternalLink("loginWithFacebook", FacebookOAuthPage.getFacebookLoginUrl()));
    }

    @Override
    protected void onComponentTag(ComponentTag tag)
    {
        super.onComponentTag(tag);

        // manually alter the form action attribute to submit the form
        // to our SSL-protected handler page
        //
        String action = urlFor(LoginFormHandlerPage.class, null).toString();
        tag.put("action", action);
    }

}
