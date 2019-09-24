package com.michael.qwitter.Model;

public class User
{
    private String mUserAlias;
    private String mPassword;
    private String mAuthToken;

    public User(String userAlias, String password)
    {
        mUserAlias = userAlias;
        mPassword = password;
    }

    public String getUserAlias()
    {
        return mUserAlias;
    }

    public void setUserAlias(String userAlias)
    {
        this.mUserAlias = userAlias;
    }

    public String getPassword()
    {
        return mPassword;
    }

    public void setPassword(String password)
    {
        this.mPassword = password;
    }

    public String getAuthToken()
    {
        return mAuthToken;
    }

    public void setAuthToken(String mAuthToken)
    {
        this.mAuthToken = mAuthToken;
    }
}
