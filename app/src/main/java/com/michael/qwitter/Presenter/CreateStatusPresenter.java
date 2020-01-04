package com.michael.qwitter.Presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.ModelInterfaces.IAttachment;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;

public class CreateStatusPresenter
{
    private UserDatabase mUserDatabase;
    private User mStatusUser;
    private Status mStatus;
    private IAttachment mAttatchment;
    private IAccessor mAccessor;
    private String mAttachmentType;

    public CreateStatusPresenter()
    {
        mStatusUser = new User("","");
        mAccessor = new Accessor();
        mAttachmentType = "none";
    }

    public void addAsImage(String url)
    {
//        Image img = new Image(mStatusUser.getUserAlias());
//        img.setFilePath(url);
//        mAttatchment = img;
        mAttachmentType = "image";
    }

    public void addAsVideo(String url)
    {
//        Video vid = new Video(mStatusUser.getUserAlias());
//        vid.setFilePath(url);
//        mAttatchment = vid;
        mAttachmentType = "video";
    }

    public void setUrl(String url)
    {
        if (mAttachmentType.equalsIgnoreCase("image"))
        {
            Image img = new Image(mStatusUser.getUserAlias());
            img.setFilePath(url);
            mAttatchment = img;
        }
    }

    public String getImageURL()
    {
        if (mStatusUser == null)
            return "";

        return mStatusUser.getProfilePicture().getFilePath();
    }

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
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mAccessor.addStatus(mStatusUser.getUserAlias(), mStatus);
            }
        }).start();
    }

    public User getStatusUser()
    {
        return mStatusUser;
    }

    public void setStatusUser(User mStatusUser)
    {
        this.mStatusUser = mStatusUser;
    }

    public void setStatusUser(String statusUserIn)
    {
        mStatusUser = mAccessor.getUserInfo(statusUserIn);
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
        if (mAttatchment != null)
            mStatus.setAttachment(mAttatchment);
    }
}
