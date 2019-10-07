package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.DummyUserDatabase;
import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.Hashtag;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchPresenter implements StatusPresenter
{

    private DummyUserDatabase mUserDatabase;
    private List<Status> mStatuses;
    private String mQuery;

    public SearchPresenter(String mQuery)
    {
        this();
        this.mQuery = mQuery.toLowerCase();
        this.search();
        System.out.println(mQuery + " search returned " + mStatuses.size() + " results");
    }

    public SearchPresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
        mStatuses = new ArrayList<>();
    }

    private void search()
    {
        Map<String, User> users = mUserDatabase.getUsers();
        List<Status> userStory;
        List<Hashtag> tags;
        for (User user : users.values())
        {
            userStory = user.getStory().getStatuses();

            for(Status status : userStory)
            {
                tags = status.getHashTags();
                for(Hashtag tag : tags)
                {
                    if(tag.getTag().equalsIgnoreCase(mQuery))
                    {
                        mStatuses.add(status);
                        break;
                    }
                }
            }
        }
    }

    public Status getStatus(int position)
    {
        return mStatuses.get(position);
    }


    @Override
    public String getUserFullName()
    {
        return "";
    }

    @Override
    public List<Status> getStatuses(String username)
    {
        return mStatuses;
    }

    @Override
    public String getUserAlias(int position)
    {
        return "";
    }
}
