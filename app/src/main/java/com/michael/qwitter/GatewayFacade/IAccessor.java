package com.michael.qwitter.GatewayFacade;

import com.michael.qwitter.Model.Feed;
import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.Story;
import com.michael.qwitter.Model.User;

import java.util.List;

public interface IAccessor
{
    User getUserInfo(String username);
    Feed getFeed(String username, String lastkey);
    Story getStory(String username, String lastkey);
    Followers getFollowers(String username, String lastkey);
    Following getFollowing(String username, String lastkey);
    boolean isFollowed(String loggedUser, String followUser);
    void follow(String loggedUser, String userToFollow);
    void unfollow(String loggedUser, String userToUnFollow);
    void addStatus(String username, Status status);
    List<Status> search(String query);
    void updateUserInfo(User user);
}
