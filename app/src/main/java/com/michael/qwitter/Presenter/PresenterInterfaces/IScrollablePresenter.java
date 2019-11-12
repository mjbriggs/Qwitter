package com.michael.qwitter.Presenter.PresenterInterfaces;

public interface IScrollablePresenter
{
    int getItemCount(String type);

    void handler(String type, String ... args);
    void parseText(String type, String ... args);

}
