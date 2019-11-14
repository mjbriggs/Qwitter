package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.DummyUserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;
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
}
