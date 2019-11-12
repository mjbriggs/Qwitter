package com.michael.qwitter.Presenter.PresenterInterfaces;

import com.michael.qwitter.Model.Followers;
import com.michael.qwitter.Model.Following;

public interface RelationPresenter
{
    Followers getFollowers();
    Following getFollowing();
    void update(String username);
}
