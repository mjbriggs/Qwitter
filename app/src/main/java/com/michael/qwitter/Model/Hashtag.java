package com.michael.qwitter.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hashtag
{
   private  Map<String , List<Status>> mHashTagList;

    public Hashtag()
    {
        mHashTagList = new HashMap<>();
    }

    public Map<String, List<Status>> getHashTagList()
    {
        return mHashTagList;
    }

    public void setHashTagList(Map<String, List<Status>> mHashTagList)
    {
        this.mHashTagList = mHashTagList;
    }

    public boolean doesHashTagExist(String tag)
    {
        tag = tag.toLowerCase();
        return mHashTagList.containsKey(tag);
    }

    public void createHashTag(String tag)
    {
        mHashTagList.put(tag.toLowerCase(), new ArrayList<Status>());
    }

    public void addStatus(String tag, Status status)
    {
        if (!doesHashTagExist(tag))
        {
            createHashTag(tag);
        }

        ArrayList<Status> statuses = (ArrayList<Status>) mHashTagList.get(tag);
        statuses.add(status);
        mHashTagList.put(tag, statuses);
    }

    public List<Status> getStatusByTag(String tag)
    {
        return mHashTagList.get(tag);
    }


}
