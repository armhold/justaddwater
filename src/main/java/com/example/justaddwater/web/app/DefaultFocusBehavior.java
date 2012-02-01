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
