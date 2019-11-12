package com.michael.qwitter.Presenter.PresenterFactory;

import com.michael.qwitter.View.ViewInterfaces.IView;

public abstract class ACPresenterFactory
{
    public Object createPresenter(String type, IView view)
    {
        return null;
    }

    private static ACPresenterFactory instance;

    public static ACPresenterFactory getInstance()
    {
        return instance;
    }

    public static void setInstance(ACPresenterFactory factory)
    {
        instance = factory;
    }

}
