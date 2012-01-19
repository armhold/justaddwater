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
