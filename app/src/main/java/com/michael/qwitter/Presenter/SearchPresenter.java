package com.michael.qwitter.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.michael.qwitter.DummyData.DummyUserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IView;

import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class SearchPresenter implements StatusPresenter
{

    private DummyUserDatabase mUserDatabase;
    private List<Status> mStatuses;
    private String mQuery;
    private IAccessor mAccessor;
    private IView mSearchView;
    private List<User> mProfiles;


    public SearchPresenter(String mQuery, IView searchView)
    {
        this();
        this.mQuery = mQuery.toLowerCase();
        this.mSearchView = searchView;
        this.search();
    }

    public SearchPresenter()
    {
        this.mQuery = "";
        mProfiles = new ArrayList<>();
        mStatuses = new ArrayList<>();
        mAccessor = new Accessor();
    }

    private void search()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mStatuses = mAccessor.search(mQuery);
                System.out.println(mQuery + " search returned " + mStatuses.size() + " results");
                for (Status status: mStatuses)
                {

                    Log.i(Global.DEBUG, status.getOwner());
                    mProfiles.add(mAccessor.getUserInfo(status.getOwner()));

                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mSearchView.updateField("SEARCH", null);

                    }
                });
            }
        }).start();
    }

    public Status getStatus(int position)
    {
        return mStatuses.get(position);
    }


    @Override
    public String getUserFullName()
    {
        return "";
    }

    @Override
    public List<Status> getStatuses(String username)
    {
        return mStatuses;
    }

    @Override
    public String getUserAlias(int position)
    {
        return "";
    }

    @Override
    public void update(String username)
    {
        this.search();
    }

    @Override
    public String getUserProfilePic(int pos)
    {
        return mProfiles.get(pos).getProfilePicture().getFilePath();
    }

    @Override
    public String getNameAt(int pos)
    {
        User user = mProfiles.get(pos);
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public Intent getIntent()
    {
        return null;
    }
    @Override
    public void handleStatusClick(Context context, int position)
    {

    }
}
