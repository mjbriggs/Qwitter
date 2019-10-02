package com.michael.qwitter.Model;

import java.util.ArrayList;

public class User
{
    private String mUserAlias;
    private String mPassword;
    private String mAuthToken;
    private String mUserEmail;
    private String mFirstName;
    private String mLastName;
    private Image mProfilePicture;
    private ArrayList<Status> mStatuses;
    //TODO has attachment
    //TODO has story
    //TODO has followers
    //TODO has followees
    //TODO has feed

    public User(String userAlias, String password)
    {
        mStatuses = new ArrayList<>();
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

    public String getFirstName()
    {
        return mFirstName;
    }

    public void setFirstName(String mFirstName)
    {
        this.mFirstName = mFirstName;
    }

    public String getLastName()
    {
        return mLastName;
    }

    public void setLastName(String mLastName)
    {
        this.mLastName = mLastName;
    }

    public Image getProfilePicture()
    {
        return mProfilePicture;
    }

    public void setProfilePicture(Image mProfilePicture)
    {
        this.mProfilePicture = mProfilePicture;
    }

    public ArrayList<Status> getStatuses()
    {
        return mStatuses;
    }

    public void setStatuses(ArrayList<Status> mStatuses)
    {
        this.mStatuses = mStatuses;
    }

    public void addStatus(Status status)
    {
        mStatuses.add(status);
    }

    @Override
    public String toString()
    {
        return "Alias: " + mUserAlias +
                "\nFirst Name: " + mFirstName +
                "\nLast Name: " + mLastName + "\n";
    }

}
