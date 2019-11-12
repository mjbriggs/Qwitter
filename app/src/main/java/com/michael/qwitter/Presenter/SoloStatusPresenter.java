package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.DummyUserDatabase;
import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;

import java.util.Date;
import java.util.List;

public class SoloStatusPresenter implements StatusPresenter
{
    private String mUserName;
    private String mText;
    private Date mTimePosted;
    private DummyUserDatabase mUserDatabase;
    private Status mStatus;

    public SoloStatusPresenter(String username, Date timePosted, String text)
    {
        mText = text;
        mUserName = username;
        mTimePosted = timePosted;
        mUserDatabase = UserDatabase.getInstance();
    }
    @Override
    public String getUserAlias(int position)
    {
        return null;
    }

    @Override
    public List<Status> getStatuses(String username)
    {
        return null;
    }

    @Override
    public String getUserFullName()
    {
        return null;
    }

    @Override
    public Status getStatus(int position)
    {
        if(position < 0)
        {
            return mStatus;
        }
        else
        {
            User user = mUserDatabase.getUser(mUserName);
            List<Status> statuses = user.getStatuses();
            for(Status status : statuses)
            {
                System.out.println("getting " + status.toString() + " of user " + mUserName);
                if(status.getOwner().equals(mUserName) && status.getTimePosted().equals(mTimePosted)
                && status.getText().equals(mText))
                {
                    System.out.println("found match");
                    mStatus = status.clone();
                    return status;
                }
            }
            return null;
        }
    }

    public boolean hasImage()
    {
        getStatus(1);
        System.out.println(mStatus.getAttachment() != null);
        return mStatus.getAttachment() != null;
    }

    @Override
    public void update(String username)
    {

    }
}
