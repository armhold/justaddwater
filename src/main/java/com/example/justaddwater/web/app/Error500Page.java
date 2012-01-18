package com.example.justaddwater.web.app;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;

import javax.inject.Inject;

public class Error500Page extends WebPage
{
    private static final long serialVersionUID = 1L;

    @Inject
    Logger log;

    public Error500Page(final PageParameters parameters)
    {
        super(parameters);
        add(new Header("header"));
    }


}
