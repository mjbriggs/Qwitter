package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.User;

public class NewUserInfoPresenter extends RegistrationPresenter
{
    private UserDatabase mUserDatabase;

    public NewUserInfoPresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
    }
    @Override
    public void updateUserInfo(String username, String firstName, String lastName) //TODO pass in attachment arg as well
    {
        User user = mUserDatabase.getUser(username);
        user.setmFirstName(firstName);
        user.setmLastName(lastName);
        //TODO set attachment
        mUserDatabase.updateUser(username, user);
        System.out.println("Updated user info \n" + mUserDatabase.getUser(username).toString());
    }
}
