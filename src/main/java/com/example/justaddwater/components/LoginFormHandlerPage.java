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

import com.example.justaddwater.web.app.LoginPage;
import com.example.justaddwater.web.app.LoginUtil;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * manually handle the https form submission from LoginForm.
 *
 * This class is needed because Wicket does not allow an https POST from an
 * http page if an HttpsMapper has been installed in WicketApplication.
 * The receiving page (i.e. this class) must be annotated with @RequiresHttps.
 *
 * This code is a (greatly simplified) take on this blog posting:
 *
 * http://www.petrikainulainen.net/programming/tips-and-tricks/wicket-https-tutorial-part-three-creating-a-secure-form-submit-from-a-non-secure-page/
 *
 */
@RequireHttps
public class LoginFormHandlerPage extends WebPage
{
    @Inject
    LoginUtil loginUtil;

    public LoginFormHandlerPage(PageParameters parameters)
    {
        HttpServletRequest req = (HttpServletRequest) getRequest().getContainerRequest();

        // NB: can't get params from PageParameters- we are processing a form submission manually
        // see: https://issues.apache.org/jira/browse/WICKET-4338
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (! loginUtil.loginWithPassword(username, password, this))
        {
            // set message on session so that it will ultimately be displayed on the LoginPage
            getSession().error(getString("error.login.failed"));
            setResponsePage(LoginPage.class);
        }
    }

}
