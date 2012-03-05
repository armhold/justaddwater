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
package com.example.justaddwater.components;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import java.util.regex.Pattern;

/**
 * minimally validate US-style phone numbers
 *
 * @author
 */
public class PhoneNumberValidator extends AbstractValidator<String>
{
    @Override
    protected void onValidate(IValidatable<String> validatable)
    {
        String value = validatable.getValue();
        if (! Pattern.matches("\\d\\d\\d\\s?-?\\d\\d\\d\\s?-?\\d\\d\\d\\d", value))
        {
            error(validatable);
        }
    }

    @Override
    protected String resourceKey()
    {
        return "PhoneNumberValidator.phoneFormat";
    }

}
