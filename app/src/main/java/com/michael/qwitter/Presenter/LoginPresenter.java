package com.michael.qwitter.Presenter;

import com.michael.qwitter.Model.User;

public class LoginPresenter implements LoginPresenterInterface
{
    private User mUser;

    public LoginPresenter()
    {
        mUser = new User("", "");
    }

    @Override
    public void addUser(String username, String password)
    {
        mUser.setUserAlias(username);
        mUser.setPassword(password);
        // TODO Add user to database
    }
}
