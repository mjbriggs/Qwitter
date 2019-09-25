package com.michael.qwitter.Model;

public class User
{
    private String mUserAlias;
    private String mPassword;
    private String mAuthToken;
    private String mUserEmail;
    private String mFirstName;
    private String mLastName;
    //TODO has attachment
    //TODO has story
    //TODO has statues
    //TODO has followers
    //TODO has followees
    //TODO has feed

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

    public String getmUserEmail()
    {
        return mUserEmail;
    }

    public void setmUserEmail(String mUserEmail)
    {
        this.mUserEmail = mUserEmail;
    }

    public String getmFirstName()
    {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName)
    {
        this.mFirstName = mFirstName;
    }

    public String getmLastName()
    {
        return mLastName;
    }

    public void setmLastName(String mLastName)
    {
        this.mLastName = mLastName;
    }

    @Override
    public String toString()
    {
        return "Alias: " + mUserAlias +
                "\nFirst Name: " + mFirstName +
                "\nLast Name: " + mLastName + "\n";
    }

}
