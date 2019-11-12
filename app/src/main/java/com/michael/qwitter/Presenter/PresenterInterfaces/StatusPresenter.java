package com.michael.qwitter.Presenter.PresenterInterfaces;

import com.michael.qwitter.Model.Status;

import java.util.List;

//Should the ordering be controlled here?
public interface StatusPresenter
{
    List<Status> getStatuses(String username);
    String getUserFullName();
    String getUserAlias(int position);
    Status getStatus(int position);
    void update(String username);
}
