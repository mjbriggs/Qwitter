package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;

import java.util.List;

public class StoryPresenter implements StatusPresenter
{

    private UserDatabase mUserDatabase = UserDatabase.getInstance();
    private String mUserFullName;

    @Override
    public List<Status> getStatuses(String username)
    {
        User user = mUserDatabase.getUser(username);
        mUserFullName = user.getFirstName() + " " + user.getLastName();

        return user.getStory().getStatuses();
    }

    @Override
    public String getUserFullName()
    {
        return mUserFullName;
    }

    @Override
    public String getUserAlias(int position)
    {
        return "";
    }
}
