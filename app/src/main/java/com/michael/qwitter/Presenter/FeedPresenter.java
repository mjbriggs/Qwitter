package com.michael.qwitter.Presenter;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;

import java.util.ArrayList;
import java.util.List;

public class FeedPresenter implements StatusPresenter
{
    private UserDatabase mUserDatabase = UserDatabase.getInstance();
    private String mUserFullName;
    private List<Status> mFeedList;

    public FeedPresenter()
    {
        mFeedList = new ArrayList<>();
    }
    @Override
    public List<Status> getStatuses(String username)
    {
        User user = mUserDatabase.getUser(username);
        mUserFullName = user.getFirstName() + " " + user.getLastName();
        List<User> following = user.getFollowing().getFollowing();

        //mFeed = mergeSort(mFeed);
        //TODO get statuses from followed users
        System.out.println("get statuses" + user.getFeed().getStatusList().toString());
        mFeedList = user.getFeedList();
        return mFeedList;
    }

    @Override
    public String getUserFullName()
    {
        return mUserFullName;
    }

    @Override
    public String getUserAlias(int position)
    {
        Status status = mFeedList.get(position);
        mUserFullName = status.getOwnerName();
        System.out.println("returning " + status.getOwner() + " as status owner");
        return status.getOwner();
    }
}
