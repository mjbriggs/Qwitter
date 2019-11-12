package com.michael.qwitter.Presenter;

import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.RelationPresenter;
import com.michael.qwitter.Utils.PageTracker;

public class FollowersPresenter implements RelationPresenter
{

    private Followers mFollowers;
    private IAccessor mAccessor;
    private String mUsername;

    public FollowersPresenter(String username)
    {
        mAccessor = new Accessor();
        mUsername = username;

    }

    @Override
    public Followers getFollowers()
    {
        if(mFollowers == null)
        {
            mFollowers = new Followers();
            this.update(mUsername);
        }

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

    @Override
    public void update(String username)
    {
        Followers newFollowers = mAccessor.getFollowers(username, PageTracker.getInstance().getFollowersLastKey());
        PageTracker.getInstance().addFollowersLastKey(newFollowers.getFollowers().size());
        for (User user : newFollowers.getFollowers())
        {
            mFollowers.addFollower(user);
        }
    }
}
