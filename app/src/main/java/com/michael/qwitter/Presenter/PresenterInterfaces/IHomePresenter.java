package com.michael.qwitter.Presenter.PresenterInterfaces;

import android.content.Intent;
import android.view.MenuItem;

public interface IHomePresenter
{
    boolean handleMenu(MenuItem item);

    void handleQuery(String query);

    void openView(String view);

    void savePicture(int requestCode, int responseCode, Intent data);

    void setHomeUser(String username);
}
