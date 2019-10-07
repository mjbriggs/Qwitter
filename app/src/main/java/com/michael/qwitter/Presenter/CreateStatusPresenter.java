package com.michael.qwitter.Presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;

public class CreateStatusPresenter
{
    private UserDatabase mUserDatabase;
    private User mStatusUser;
    private Status mStatus;

    public CreateStatusPresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
        System.out.println(mUserDatabase.getUsers().toString());
        mStatusUser = new User("","");
    }

    //get name, alias, image

    public String getUserFullName()
    {
        return mStatusUser.getFirstName() + " " + mStatusUser.getLastName();
    }

    public String getUserAlias()
    {
        return mStatusUser.getUserAlias();
    }

    public Bitmap getUserProfilePicture()
    {
        Image profilePicture = mStatusUser.getProfilePicture();

        return BitmapFactory.decodeFile(profilePicture.getImagePath());
    }
    public void addStatusToUser()
    {
        mStatusUser.addStatusToStory(mStatus);
        //mStatusUser.addStatusToFeed(mStatus);
        mUserDatabase.updateUser(mStatusUser.getUserAlias(), mStatusUser);
    }

    public User getStatusUser()
    {
        return mStatusUser;
    }

    public void setStatusUser(User mStatusUser)
    {
        this.mStatusUser = mStatusUser;
    }

    public void setStatusUser(String mStatusUser)
    {
        System.out.println(mUserDatabase.getUser(mStatusUser).toString());
        this.mStatusUser = mUserDatabase.getUser(mStatusUser);
    }

    public Status getStatus()
    {
        return mStatus;
    }

    public void setStatus(Status mStatus)
    {
        this.mStatus = mStatus;
    }

    public void setStatus(String statusText)
    {
        String alias = mStatusUser.getUserAlias();
        String fullName = mStatusUser.getFirstName() + " " + mStatusUser.getLastName();
        mStatus = new Status(statusText, alias, fullName);
    }
}
