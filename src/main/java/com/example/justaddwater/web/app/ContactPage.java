package com.example.justaddwater.web.app;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author George Armhold armhold@gmail.com
 */
public class ContactPage extends WebPage
{
    public static final String CONTACT_EMAIL_ADDRESS = "armhold@gmail.com";

    public ContactPage(PageParameters parameters)
    {
        super(parameters);
        add(new Header("header"));
    }

}
