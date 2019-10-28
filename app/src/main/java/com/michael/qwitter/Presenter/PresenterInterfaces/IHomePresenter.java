package com.michael.qwitter.Presenter.PresenterInterfaces;

import android.view.MenuItem;

public interface IHomePresenter
{
    boolean handleMenu(MenuItem item);
    void handleQuery(String query);
    void openView(String view);

}
