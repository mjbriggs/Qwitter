package com.michael.qwitter.Presenter;

import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.RelationPresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.Utils.PageTracker;
import com.michael.qwitter.View.ViewInterfaces.IView;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class FollowingPresenter implements RelationPresenter
{
    private Following mFollowing;
    private IAccessor mAccessor;
    private String mUsername;
    private IView mFollowingView;

    public FollowingPresenter(String username)
    {
        PageTracker.getInstance().setFollowingLastKey(0);
        mAccessor = new Accessor();
        mUsername = username;
    }

    public FollowingPresenter(String username, IView followingView)
    {
        this(username);
        mFollowingView = followingView;
        mFollowing = new Following();
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
//        if(mFollowing == null)
//        {
//            mFollowing = new Following();
//            this.update(mUsername);
//        }

        return mFollowing;
    }

    @Override
    public void update(String usernameIn)
    {
        final String username = usernameIn;
        new Thread(new Runnable() {
            public void run() {
                String lk = "";
                if (mFollowing.getFollowing().size() > 0)
                {
                    lk = mFollowing.getFollowing().get(mFollowing.getFollowing().size() - 1).getUserAlias();
                }

                Following newFollowing = mAccessor.getFollowing(username, lk);
                PageTracker.getInstance().addFollowingLastKey(newFollowing.getFollowing().size());
                for (User user : newFollowing.getFollowing())
                {
                    mFollowing.addFollowing(user);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        mFollowingView.updateField(Global.FOLLOWING, null);
                    }
                });
                }
            }).start();


    }


}
