package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.ModelInterfaces.IAuthentication;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.IProfilePresenter;
import com.michael.qwitter.Utils.PageTracker;
import com.michael.qwitter.View.ViewInterfaces.IProfileView;

import java.util.ArrayList;

public class ProfilePresenter implements IProfilePresenter
{
    private UserDatabase mUserDatabase;
    private String mLoggedUserAlias;
    private String mFollowUserAlias;
    private User mUser;
    private User mLoggedUser;
    private boolean mFollowed;
    private IAuthentication mAuthenticationHandler;
    private IProfileView mProfileView;
    private IAccessor mAccessor;

    public ProfilePresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
        PageTracker.getInstance().reinit();
        mUser = null;
        mLoggedUser = null;
        mFollowed = false;
        mAccessor = new Accessor();
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
        mUser = mAccessor.getUserInfo(username);
    }
    private String getName(String username)
    {
        if(mUser == null)
            grabUser(username);

        return mUser.getFirstName() + " " + mUser.getLastName();
    }

    private boolean isFollowed(String loggedUser)
    {
//        if(mLoggedUser == null)
//            grabLoggedUser(loggedUser);
//
//        assert mUser != null;
//
//        mFollowed = mUser.getFollowers().getFollowers().contains(mLoggedUser) &&
//                mLoggedUser.getFollowing().getFollowing().contains(mUser);

        mFollowed = mAccessor.isFollowed(mLoggedUserAlias, mFollowUserAlias);

        return mFollowed;
    }
    private void follow(String username, String loggedUser)
    {

        mAccessor.follow(loggedUser, username);
        mFollowed = true;
//        if(mAuthenticationHandler.authenticated())
//        {
//            if(mUser == null)
//                grabUser(username);
//
//            if(mLoggedUser == null)
//                grabLoggedUser(loggedUser);
//
//            mUser.addFollower(mLoggedUser);
//
//            mLoggedUser.addFollowing(mUser);
//
//            mUserDatabase.updateUser(mUser.getUserAlias(), mUser);
//            mUserDatabase.updateUser(mLoggedUser.getUserAlias(), mLoggedUser);
//        }
    }

    private void unfollow(String username, String loggedUser)
    {
        mAccessor.unfollow(loggedUser, username);
        mFollowed = false;
//        if(mAuthenticationHandler.authenticated())
//        {
//            if(mUser == null)
//                grabUser(username);
//
//            if(mLoggedUser == null)
//                grabLoggedUser(loggedUser);
//
//            mUser.removeFollower(mLoggedUser);
//
//            mLoggedUser.removeFollowing(mUser);
//
//            mUserDatabase.updateUser(mUser.getUserAlias(), mUser);
//            mUserDatabase.updateUser(mLoggedUser.getUserAlias(), mLoggedUser);
//        }
    }

    @Override
    public void handleFollowClick()
    {
        ArrayList<String> info = mProfileView.profileInfo();
        assert info.size() == 2;

        String username = info.get(0);
        String loggedUser = info.get(1);
        mFollowUserAlias = username;
        mLoggedUserAlias = loggedUser;

        if(mAuthenticationHandler.authenticated())
        {
            if(mFollowed)
            {
                unfollow(username, loggedUser);
                mProfileView.updateField("R.id.follow_button", "follow");
            }
            else
            {
                follow(username, loggedUser);
                mProfileView.updateField("R.id.follow_button", "un-follow");
            }
        }
    }

    @Override
    public void setProfileInfo()
    {
        ArrayList<String> info = mProfileView.profileInfo();
        assert info.size() == 2;
        String username = info.get(0);
        String loggedUser = info.get(1);
        mFollowUserAlias = username;
        mLoggedUserAlias = loggedUser;
        this.grabUser(username);
        mProfileView.updateField("R.id.follow_name", mUser.getFirstName() + " " + mUser.getLastName());
        mProfileView.updateField("R.id.follow_user_alias", username);

        this.isFollowed(loggedUser);

        if(username.equalsIgnoreCase(loggedUser) || !mAuthenticationHandler.authenticated())
        {
            mProfileView.updateField("Hide.R.id.follow_button", null);
        }
        else if(mFollowed)
        {
            mProfileView.updateField("R.id.follow_button", "un-follow");
        }
        else
        {
            mProfileView.updateField("R.id.follow_button", "follow");
        }

    }

    //TODO get profile picture
}
