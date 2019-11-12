package com.michael.qwitter.GatewayProxy;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetFeed;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetStatusList;
import com.michael.qwitter.Model.Feed;
import com.michael.qwitter.Model.Status;

import java.util.List;

public class GetFeed implements IGetFeed
{
    private Feed mFeed;
    private String mUsername;
    private IGetStatusList mGetStatusList;

    public GetFeed(String username, String lastkey)
    {
        mUsername = username;
        mGetStatusList = new GetStatusList(username, "feed", lastkey, "5");
    }

    @Override
    public void execute()
    {
        Feed newFeed = new Feed();
        List<Status> statuses = null;
        mGetStatusList.execute();

        while (statuses == null)
        {
            statuses = mGetStatusList.getStatusList();
        }

        newFeed.setStatusList(statuses);

        mFeed = newFeed;

    }

    @Override
    public Feed getFeed()
    {
        return mFeed;
    }
}
