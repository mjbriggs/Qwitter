package com.michael.qwitter.Presenter;

import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;

public interface RelationPresenter
{
    Followers getFollowers();
    Following getFollowing();
}
