package com.example.justaddwater.web.app;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author George Armhold armhold@gmail.com
 */
public class AboutPage extends WebPage
{
    public AboutPage(PageParameters parameters)
    {
        super(parameters);
        add(new Header("header"));
    }

}
