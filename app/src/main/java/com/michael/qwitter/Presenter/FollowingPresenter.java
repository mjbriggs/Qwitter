package com.michael.qwitter.Presenter;

import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.RelationPresenter;
import com.michael.qwitter.Utils.PageTracker;

public class FollowingPresenter implements RelationPresenter
{
    private Following mFollowing;
    private IAccessor mAccessor;
    private String mUsername;
    public FollowingPresenter(String username)
    {
        mAccessor = new Accessor();
        mUsername = username;
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
        if(mFollowing == null)
        {
            mFollowing = new Following();
            this.update(mUsername);
        }

        return mFollowing;
    }

    @Override
    public void update(String username)
    {
        Following newFollowing = mAccessor.getFollowing(username, PageTracker.getInstance().getFollowingLastKey());
        PageTracker.getInstance().addFollowingLastKey(newFollowing.getFollowing().size());
        for (User user : newFollowing.getFollowing())
        {
            mFollowing.addFollowing(user);
        }
    }
}
