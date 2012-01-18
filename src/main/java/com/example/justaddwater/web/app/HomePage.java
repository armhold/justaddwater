package com.example.justaddwater.web.app;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;

//@RequireHttps
public class HomePage extends WebPage
{
    private static final long serialVersionUID = 1L;

    public HomePage()
    {
        this(new PageParameters());
    }

    public HomePage(final PageParameters parameters)
    {
        super(parameters);
        add(new Header("header"));
    }
}
