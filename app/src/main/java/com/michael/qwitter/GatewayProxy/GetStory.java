package com.michael.qwitter.GatewayProxy;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetStatusList;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetStory;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.Story;

import java.util.ArrayList;

public class GetStory implements IGetStory
{
    private Story mStory;
    private String mUsername;
    private IGetStatusList mGetStatusList;

    public GetStory(String username, String lastkey)
    {
        mUsername = username;
        mGetStatusList = new GetStatusList(username, "story",lastkey, "5");
    }

    @Override
    public void execute()
    {
        Story newStory = new Story();
        ArrayList<Status> statuses = null;
        mGetStatusList.execute();

        while (statuses == null)
        {
            statuses = (ArrayList) mGetStatusList.getStatusList();
        }

        newStory.setStatuses(statuses);

        mStory = newStory;

    }

    @Override
    public Story getStory()
    {
        return mStory;
    }
}
