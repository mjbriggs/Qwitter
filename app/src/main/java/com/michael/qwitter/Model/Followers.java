package com.michael.qwitter.Model;

import java.util.ArrayList;
import java.util.List;

public class Followers
{
    private String mUserAlias;
    private List<User> mFollowers;

    public Followers()
    {
        this.mFollowers = new ArrayList<>();
    }

    public String getUserAlias()
    {
        return mUserAlias;
    }

    public void setUserAlias(String mUser)
    {
        this.mUserAlias = mUser;
    }

    public List<User> getFollowers()
    {
        return mFollowers;
    }

    public void setFollowers(List<User> followers)
    {
        this.mFollowers = followers;
    }

    public void addFollower(User user)
    {
        if(!user.getUserAlias().equalsIgnoreCase(mUserAlias))
            mFollowers.add(user);
    }

    public void removeFollower(User user)
    {
        if(mFollowers.contains(user))
            mFollowers.remove(user);
    }
}
