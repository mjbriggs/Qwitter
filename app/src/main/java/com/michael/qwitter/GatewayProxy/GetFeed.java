package com.michael.qwitter.GatewayProxy;

import android.util.Log;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetFeed;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetStatusList;
import com.michael.qwitter.Model.Feed;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Utils.Global;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GetFeed implements IGetFeed, Callable<Feed>
{
    private Feed mFeed;
    private String mUsername;
    private IGetStatusList mGetStatusList;
    private GetStatusList mStatusGetter;

    public GetFeed(String username, String lastkey)
    {
        mUsername = username;
        mGetStatusList = new GetStatusList(username, "feed", lastkey, "5");
        mStatusGetter =  new GetStatusList(username, "feed", lastkey, "5");
    }

    @Override
    public Feed call() throws Exception
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try
        {
            Future<List<Status>> future = executorService.submit(mStatusGetter);
            mFeed = new Feed();
            mFeed.setStatusList(future.get());
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, e.getMessage(), e);
        }
        return mFeed;
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
