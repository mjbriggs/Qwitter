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

public class StoryPresenter implements StatusPresenter
{

    private String mUserFullName;
    private List<Status> mStoryList;
    private IAccessor mAccessor;

    public StoryPresenter()
    {
        mUserFullName = " ";
        mAccessor = new Accessor();
        mStoryList = new ArrayList<>();
    }

    @Override
    public List<Status> getStatuses(String username)
    {
        if(mStoryList.size() == 0)
            this.update(username);

        return mStoryList;
    }

    @Override
    public String getUserFullName()
    {
        return mUserFullName;
    }

    @Override
    public String getUserAlias(int position)
    {
        return "";
    }

    public Status getStatus(int position)
    {
        return null;
    }

    @Override
    public void update(String username)
    {
        User user = mAccessor.getUserInfo(username);

        mUserFullName = user.getFirstName() + " " + user.getLastName();

        String lk = PageTracker.getInstance().getStoryLastKey();
        Log.i(Global.INFO, "lk before update is " + PageTracker.getInstance().getStoryLastKey());
        List<Status> newStatuses = mAccessor.getStory(username, lk).getStatuses();
        PageTracker.getInstance().addStoryLastKey(newStatuses.size());
        Log.i(Global.INFO, "lk after update is " + PageTracker.getInstance().getStoryLastKey());

        mStoryList.addAll(newStatuses);
    }
}
