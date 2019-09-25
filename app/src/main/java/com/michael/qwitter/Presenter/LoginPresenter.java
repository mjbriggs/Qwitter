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

        boolean userCreated = user.getmFirstName() != null && user.getmFirstName().length() > 0;
        userCreated &= user.getmLastName() != null && user.getmLastName().length() > 0;

        return userCreated;
    }

}
