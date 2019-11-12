package com.michael.qwitter.GatewayProxy;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetFollowing;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetUserList;
import com.michael.qwitter.Model.Following;
import com.michael.qwitter.Model.User;

import java.util.List;

public class GetFollowing implements IGetFollowing
{
    private IGetUserList mGetUserList;
    private Following mFollowing;

    public GetFollowing(String username, String lastkey)
    {
        mGetUserList = new GetUserList(username, "all-following", lastkey, "5");
        mFollowing = null;
    }

    @Override
    public void execute()
    {
        Following following = new Following();
        mGetUserList.execute();
        List<User> usersList = null;

        while(usersList == null)
        {
            usersList = mGetUserList.getUserList();
        }

        following.setFollowing(usersList);

        mFollowing = following;
    }

    @Override
    public Following getFollowing()
    {
        return mFollowing;
    }
}
