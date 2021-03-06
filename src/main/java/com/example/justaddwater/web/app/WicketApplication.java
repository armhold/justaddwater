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

import com.example.justaddwater.components.LoginFormHandlerPage;
import com.example.justaddwater.facebook.FacebookOAuthPage;
import net.ftlines.wicket.cdi.CdiConfiguration;
import net.ftlines.wicket.cdi.CdiContainer;
import org.apache.wicket.authorization.strategies.page.SimplePageAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsMapper;
import org.apache.wicket.settings.IApplicationSettings;
import org.apache.wicket.settings.IExceptionSettings;
import org.jboss.weld.environment.servlet.Listener;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

public class WicketApplication extends WebApplication
{
    public static final int HTTP_PORT = 8080;
    public static final int HTTPS_PORT = 8443;

	@Override
	public Class<HomePage> getHomePage()
	{
		return HomePage.class;
	}

	@Override
	public void init()
	{
		super.init();

        BeanManager manager = (BeanManager) getServletContext().getAttribute(Listener.BEAN_MANAGER_ATTRIBUTE_NAME);
        new CdiConfiguration(manager).configure(this);

        mountPage("/signup", SignupPage.class);
        mountPage("/forgot", ForgotPasswordPage.class);
        mountPage("/login", LoginPage.class);
        mountPage("/loginhandler", LoginFormHandlerPage.class);
        mountPage("/contact", ContactPage.class);
        mountPage("/about", AboutPage.class);
        mountPage("/change", ChangePasswordPage.class);
        mountPage("/404", Error404Page.class);
        mountPage("/500", Error500Page.class);
        mountPage("/account", AccountPage.class);
        mountPage("/fbauth", FacebookOAuthPage.class);

        setRootRequestMapper(new HttpsMapper(getRootRequestMapper(), new HttpsConfig(HTTP_PORT, HTTPS_PORT)));
        getSecuritySettings().setAuthorizationStrategy(new AuthorizationStrategy());

        IApplicationSettings settings = getApplicationSettings();
        settings.setInternalErrorPage(Error500Page.class);

        // https://cwiki.apache.org/WICKET/error-pages-and-feedback-messages.html
        getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
}

    // tell wicket to use our RequiresAuthentication marker interface to indicate which
    // pages need authentication
    public static class AuthorizationStrategy extends SimplePageAuthorizationStrategy
    {
        @Inject
        MySession session;

        public AuthorizationStrategy()
        {
            super(RequiresAuthentication.class, LoginPage.class);

            // must inject manually, since this object is not managed by wicket-cdi
            // see: https://www.42lines.net/2011/11/15/integrating-cdi-into-wicket/
            CdiContainer.get().getNonContextualManager().inject(this);
        }

        @Override
        protected boolean isAuthorized()
        {
            return session.isLoggedIn();
        }
    }

}
