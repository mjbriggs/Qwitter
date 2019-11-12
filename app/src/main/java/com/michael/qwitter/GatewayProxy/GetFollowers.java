package com.michael.qwitter.GatewayProxy;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetFollowers;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetUserList;
import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.User;

import java.util.List;

public class GetFollowers implements IGetFollowers
{
    private IGetUserList mGetUserList;
    private Followers mFollowers;

    public GetFollowers(String username, String lastKey)
    {
        mGetUserList = new GetUserList(username, "all-followers", lastKey, "5");
        mFollowers = null;
    }

    @Override
    public void execute()
    {
        Followers followers = new Followers();
        mGetUserList.execute();
        List<User> usersList = null;

        while(usersList == null)
        {
            usersList = mGetUserList.getUserList();
        }

        followers.setFollowers(usersList);

        mFollowers = followers;
    }

    @Override
    public Followers getFollowers()
    {
        return mFollowers;
    }
}
