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
