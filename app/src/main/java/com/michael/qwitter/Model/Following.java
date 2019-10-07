package com.michael.qwitter.Model;

import java.util.ArrayList;
import java.util.List;

public class Following
{
    private String mUserAlias;
    private List<User> mFollowing;

    public Following()
    {
        this.mFollowing = new ArrayList<>();
    }

    public String getUserAlias()
    {
        return mUserAlias;
    }

    public void setUserAlias(String mUser)
    {
        this.mUserAlias = mUser;
    }

    public List<User> getFollowing()
    {
        return mFollowing;
    }

    public void setFollowing(List<User> following)
    {
        this.mFollowing = following;
    }

    public void addFollowing(User user)
    {
        if(!user.getUserAlias().equalsIgnoreCase(mUserAlias))
            mFollowing.add(user);
    }

    public void removeFollowing(User user)
    {
        if(mFollowing.contains(user))
            mFollowing.remove(user);
    }

}
