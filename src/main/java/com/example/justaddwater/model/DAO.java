package com.example.justaddwater.model;

import org.apache.wicket.util.string.Strings;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author George Armhold armhold@gmail.com
 */
@ApplicationScoped
public class DAO
{
    @Inject
    private EntityManager em;

    public User findUserByEmail(String email)
    {
        if (Strings.isEmpty(email)) return null;

        TypedQuery<User> query = em.createQuery("select u from User u where u.email= :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().size() > 0 ? query.getSingleResult() : null;
    }

    public OneTimeLogin findOneTimeLoginByToken(String token)
    {
        if (Strings.isEmpty(token)) return null;

        TypedQuery<OneTimeLogin> query = em.createQuery("select otl from OneTimeLogin otl where otl.token = :token", OneTimeLogin.class);
        query.setParameter("token", token);
        List<OneTimeLogin> results = query.getResultList();
        if (results.size() > 0)
        {
            return results.get(0);
        }
        else
        {
            return null;
        }
    }

}
