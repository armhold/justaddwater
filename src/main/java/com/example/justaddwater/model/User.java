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
