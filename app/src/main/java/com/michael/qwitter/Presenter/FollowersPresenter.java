package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.DummyUserDatabase;
import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;

public class FollowersPresenter implements RelationPresenter
{

    private Followers mFollowers;
    private DummyUserDatabase mUserDatabase;

    public FollowersPresenter(String username)
    {
        mUserDatabase = UserDatabase.getInstance();
        mFollowers = mUserDatabase.getUser(username).getFollowers();
    }

    @Override
    public Followers getFollowers()
    {
        return mFollowers;
    }

    @Override
    public Following getFollowing()
    {
        try
        {
            throw new Exception("FollowersPresenter does not contain user following information");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
