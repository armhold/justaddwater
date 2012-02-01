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
