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
