package com.michael.qwitter.Utils;

public class PageTracker
{
    private int feedLastKey;
    private int storyLastKey;
    private int followersLastKey;
    private int followingLastKey;

    private static PageTracker instance = new PageTracker();

    private PageTracker()
    {
        feedLastKey = 0;
        storyLastKey = 0;
        followersLastKey = 0;
        followingLastKey = 0;
    }

    //Get the only object available
    public static PageTracker getInstance(){
        return instance;
    }

    public void reinit()
    {
        instance = new PageTracker();
    }

    public void addFeedLastKey(int num)
    {
        this.feedLastKey += num;
    }

    public String getFeedLastKey()
    {
        return "" + feedLastKey;
    }

    public void setFeedLastKey(int feedLastKey)
    {
        this.feedLastKey = feedLastKey;
    }

    public void addStoryLastKey(int num)
    {
        this.storyLastKey += num;
    }

    public String getStoryLastKey()
    {
        return "" + storyLastKey;
    }

    public void setStoryLastKey(int storyLastKey)
    {
        this.storyLastKey = storyLastKey;
    }

    public void addFollowersLastKey(int num)
    {
        this.followersLastKey += num;
    }

    public String getFollowersLastKey()
    {
        return "" + followersLastKey;
    }

    public void setFollowersLastKey(int followersLastKey)
    {
        this.followersLastKey = followersLastKey;
    }

    public void addFollowingLastKey(int num)
    {
        this.followingLastKey += num;
    }

    public String getFollowingLastKey()
    {
        return "" + followingLastKey;
    }

    public void setFollowingLastKey(int followingLastKey)
    {
        this.followingLastKey = followingLastKey;
    }
}
