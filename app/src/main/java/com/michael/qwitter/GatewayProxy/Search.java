package com.michael.qwitter.GatewayProxy;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetStatusList;
import com.michael.qwitter.GatewayProxy.ProxyInterfaces.ISearch;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Utils.Global;

import java.util.List;

public class Search implements ISearch
{

    private String mUrl;
    private String mQuery;
    private List<Status> mStatusList;
    private IGetStatusList mGetStatusList;
    private String mTmpStatus;

    public Search(String query)
    {
        String searchType = "";
        if(query.charAt(0) == '#')
        {
            searchType = "hashtag";
            mQuery = query.substring(1);
        }
        else if(query.charAt(0) == '@')
        {
            searchType = "mention";
            mQuery = query.substring(1);
        }
        else
        {
            mQuery = query;
        }

        mUrl = Global.BASE_URL + "search?query=" + mQuery + "&type=" + searchType;
        mStatusList = null;
        mGetStatusList = new GetStatusList("", "results", "", "", mUrl);
    }

    @Override
    public void execute()
    {
        List<Status> statuses = null;
        mGetStatusList.execute();

        while (statuses == null)
        {
            statuses = mGetStatusList.getStatusList();
        }

        mStatusList = statuses;
    }

    @Override
    public List<Status> getResult()
    {
        return mStatusList;
    }
}
