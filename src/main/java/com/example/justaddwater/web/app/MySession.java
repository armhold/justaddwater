package com.example.justaddwater.web.app;

import com.example.justaddwater.model.DAO;
import com.example.justaddwater.model.User;
import org.apache.wicket.Session;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;

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
    Logger log;

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
        log.info("username set to: " + this.username);
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
