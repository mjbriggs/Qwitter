package com.michael.qwitter.Model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Hashtag
{
    private String mTag;
    private List<Status> mHashTagList;

    public Hashtag()
    {
        mHashTagList = new ArrayList<>();
    }
    public Hashtag(String tag)
    {
        this();
        mTag = tag.toLowerCase();
    }


    public List<Status> getHashTagList()
    {
        return mHashTagList;
    }

    public void setHashTagList(List<Status> mHashTagList)
    {
        this.mHashTagList = mHashTagList;
    }

    public String getTag()
    {
        return mTag;
    }

    public void setTag(String mTag)
    {
        this.mTag = mTag.toLowerCase();
    }

    public void addStatus(Status status)
    {
        if(mHashTagList.size() == 0)
        {
            mHashTagList.add(status);
        }
        else
        {
            for(int i = 0; i < mHashTagList.size(); i++)
            {
                if(status.equals(mHashTagList.get(i)))
                {
                    return;
                }
            }
            mHashTagList.add(status);
        }
    }

    @NonNull
    @Override
    public String toString()
    {
        return mTag;
    }
}
