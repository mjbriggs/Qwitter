package com.michael.qwitter.Presenter;

import android.util.Log;

import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.Utils.PageTracker;

import java.util.ArrayList;
import java.util.List;

public class FeedPresenter implements StatusPresenter
{
    private String mUserFullName;
    private List<Status> mFeedList;
    private IAccessor mAccessor;
    private int lastkey;

    public FeedPresenter()
    {
        mFeedList = new ArrayList<>();
        mAccessor = new Accessor();
        mUserFullName = "";
        lastkey = 0;
    }
    @Override
    public List<Status> getStatuses(String username)
    {
//        User user = mUserDatabase.getUser(username);

        if(mFeedList.size() == 0)
            this.update(username);

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
        mUserFullName = user.getFirstName() + " " + user.getLastName();
//        System.out.println("returning " + status.getOwner() + " as status owner");
        return status.getOwner();
    }

    @Override
    public void update(String username)
    {
        User user = mAccessor.getUserInfo(username);

        mUserFullName = user.getFirstName() + " " + user.getLastName();

        String lk = PageTracker.getInstance().getFeedLastKey();
        Log.i(Global.INFO, "lk before update is " + PageTracker.getInstance().getFeedLastKey());
        List<Status> newStatuses = mAccessor.getFeed(username, lk).getStatusList();
        lastkey += newStatuses.size();
        PageTracker.getInstance().addFeedLastKey(newStatuses.size());
        Log.i(Global.INFO, "lk after update is " + PageTracker.getInstance().getFeedLastKey());

        mFeedList.addAll(newStatuses);
    }

    public Status getStatus(int position)
    {
        return null;
    }
}
