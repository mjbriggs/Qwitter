package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.User;

public class RegistrationPresenter implements RegistrationInterface
{
    private User mUser;
    private UserDatabase mUserDatabase;

    public RegistrationPresenter()
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

    @Override
    public void updateUserInfo(String username, String firstName, String lastName) {}//TODO pass in attachment arg as well

    @Override
    public boolean isUserCreated(String username)
    {
        return username != null;
    }
}
