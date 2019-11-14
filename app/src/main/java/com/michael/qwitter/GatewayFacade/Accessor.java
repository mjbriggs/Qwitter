package com.michael.qwitter.GatewayFacade;

import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.michael.qwitter.GatewayProxy.AddStatus;
import com.michael.qwitter.GatewayProxy.Follow;
import com.michael.qwitter.GatewayProxy.GetFeed;
import com.michael.qwitter.GatewayProxy.GetFollowers;
import com.michael.qwitter.GatewayProxy.GetFollowing;
import com.michael.qwitter.GatewayProxy.GetStory;
import com.michael.qwitter.GatewayProxy.GetUserInfo;
import com.michael.qwitter.GatewayProxy.IsFollowing;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetFollowers;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetFollowing;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetStory;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IIsFollowing;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IProxy;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.ISearch;
import com.michael.qwitter.GatewayProxy.Search;
import com.michael.qwitter.GatewayProxy.UnFollow;
import com.michael.qwitter.GatewayProxy.UpdateUserInfo;
import com.michael.qwitter.Model.Feed;
import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.Story;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Utils.Global;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Accessor implements IAccessor
{
    private Callable<User> mGetUserInfo;
    private Callable<Feed> mGetFeed;
    private IGetStory mGetStory;
    private IGetFollowers mGetFollowers;
    private IGetFollowing mGetFollowing;
    private IIsFollowing mIsFollowing;
    private IProxy mFollow;
    private IProxy mUnFollow;
    private IProxy mAddStatus;
    private ISearch mSearch;
    private IProxy mUpdateUserInfo;

    public Accessor() {}
    public Accessor(Callable<User> getUserInfo, Callable<Feed> getFeed, IGetStory getStory, IGetFollowers getFollowers,
                    IGetFollowing getFollowing, IIsFollowing isFollowing, IProxy follow, IProxy unfollow, IProxy addStatus,
                    ISearch search, IProxy updateUserInfo)
    {
        mGetUserInfo = getUserInfo;
        mGetFeed = getFeed;
        mGetStory = getStory;
        mGetFollowers = getFollowers;
        mGetFollowing = getFollowing;
        mIsFollowing = isFollowing;
        mFollow = follow;
        mUnFollow = unfollow;
        mAddStatus = addStatus;
        mSearch = search;
        mUpdateUserInfo = updateUserInfo;
    }
    @Override
    public User getUserInfo(String username)
    {
        if(mGetUserInfo == null)
            mGetUserInfo = new GetUserInfo(username);
        User user = new User("","");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try
        {
            Future<User> future = executorService.submit(mGetUserInfo);
            Log.i(Global.INFO ," getting user info for " + username);
            user = future.get();
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, e.getMessage(), e);
        }
        Log.i(Global.INFO ," got user info for " + username);

        return user;
    }

    @Override
    public Feed getFeed(String username, String lastkey)
    {
//        if(mGetFeed == null)
        mGetFeed = new GetFeed(username, lastkey);
        Feed feed = new Feed();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try
        {
            Future<Feed> future = executorService.submit(mGetFeed);
            Log.i(Global.INFO ," getting user info for " + username);
            feed = future.get();
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, e.getMessage(), e);
        }
        finally
        {
            executorService.shutdown();
        }
        Log.i(Global.INFO ," got user info for " + username);

//        mGetFeed.execute();
//
//        Feed feed = mGetFeed.getFeed();
//
//        while (feed == null)
//            feed = mGetFeed.getFeed();

        return feed;
    }

    @Override
    public Story getStory(String username, String lastKey)
    {
//        if(mGetStory == null)
        mGetStory = new GetStory(username, lastKey);
//
        mGetStory.execute();

        Story story = mGetStory.getStory();
        while (story == null)
            story = mGetStory.getStory();

        return story;
    }

    @Override
    public Followers getFollowers(String username, String lastkey)
    {
//        if(mGetFollowers == null)
            mGetFollowers = new GetFollowers(username, lastkey);

        mGetFollowers.execute();

        Followers followers = null;

        while (followers == null)
        {
            followers = mGetFollowers.getFollowers();
        }
        return followers;
    }

    @Override
    public Following getFollowing(String username, String lastkey)
    {
//        if(mGetFollowing == null)
        mGetFollowing = new GetFollowing(username, lastkey);
//
        mGetFollowing.execute();

        Following following = null;

        while (following == null)
        {
            following = mGetFollowing.getFollowing();
        }
        return following;
    }

    @Override
    public boolean isFollowed(String loggedUser, String followUser)
    {
        if(mIsFollowing == null)
            mIsFollowing = new IsFollowing(loggedUser, followUser);

        mIsFollowing.execute();
        Boolean isFollowing = null;

        while (isFollowing == null)
        {
            isFollowing = mIsFollowing.isFollowing();
        }
        return isFollowing;
    }

    @Override
    public void follow(String loggedUser, String userToFollow)
    {
        try
        {
            String token = AWSMobileClient.getInstance().getTokens().getAccessToken().getTokenString();
            if (mFollow == null)
                mFollow = new Follow(loggedUser, userToFollow, token);

            mFollow.execute();
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, "Unable to authenticate " + loggedUser + " to follow " + userToFollow, e);
        }
    }

    @Override
    public void unfollow(String loggedUser, String userToUnFollow)
    {
        try
        {
            String token = AWSMobileClient.getInstance().getTokens().getAccessToken().getTokenString();
            if (mUnFollow == null)
                mUnFollow = new UnFollow(loggedUser, userToUnFollow, token);

            mUnFollow.execute();
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, "Unable to authenticate " + loggedUser + " to unfollow " + userToUnFollow, e);
        }
    }

    @Override
    public void addStatus(String username, Status status)
    {
        try
        {
            String token = AWSMobileClient.getInstance().getTokens().getAccessToken().getTokenString();
            if (mAddStatus == null)
                mAddStatus = new AddStatus(username, status, token);

            mAddStatus.execute();
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, "Unable to authenticate " + username + " to add status", e);
        }
    }

    @Override
    public List<Status> search(String query)
    {
        if (mSearch == null)
            mSearch = new Search(query);

        List<Status> statuses = null;
        mSearch.execute();

        while (statuses == null)
        {
            statuses = mSearch.getResult();
        }

        return statuses;
    }

    @Override
    public void updateUserInfo(User user)
    {
        if(mUpdateUserInfo == null)
            mUpdateUserInfo = new UpdateUserInfo(user);

        mUpdateUserInfo.execute();
    }
}
