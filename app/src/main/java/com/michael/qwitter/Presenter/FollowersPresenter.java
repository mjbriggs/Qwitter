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

public class FollowersPresenter implements RelationPresenter
{

    private Followers mFollowers;
    private IAccessor mAccessor;
    private String mUsername;
    private IView mFollowersView;

    public FollowersPresenter(String username)
    {
        PageTracker.getInstance().setFollowersLastKey(0);
        mAccessor = new Accessor();
        mUsername = username;
    }

    public FollowersPresenter(String username, IView followersView)
    {
        this(username);
        mFollowersView = followersView;
        mFollowers = new Followers();
    }

    @Override
    public Followers getFollowers()
    {
//        if(mFollowers == null)
//        {
//            mFollowers = new Followers();
//            this.update(mUsername);
//        }

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
    public void update(String usernameIn)
    {
        final String username = usernameIn;
        new Thread(new Runnable() {
            public void run() {
//                User user = mAccessor.getUserInfo(username);

//                mUserFullName = user.getFirstName() + " " + user.getLastName();
//
//                String lk = PageTracker.getInstance().getStoryLastKey();
//                Log.i(Global.INFO, "story lk before update is " + PageTracker.getInstance().getStoryLastKey());
//                List<Status> newStatuses = mAccessor.getStory(username, lk).getStatuses();
//                PageTracker.getInstance().addStoryLastKey(newStatuses.size());
//                Log.i(Global.INFO, "story lk after update is " + PageTracker.getInstance().getStoryLastKey());
//
//                mStoryList.addAll(newStatuses);

                String lk = "";
                if (mFollowers.getFollowers().size() > 0)
                {
                    lk = mFollowers.getFollowers().get(mFollowers.getFollowers().size() - 1).getUserAlias();
                }
                Followers newFollowers = mAccessor.getFollowers(username, lk);
                PageTracker.getInstance().addFollowersLastKey(newFollowers.getFollowers().size());
                for (User user : newFollowers.getFollowers())
                {
                    mFollowers.addFollower(user);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        mFollowersView.updateField(Global.FOLLOWERS, null);
                    }
                });
            }
        }).start();

    }


}
