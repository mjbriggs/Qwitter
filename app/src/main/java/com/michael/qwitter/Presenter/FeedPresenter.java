package com.michael.qwitter.Presenter;

import android.util.Log;

import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.Utils.PageTracker;
import com.michael.qwitter.View.ViewInterfaces.IView;

import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class FeedPresenter implements StatusPresenter
{
    private String mUserFullName;
    private List<Status> mFeedList;
    private List<String> mProfileLinks;
    private IAccessor mAccessor;
    private int lastkey;
    private IView mFeedView;

    public FeedPresenter()
    {
        PageTracker.getInstance().setFeedLastKey(0);
        mFeedList = new ArrayList<>();
        mAccessor = new Accessor();
        mProfileLinks = new ArrayList<>();
        mUserFullName = "";
        lastkey = 0;
    }

    public FeedPresenter(IView feedView)
    {
        this();
        mFeedView = feedView;
    }
    @Override
    public List<Status> getStatuses(String username)
    {
//        User user = mUserDatabase.getUser(username);

//        if(mFeedList.size() == 0)
//            this.update(username);

        return mFeedList;
    }

    @Override
    public String getUserFullName()
    {
        return mUserFullName;
    }

    @Override
    public String getUserAlias(int position)
    {
        Status status = mFeedList.get(position);
        User user = mAccessor.getUserInfo(status.getOwner());
//        mUserFullName = status.getOwnerName();
        mUserFullName = user.getFirstName() + " " + user.getLastName();
//        System.out.println("returning " + status.getOwner() + " as status owner");
        return status.getOwner();
    }

    @Override
    public void update(String usernameIn)
    {
        //TODO handle null results when network is down
        final String username = usernameIn;
        new Thread(new Runnable() {
            public void run() {
                User user = mAccessor.getUserInfo(username);

                mUserFullName = user.getFirstName() + " " + user.getLastName();

                String lk = "";
                if (mFeedList.size() > 0)
                {
                    lk = mFeedList.get(mFeedList.size() - 1).getTimestamp();
                }
                List<Status> newStatuses = mAccessor.getFeed(username, lk).getStatusList();
                Log.i(Global.INFO, "lk after update is " + PageTracker.getInstance().getFeedLastKey());

                for (Status status: newStatuses)
                {

                    Log.i(Global.DEBUG, status.getOwner());
                    mProfileLinks.add(mAccessor.getUserInfo(status.getOwner()).getProfilePicture().getFilePath());

                }
                mFeedList.addAll(newStatuses);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        mFeedView.updateField(Global.FEED, null);
                    }
                });
            }
        }).start();
    }

    public Status getStatus(int position)
    {
        return null;
    }

    @Override
    public String getUserProfilePic(int pos)
    {
        return mProfileLinks.get(pos);
    }

    @Override
    public String getNameAt(int pos)
    {
        return null;
    }
}
