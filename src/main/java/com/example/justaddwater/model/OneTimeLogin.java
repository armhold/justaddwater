package com.example.justaddwater.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * supports "forgotten password login"
 *
 * @author George Armhold armhold@gmail.com
 */
@Entity
@Table(name = "onetimelogin")
public class OneTimeLogin implements Serializable
{
    // valid for 24 hours
    public static final long VALID_FOR_MILLIS = 1000 * 60 * 60 * 24;

    @Id
    private String token;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne
    private User user;


    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public boolean isExpired()
    {
        return ! isNotExpired();
    }

    public boolean isNotExpired()
    {
        return new Date().getTime() - creationDate.getTime() < VALID_FOR_MILLIS;
    }

}
