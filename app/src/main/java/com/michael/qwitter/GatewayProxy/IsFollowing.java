package com.michael.qwitter.GatewayProxy;

import android.util.Log;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IIsFollowing;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Utils.Global;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class IsFollowing implements IIsFollowing
{
    private Boolean mIsFollowing;
    private String mLoggedUser;
    private String mFollowedUser;
    private String mUrl;

    public IsFollowing(String loggedUser, String followedUser)
    {
        mIsFollowing = null;
        mLoggedUser = loggedUser;
        mFollowedUser = followedUser;

        mUrl = Global.BASE_URL + "users/" + loggedUser + "/following?user=" + followedUser;
    }

    @Override
    public void execute()
    {
        try
        {
            Request request = new Request.Builder()
                    .url(mUrl)
                    .build();

            Global.client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException{
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        List<User> users = new ArrayList<>();

//                        Log.i(Global.INFO, "api responded with " + response.code() + " status code");

                        Object obj = new JSONTokener(response.body().string()).nextValue();

                        // typecasting obj to JSONObject
                        JSONObject jo = (JSONObject) obj;
                        mIsFollowing = jo.getBoolean("follows");
                    }
                    catch (Exception e) {
                        Log.e(Global.ERROR, e.getMessage(), e);
                    }
                    finally
                    {
                        response.body().close();
                        Global.client.connectionPool().evictAll();
                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, "exception in IsFollowing proxy", e);
        }
    }

    @Override
    public Boolean isFollowing()
    {
        return mIsFollowing;
    }
}
