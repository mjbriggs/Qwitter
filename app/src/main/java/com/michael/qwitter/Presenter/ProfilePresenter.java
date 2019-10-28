package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.ModelInterfaces.IAuthentication;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.IProfilePresenter;
import com.michael.qwitter.View.ViewInterfaces.IProfileView;

import java.util.ArrayList;

public class ProfilePresenter implements IProfilePresenter
{
    private UserDatabase mUserDatabase;
    private User mUser;
    private User mLoggedUser;
    private boolean mFollowed;
    private IAuthentication mAuthenticationHandler;
    private IProfileView mProfileView;

    public ProfilePresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
        mUser = null;
        mLoggedUser = null;
        mFollowed = false;
    }

    public  ProfilePresenter(IProfileView profileView, IAuthentication authenticationHandler)
    {
        this();
        mProfileView = profileView;
        mAuthenticationHandler = authenticationHandler;
    }

    private void grabLoggedUser(String username)
    {
        mLoggedUser = mUserDatabase.getUser(username);
    }
    private void grabUser(String username)
    {
        mUser = mUserDatabase.getUser(username);
    }
    private String getName(String username)
    {
        if(mUser == null)
            grabUser(username);

        return mUser.getFirstName() + " " + mUser.getLastName();
    }

    private boolean isFollowed(String loggedUser)
    {
        if(mLoggedUser == null)
            grabLoggedUser(loggedUser);

        mFollowed = mUser.getFollowers().getFollowers().contains(mLoggedUser) &&
                mLoggedUser.getFollowing().getFollowing().contains(mUser);

        return mFollowed;
    }
    private void follow(String username, String loggedUser)
    {
//        if(mAuthenticationHandler.authenticated())
//        {
            if(mUser == null)
                grabUser(username);

            if(mLoggedUser == null)
                grabLoggedUser(loggedUser);

            mUser.addFollower(mLoggedUser);

            mLoggedUser.addFollowing(mUser);

            mUserDatabase.updateUser(mUser.getUserAlias(), mUser);
            mUserDatabase.updateUser(mLoggedUser.getUserAlias(), mLoggedUser);
//        }
    }

    private void unfollow(String username, String loggedUser)
    {
//        if(mAuthenticationHandler.authenticated())
//        {
            if(mUser == null)
                grabUser(username);

            if(mLoggedUser == null)
                grabLoggedUser(loggedUser);

            mUser.removeFollower(mLoggedUser);

            mLoggedUser.removeFollowing(mUser);

            mUserDatabase.updateUser(mUser.getUserAlias(), mUser);
            mUserDatabase.updateUser(mLoggedUser.getUserAlias(), mLoggedUser);
//        }
    }

    @Override
    public void handleFollowClick()
    {
        ArrayList<String> info = mProfileView.profileInfo();
        assert info.size() == 2;

        String username = info.get(0);
        String loggedUser = info.get(1);

        if(mAuthenticationHandler.authenticated())
        {
            if(mFollowed)
            {
                unfollow(username, loggedUser);
            }
            else
            {
                follow(username, loggedUser);
            }
        }
    }

    @Override
    public void toggleFollowButton()
    {

    }

    @Override
    public void setProfileInfo()
    {

    }

    //TODO get profile picture
}
