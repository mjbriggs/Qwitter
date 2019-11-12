package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.DummyUserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements StatusPresenter
{

    private DummyUserDatabase mUserDatabase;
    private List<Status> mStatuses;
    private String mQuery;
    private IAccessor mAccessor;

    public SearchPresenter(String mQuery)
    {
        this();
        this.mQuery = mQuery.toLowerCase();
        this.search();
        System.out.println(mQuery + " search returned " + mStatuses.size() + " results");
    }

    public SearchPresenter()
    {
        mStatuses = new ArrayList<>();
        mAccessor = new Accessor();
    }

    private void search()
    {
       mStatuses = mAccessor.search(mQuery);
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

    }
}
