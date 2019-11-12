package com.michael.qwitter.Presenter;

import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.ModelInterfaces.IAuthentication;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.IHomePresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IHomeView;

public class HomePresenter implements IHomePresenter
{
    private UserDatabase mUserDatabase;
    private User mHomeUser;
    private IHomeView mHomeView;
    private IAuthentication mAuthenticationHandler;
    private IAccessor mAccessor;

    //TODO talk to adapter to update status list, for now I'll just print the list of statuses
    public HomePresenter()
    {
        mUserDatabase = UserDatabase.getInstance();
        mHomeUser = null;
        mAccessor = new Accessor();
    }

    public HomePresenter(IHomeView view)
    {
        this();
        mHomeView = view;
    }

    public HomePresenter(IHomeView view, IAuthentication authenticationHandler)
    {
        this();
        mHomeView = view;
        mAuthenticationHandler = authenticationHandler;
    }

    public boolean findLoggedInUser(String username)
    {
        Log.d(Global.DEBUG, "username in findLoggerInUser is " + username);
        //TODO update to check with cognito for validation
        setHomeUser(mUserDatabase.getUser(username));
        if(!mUserDatabase.validateAuthToken(mHomeUser.getUserAlias(), mHomeUser.getAuthToken()))
        {
            return false;
        }

        return mHomeUser != null;
    }

    public User getHomeUser()
    {
        return mHomeUser;
    }

    public void setHomeUser(User mHomeUser)
    {
        this.mHomeUser = mHomeUser;
    }

    public void logoutUser()
    {
        if(mHomeUser != null)
        {
            mHomeUser.setAuthToken("");
            mUserDatabase.updateUser(mHomeUser.getUserAlias(), mHomeUser);
            AWSMobileClient.getInstance().signOut();
        }
        else
        {
            Log.e(Global.ERROR, "home user does not exist in database");
        }

    }

    public boolean doesUserExist(String username)
    {
        User user = mAccessor.getUserInfo(username);
        return user.getUserAlias().length() > 0;
    }

    @Override
    public boolean handleMenu(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.update_profile_picture:
                mHomeView.takePhoto();
                return true;
            case R.id.logout_button:
                this.logoutUser();
                mHomeView.goTo(Global.LoginActivity);
                //TODO logout user with amplify
                return true;
            case R.id.menu_search_button:
                Log.d(Global.DEBUG, "search button id clicked, going to " + Global.SearchView);
                mHomeView.goTo(Global.SearchView);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void openView(String view)
    {
        if(view.equals(Global.CreateStatusActivity))
        {
            if(!mAuthenticationHandler.authenticated())
            {
                Log.i(Global.ERROR, mHomeView.user() + " does not have permission to create a status");
                return;
            }
        }
        mHomeView.goTo(view);
    }

    @Override
    public void handleQuery(String query)
    {
        if(query.charAt(0) == '@')
        {
            if(this.doesUserExist(query.substring(1)))
            {
                mHomeView.goTo(Global.ProfileActivity);
                mHomeView.updateField(SearchView.class.toString(), "");
            }
            else
            {
                mHomeView.postToast(query + " cannot be found");
            }
        }
        else if(query.charAt(0) == '#')
        {
            mHomeView.goTo(Global.SearchActivity);
            mHomeView.updateField(SearchView.class.toString(), "");
        }
        else
        {
            mHomeView.postToast("search format not supported");
        }

    }
}
