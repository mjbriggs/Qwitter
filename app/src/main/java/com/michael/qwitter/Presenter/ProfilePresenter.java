package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.User;

public class ProfilePresenter
{
    private UserDatabase mUserDatabase;
    private User mUser;
    private User mLoggedUser;

    public ProfilePresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
        mUser = null;
        mLoggedUser = null;
    }

    private void grabLoggedUser(String username)
    {
        mLoggedUser = mUserDatabase.getUser(username);
    }
    private void grabUser(String username)
    {
        mUser = mUserDatabase.getUser(username);
    }
    public String getName(String username)
    {
        if(mUser == null)
            grabUser(username);

        return mUser.getFirstName() + " " + mUser.getLastName();
    }

    public boolean isFollowed(String loggedUser)
    {
        if(mLoggedUser == null)
            grabLoggedUser(loggedUser);

        return mUser.getFollowers().getFollowers().contains(mLoggedUser) &&
                mLoggedUser.getFollowing().getFollowing().contains(mUser);
    }
    public void follow(String username, String loggedUser)
    {
        if(mUser == null)
            grabUser(username);

        if(mLoggedUser == null)
            grabLoggedUser(loggedUser);

        mUser.addFollower(mLoggedUser);

        mLoggedUser.addFollowing(mUser);

        mUserDatabase.updateUser(mUser.getUserAlias(), mUser);
        mUserDatabase.updateUser(mLoggedUser.getUserAlias(), mLoggedUser);
    }

    public void unfollow(String username, String loggedUser)
    {
        if(mUser == null)
            grabUser(username);

        if(mLoggedUser == null)
            grabLoggedUser(loggedUser);

        mUser.removeFollower(mLoggedUser);

        mLoggedUser.removeFollowing(mUser);

        mUserDatabase.updateUser(mUser.getUserAlias(), mUser);
        mUserDatabase.updateUser(mLoggedUser.getUserAlias(), mLoggedUser);
    }

    //TODO get profile picture
}
