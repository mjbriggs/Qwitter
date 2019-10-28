package com.michael.qwitter.Presenter.PresenterFactory;

import com.michael.qwitter.View.ViewInterfaces.IView;

public interface IPresenterFactory
{
    Object createPresenter(String type, IView view);
}
