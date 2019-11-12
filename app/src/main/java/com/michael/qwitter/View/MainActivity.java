package com.michael.qwitter.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.michael.qwitter.Presenter.PresenterFactory.ACPresenterFactory;
import com.michael.qwitter.Presenter.PresenterFactory.PresenterFactory;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;

public class MainActivity extends AppCompatActivity
{

    private boolean mSignedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        Log.i("INIT", "onResult: " + userStateDetails.getUserState());

                        if(userStateDetails.getUserState().equals(UserState.SIGNED_IN))
                        {
                            mSignedIn = true;
                        }
                        else
                        {
                             mSignedIn = false;
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );

        if(mSignedIn)
        {
            try
            {
                AWSMobileClient.getInstance().signOut();
            }
            catch (Exception e)
            {
                Log.e(Global.ERROR, e.getMessage(), e);
            }
        }
        ACPresenterFactory.setInstance(new PresenterFactory());
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}


