package com.michael.qwitter.Model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String mUserAlias;
    private String mPassword;
    private String mAuthToken;
    private String mUserEmail;
    private String mFirstName;
    private String mLastName;
    private Image mProfilePicture;
    private Story mStory;
    private ArrayList<Status> mStatuses; //serves as both story and feed at the moment
    private Following mFollowing;
    private Followers mFollowers;
    private Feed mFeed;
    //TODO has attachment
    //TODO has feed

    public User(String userAlias, String password)
    {

        mStory = new Story();

        mFeed = new Feed();

        mFollowers = new Followers();
        mFollowers.setUserAlias(userAlias);

        mFollowing = new Following();
        mFollowing.setUserAlias(userAlias);

        mStatuses = new ArrayList<>();

        mUserAlias = userAlias;
        mPassword = password;
    }

    public User(User user)
    {
        this.mFollowers = user.mFollowers;
        this.mFollowing = user.mFollowing;
        this.mUserAlias = user.mUserAlias;
        this.mPassword = user.mPassword;
        this.mStatuses = user.mStatuses; //will update to feed and story
        this.mFirstName = user.mFirstName;
        this.mLastName = user.mLastName;
        this.mAuthToken = user.mAuthToken;
        this.mProfilePicture = user.mProfilePicture;
        this.mStory = user.mStory;
        this.mFeed = user.mFeed;
    }

    public String getUserAlias()
    {
        return mUserAlias;
    }

    public void setUserAlias(String userAlias)
    {
        this.mUserAlias = userAlias;
    }

    public String getPassword()
    {
        return mPassword;
    }

    public void setPassword(String password)
    {
        this.mPassword = password;
    }

    public String getAuthToken()
    {
        return mAuthToken;
    }

    public void setAuthToken(String mAuthToken)
    {
        this.mAuthToken = mAuthToken;
    }

    public String getUserEmail()
    {
        return mUserEmail;
    }

    public void setUserEmail(String mUserEmail)
    {
        this.mUserEmail = mUserEmail;
    }

    public String getFirstName()
    {
        return mFirstName;
    }

    public void setFirstName(String mFirstName)
    {
        this.mFirstName = mFirstName;
    }

    public String getLastName()
    {
        return mLastName;
    }

    public void setLastName(String mLastName)
    {
        this.mLastName = mLastName;
    }

    public Image getProfilePicture()
    {
        return mProfilePicture;
    }

    public void setProfilePicture(Image mProfilePicture)
    {
        this.mProfilePicture = mProfilePicture;
    }

    public ArrayList<Status> getStatuses()
    {
        return mStory.getStatuses();
    }

    public void setStatuses(ArrayList<Status> mStatuses)
    {
        this.mStatuses = mStatuses;
    }

    public void addStatus(Status status)
    {
        mStatuses.add(0, status);
    }

    public Following getFollowing()
    {
        return mFollowing;
    }

    public void setFollowing(Following following)
    {
        this.mFollowing = following;
    }

    public void addFollowing(User user)
    {
        if(!mFollowing.getFollowing().contains(user))
            mFollowing.addFollowing(user);
    }

    public void removeFollowing(User user)
    {
        mFollowing.removeFollowing(user);
    }

    public Followers getFollowers()
    {
        return mFollowers;
    }

    public void setFollowers(Followers followers)
    {
        this.mFollowers = followers;
    }

    public void addFollower(User user)
    {
        if(!mFollowers.getFollowers().contains(user))
            mFollowers.addFollower(user);
    }

    public void removeFollower(User user)
    {
        mFollowers.removeFollower(user);
    }


    public Story getStory()
    {
        return mStory;
    }

    public void setStory(Story mStory)
    {
        this.mStory = mStory;
    }

    public void addStatusToStory(Status status)
    {
        mStory.addStatus(status);
    }

    public List<Status> getFeedList()
    {
        return mFeed.getFeed(mStory.getStatuses());
    }
    public Feed getFeed()
    {
        mFeed.setFollowing(mFollowing.getFollowing());
        return mFeed;
    }

    public void setFeed(Feed mFeed)
    {
        this.mFeed = mFeed;
    }

    public void addStatusToFeed(Status status)
    {
        mFeed.addStatus(status, this);
    }

    @Override
    public String toString()
    {
        return "Alias: " + mUserAlias +
                "\nFirst Name: " + mFirstName +
                "\nLast Name: " + mLastName + "\n";
    }

    @NonNull
    @Override
    public Object clone()
    {
        User u = new User(this.mUserAlias, this.mPassword);
        u.mLastName = this.mLastName;
        u.mFirstName = this.mFirstName;
        //TODO attachment
        return u;
    }
}
