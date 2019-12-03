package com.michael.qwitter.Presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.ModelInterfaces.IAuthentication;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.IHomePresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IHomeView;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

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
//            mHomeUser.setAuthToken("");
//            mUserDatabase.updateUser(mHomeUser.getUserAlias(), mHomeUser);
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
//        Log.d(Global.DEBUG, "item id is " + item.getItemId());
//        Log.d(Global.DEBUG, "profile id is " + R.id.update_profile_picture);
//        Log.d(Global.DEBUG, "logout id is " + R.id.logout_button);
//        Log.d(Global.DEBUG, "search id is " + R.id.menu_search_button);

        CharSequence title = item.getTitle();
        String titleStr = title.toString();
        Log.i(Global.INFO, "title of menu item is " + titleStr);
        if (titleStr.equalsIgnoreCase("Update profile picture"))
        {
            Log.i(Global.INFO, "Trying to dispatch photo intent");
            mHomeView.takePhoto();
            return true;
        }
        else if (titleStr.equalsIgnoreCase("Logout"))
        {
            this.logoutUser();
            mHomeView.goTo(Global.LoginActivity);
            return true;
        }
        else if (titleStr.equalsIgnoreCase("Search"))
        {
            Log.d(Global.DEBUG, "search button id clicked, going to " + Global.SearchView);
            mHomeView.goTo(Global.SearchView);
            return true;
        }
        else
        {
            return false;
        }
//        switch (item.getItemId())
//        {
//            case R.id.update_profile_picture:
//                mHomeView.takePhoto();
//                return true;
//            case R.id.logout_button:
//                this.logoutUser();
//                mHomeView.goTo(Global.LoginActivity);
//                return true;
//            case R.id.menu_search_button:
//                Log.d(Global.DEBUG, "search button id clicked, going to " + Global.SearchView);
//                mHomeView.goTo(Global.SearchView);
//                return true;
//            default:
//
//        }
    }

    @Override
    public void openView(String view)
    {
        final String fView = view;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if(fView.equals(Global.CreateStatusActivity))
                {
                    if(!mAuthenticationHandler.authenticated())
                    {
                        Log.i(Global.ERROR, mHomeView.user() + " does not have permission to create a status");
                        return;
                    }
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mHomeView.goTo(fView);
                    }
                });
            }
        }).start();

    }

    @Override
    public void handleQuery(String query)
    {
        final String fQuery = query;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if(fQuery.charAt(0) == '@')
                {
                    if(doesUserExist(fQuery.substring(1)))
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mHomeView.goTo(Global.ProfileActivity);
                                mHomeView.updateField(SearchView.class.toString(), "");
                            }
                        });

                    }
                    else
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mHomeView.postToast(fQuery + " cannot be found");
                            }
                        });
                    }
                }
                else if(fQuery.charAt(0) == '#')
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mHomeView.goTo(Global.SearchActivity);
                            mHomeView.updateField(SearchView.class.toString(), "");
                        }
                    });

                }
                else
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mHomeView.postToast("search format not supported");
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void savePicture(int requestCode, int responseCode, Intent data)
    {
        if (requestCode == Global.REQUEST_PHOTO && responseCode == Global.RESULT_OK)
        {
            Bundle extras = data.getExtras();

            Bitmap sourceBitmap = (Bitmap) extras.get("data");

            Matrix matrix = new Matrix();

            //matrix.postRotate(270);  //rotation for normal phones
            matrix.postRotate(90);  //rotation for emulator


            Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

            sourceBitmap.recycle();

            if (mHomeUser == null)
            {
                mHomeView.postToast("Unable to save picture, wait and try again");
                return;
            }

            Image img = new Image(mHomeUser.getUserAlias());
            img.setBitMap(bitmap);

            mHomeUser.setProfilePicture(img);

            Log.i(Global.INFO, "updating " + mHomeUser.toString());

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    mAccessor.updateUserInfo(mHomeUser);
                }
            }).start();

//            .updateField(Global.PROFILE_PIC, bitmap);
        }
    }

    @Override
    public void setHomeUser(String username)
    {
        mHomeUser = mAccessor.getUserInfo(username);
    }
}
