package com.michael.qwitter.Model;

import java.util.ArrayList;
import java.util.List;

public class Feed
{
    private List<Status> mStatusList;
    private List<User> mFollowing;
    private User mOwner;

    public Feed()
    {
        mStatusList = new ArrayList<>();
        mFollowing = new ArrayList<>();
    }


    public void addStatus(Status status, User user)
    {
        //System.out.println("adding status " + status.getText());
        if(mStatusList.size() == 0)
        {
            mStatusList.add(status.clone());
            return;
        }
        else if (mStatusList.size() == 1)
        {
            if(mStatusList.get(0).equals(status))
            {
                return;
            }
            else if(mStatusList.get(0).getTimePosted().compareTo(status.getTimePosted()) < 0)
            {
                mStatusList.add(0, status.clone());
                return;
            }
            else
            {
                mStatusList.add(status.clone());
                return;
            }
        }
        else
        {
            for(int i = mStatusList.size() - 1; i > 0; i--)
            {
                if(mStatusList.get(i).equals(status.clone()) || mStatusList.get(i - 1).equals(status.clone()))
                {
                    return;
                }
                else if(mStatusList.get(i).getTimePosted().compareTo(status.getTimePosted()) > 0 &&
                        mStatusList.get(i - 1).getTimePosted().compareTo(status.getTimePosted()) < 0)
                {
                    mStatusList.add(i, status.clone());
                    return;
                }
            }
        }

        mStatusList.add(status.clone());
        return;
    }

    public List<Status> getFeed(List<Status> userStatuses)
    {
        mStatusList = new ArrayList<>();

        for(Status status : userStatuses)
        {
            this.addStatus(status, mOwner);
        }
        for(User user : mFollowing)
        {
            for(Status status : user.getStory().getStatuses())
            {
                this.addStatus(status, user);
            }
        }
        return mStatusList;
    }
    public List<Status> getStatusList()
    {
        return mStatusList;
    }

    public void setStatusList(List<Status> mStatusList)
    {
        this.mStatusList = mStatusList;
    }

    public List<User> getFollowing()
    {
        return mFollowing;
    }

    public void setFollowing(List<User> mFollowing)
    {
        this.mFollowing = mFollowing;
    }

    public User getOwner()
    {
        return mOwner;
    }

    public void setOwner(User mOwner)
    {
        this.mOwner = (User) mOwner.clone();
    }
}
