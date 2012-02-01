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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author armhold
 */
@Entity
@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class User implements Serializable
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Basic
    private String email;

    @Basic
    private String password;

    @Basic
    private Date accountCreationDate;

    @Basic
    private AuthenticationType authenticationType;

    public User()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Date getAccountCreationDate()
    {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate)
    {
        this.accountCreationDate = accountCreationDate;
    }

    public void setAuthenticationType(AuthenticationType authenticationType)
    {
        this.authenticationType = authenticationType;
    }

    public AuthenticationType getAuthenticationType()
    {
        return authenticationType;
    }
}
