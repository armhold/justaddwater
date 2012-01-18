package com.example.justaddwater.web.app;

import com.example.justaddwater.model.AuthenticationType;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import javax.inject.Inject;

/**
 * @author George Armhold armhold@gmail.com
 */
public class AccountPage extends WebPage implements RequiresAuthentication
{
    @Inject
    MySession session;

    public AccountPage()
    {
        add(new Header("header"));
        add(new Label("username", Model.of(session.getUsername())));
        add(new Label("accountType", Model.of(session.getLoggedInUser().getAuthenticationType())));

        add(new WebMarkupContainer("changePassword")
        {
            @Override
            public boolean isVisible()
            {
                return super.isVisible() && session.getLoggedInUser().getAuthenticationType() == AuthenticationType.local;
            }
        });
    }

}
