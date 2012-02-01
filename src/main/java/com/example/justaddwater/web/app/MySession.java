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

import com.example.justaddwater.model.DAO;
import com.example.justaddwater.model.User;
import org.apache.wicket.Session;
import org.apache.wicket.util.string.Strings;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * a simple SessionScope object that stores the user's login credentials, if logged in
 *
 * @author George Armhold armhold@gmail.com
 */
@SessionScoped
public class MySession implements Serializable
{
    @Inject
    DAO dao;
    
    private String username;

    public String getUsername()
    {
        return ! Strings.isEmpty(username) ? username : "";
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void logout()
    {
        setUsername(null);
        Session.get().invalidate();
    }

    public boolean isLoggedIn()
    {
        return ! Strings.isEmpty(getUsername());
    }

    public User getLoggedInUser()
    {
        if (! isLoggedIn()) return null;

        return dao.findUserByEmail(getUsername());
    }

}
