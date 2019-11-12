package com.michael.qwitter.Presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;

public class CreateStatusPresenter
{
    private UserDatabase mUserDatabase;
    private User mStatusUser;
    private Status mStatus;
    private IAccessor mAccessor;

    public CreateStatusPresenter()
    {
        mStatusUser = new User("","");
        mAccessor = new Accessor();
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
//        mStatusUser.addStatusToStory(mStatus);
//        //mStatusUser.addStatusToFeed(mStatus);
//        mUserDatabase.updateUser(mStatusUser.getUserAlias(), mStatusUser);
        mAccessor.addStatus(mStatusUser.getUserAlias(), mStatus);
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
        this.mStatusUser = mAccessor.getUserInfo(mStatusUser);
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
