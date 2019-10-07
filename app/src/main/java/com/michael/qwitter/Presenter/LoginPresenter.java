package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.User;

public class LoginPresenter extends RegistrationPresenter
{
    private UserDatabase mUserDatabase;

    public LoginPresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
    }

    @Override
    public boolean isUserCreated(String username)
    {
        User user = mUserDatabase.getUser(username);

        System.out.println(user.toString());

        boolean userCreated = user.getFirstName() != null && user.getFirstName().length() > 0;
        userCreated &= user.getLastName() != null && user.getLastName().length() > 0;

        return userCreated;
    }

}
