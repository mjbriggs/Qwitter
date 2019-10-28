package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.DummyUserDatabase;
import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;
import com.michael.qwitter.Presenter.PresenterInterfaces.RelationPresenter;

public class FollowingPresenter implements RelationPresenter
{
    private Following mFollowing;
    private DummyUserDatabase mUserDatabase;

    public FollowingPresenter(String username)
    {
        mUserDatabase = UserDatabase.getInstance();
        mFollowing = mUserDatabase.getUser(username).getFollowing();
    }
    @Override
    public Followers getFollowers()
    {
        try
        {
            throw new Exception("FollowingPresenter does not contain user follower information");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Following getFollowing()
    {
        return mFollowing;
    }
}
