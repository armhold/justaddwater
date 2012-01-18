package com.example.justaddwater.web.app;

/**
 * force a field to gain mouse focus when the page is loaded
 *
 * @author George Armhold armhold@gmail.com
 */

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponent;

/*
 * @see http://www.nabble.com/Default-Focus-Behavior--td15934889.html
 */
public class DefaultFocusBehavior extends Behavior
{
    private static final long serialVersionUID = 1;

    @Override
    public void bind(Component component)
    {
        if (! (component instanceof FormComponent))
        {
            throw new IllegalArgumentException("DefaultFocusBehavior: component must be instanceof FormComponent");
        }

        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response)
    {
        super.renderHead(component, response);
        response.renderOnLoadJavaScript("document.getElementById('" + component.getMarkupId() + "').focus();");
    }
}
