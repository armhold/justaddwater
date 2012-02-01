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

import com.example.justaddwater.components.LoginForm;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * common header used by all the pages
 *
 * @author George Armhold armhold@gmail.com
 */
public class Header extends Panel
{
    @Inject
    MySession session;
    
    @Inject
    Logger log;

    public Header(String id)
    {
        super(id);

        WebMarkupContainer signUpLink = new WebMarkupContainer("signup-link")
        {
            @Override
            public boolean isVisible()
            {
                return super.isVisible() && ! session.isLoggedIn();
            }
        };
        add(signUpLink);

        WebMarkupContainer accountLink = new WebMarkupContainer("account-link")
        {
            @Override
            public boolean isVisible()
            {
                return super.isVisible() && session.isLoggedIn();
            }
        };
        add(accountLink);

        add(new LoginForm("loginForm")
        {
            @Override
            public boolean isVisible()
            {
                return super.isVisible() && ! session.isLoggedIn();
            }
        });

        Form logoutForm = new Form("logoutForm")
        {
            @Override
            public boolean isVisible()
            {
                return super.isVisible() && session.isLoggedIn();
            }
        };
        
        Label loggedOnAs = new Label("loggedOnAs", new Model<String>()
        {
            @Override
            public String getObject()
            {
                return session.getUsername();
            }

        });
        logoutForm.add(loggedOnAs);

        Link logoutLink = new Link<String>("logout")
        {
            @Override
            public void onClick()
            {
                session.logout();
                setResponsePage(new HomePage());
            }
        };
        logoutForm.add(logoutLink);
        add(logoutForm);
    }

}
