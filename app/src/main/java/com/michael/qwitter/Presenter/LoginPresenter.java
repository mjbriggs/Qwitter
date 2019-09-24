package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.User;

public class LoginPresenter implements LoginPresenterInterface
{
    private User mUser;
    private UserDatabase mUserDatabase;

    public LoginPresenter()
    {
        mUser = new User("", "");
        mUserDatabase = UserDatabase.getInstance();
    }

    @Override
    public void addUser(String username, String password)
    {
        System.out.println("presenter add of user");
        mUser.setUserAlias(username);
        mUser.setPassword(password);
        // TODO Add user to database
        mUser.setAuthToken(mUserDatabase.generateAuthToken());
        mUserDatabase.addUser(mUser);
    }

    @Override
    public String validateUser(String username, String password)
    {
        if(mUserDatabase.userExists(username))
        {
            User potentialUser = mUserDatabase.getUser(username);
            if(potentialUser.getPassword().equals(password))
            {
                potentialUser.setAuthToken(mUserDatabase.generateAuthToken());
                mUserDatabase.updateUser(username, potentialUser);
                return potentialUser.getAuthToken();
            }
        }

        return "";
    }

}
