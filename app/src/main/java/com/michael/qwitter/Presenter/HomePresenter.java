package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.DummyUserDatabase;
import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.User;

public class HomePresenter
{
    private DummyUserDatabase mUserDatabase;
    private User mHomeUser;

    //TODO talk to adapter to update status list, for now I'll just print the list of statuses
    public HomePresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
        mHomeUser = null;
    }

    public boolean findLoggedInUser(String username)
    {
        setHomeUser(mUserDatabase.getUser(username));
        if(!mUserDatabase.validateAuthToken(mHomeUser.getUserAlias(), mHomeUser.getAuthToken()))
        {
            mHomeUser = null;
        }

        return mHomeUser != null;
    }

    public User getHomeUser()
    {
        return mHomeUser;
    }

    public void setHomeUser(User mHomeUser)
    {
        this.mHomeUser = mHomeUser;
    }

}
