package com.michael.qwitter.GatewayProxy;

import android.util.Log;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetUserInfo;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Utils.Global;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetUserInfo implements IGetUserInfo
{
    private final String info = "/info";
    private final String username;
    private User user;

    public GetUserInfo(String username)
    {
        this.username = username;
        user = new User("","");
        user.setFirstName(null);
        user.setLastName(null);
        user.setProfilePicture(null);
    }

    @Override
    public void execute()
    {
        try
        {
            String url = Global.BASE_URL + "users/" + username + info;
            Log.i(Global.INFO, "url is " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

//            OkHttpClient client = new OkHttpClient();

            Global.client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException{
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//                        Log.i(Global.INFO, "api responded with " + response.code() + " status code");

                        Object obj = new JSONTokener(response.body().string()).nextValue();

                        // typecasting obj to JSONObject
                        JSONObject jo = (JSONObject) obj;

                        String username = jo.getString("username");
                        String firstName = jo.getString("firstName");
                        String lastName =  jo.getString("lastName");
                        String email = jo.getString("email");
                        String profilePicture = jo.getString("profilePicture");

//            String resPath = "/Users/michaelbriggs/AndroidStudioProjects/Qwitter/app/src/main/res/";
                        user.setUserAlias(username);
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        user.setUserEmail(email);
//            user.setProfilePicture(profilePicture);
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
//            user = gson.fromJson( response.body().string(), User.class);
        }
        catch (Exception e)
        {
            Log.d(Global.ERROR, "exception trying to post ", e);
        }
    }

    @Override
    public User getUser()
    {
        return this.user;
    }
}
