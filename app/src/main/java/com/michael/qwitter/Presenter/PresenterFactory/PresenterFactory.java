package com.michael.qwitter.Presenter.PresenterFactory;

import com.michael.qwitter.Model.AuthHandler;
import com.michael.qwitter.Presenter.HomePresenter;
import com.michael.qwitter.Presenter.ProfilePresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IHomeView;
import com.michael.qwitter.View.ViewInterfaces.IProfileView;
import com.michael.qwitter.View.ViewInterfaces.IView;

public class PresenterFactory implements IPresenterFactory
{
    public Object createPresenter(String type, IView view)
    {
        if(type.equals(Global.HomeActivity))
        {
            IHomeView homeView = (IHomeView) view;
            return new HomePresenter(homeView, new AuthHandler());
        }
        else if (type.equals(Global.ProfileActivity))
        {
            IProfileView profileView = (IProfileView) view;
            return new ProfilePresenter(profileView, new AuthHandler());
        }
        return null;
    }


}
