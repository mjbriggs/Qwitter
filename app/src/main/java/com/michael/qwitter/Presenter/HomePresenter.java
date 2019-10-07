package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.User;

public class HomePresenter
{
    private UserDatabase mUserDatabase;
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

    public void logoutUser()
    {
        mHomeUser.setAuthToken("");
        mUserDatabase.updateUser(mHomeUser.getUserAlias(), mHomeUser);
    }

    public boolean doesUserExist(String username)
    {
        return mUserDatabase.userExists(username);
    }

}
