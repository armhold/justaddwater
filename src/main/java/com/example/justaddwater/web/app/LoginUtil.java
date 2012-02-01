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

import com.example.justaddwater.model.AuthenticationType;
import com.example.justaddwater.model.DAO;
import com.example.justaddwater.model.User;
import org.apache.wicket.Component;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author George Armhold armhold@gmail.com
 */
@ApplicationScoped
public class LoginUtil
{
    @Inject
    DAO dao;
    
    @Inject
    MySession session;

    public boolean loginWithPassword(String username, String password, Component component)
    {
        User user = dao.findUserByEmail(username);
        if (user == null || user.getPassword() == null || user.getAuthenticationType() != AuthenticationType.local)
        {
            return false;
        }
        else
        {
            boolean success = BCrypt.checkpw(password, user.getPassword());
            if (success)
            {
                session.setUsername(user.getEmail());
                if (! component.continueToOriginalDestination())
                {
                    component.setResponsePage(new AccountPage());
                }
            }

            return success;
        }
    }

}
