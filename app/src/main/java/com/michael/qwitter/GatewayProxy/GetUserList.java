package com.michael.qwitter.GatewayProxy;

import android.util.Log;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetUserList;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Utils.Global;

import org.json.JSONArray;
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

public class GetUserList implements IGetUserList
{
    private String mUsername;
    private String mType;
    private String mUrl;
    private List<User> mUsers;
    private User mTmpUser;

    public GetUserList(String username, String listType, String lastKey, String pagesize)
    {
        mUsername = username;
        mUrl = Global.BASE_URL + "users/" + mUsername + "/" + listType + "?lastkey=" + lastKey + "&pagesize=" + 1;
        mUsers = null;

        if(listType.equalsIgnoreCase("all-followers"))
        {
            mType = "followers";
        }
        else
        {
            mType = "following";
        }
    }
    @Override
    public void execute()
    {
        try
        {
            Request request = new Request.Builder()
                    .url(mUrl)
                    .build();

//            OkHttpClient client = new OkHttpClient();

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
                        JSONArray jArr = jo.getJSONArray(mType);

                        int jLen = jArr.length();

                        for (int i = 0; i < jLen; i++)
                        {
                            JSONObject myObj = jArr.getJSONObject(i);
                            mTmpUser = new User(myObj.getString("userAlias"),"");
                            mTmpUser.setFirstName(myObj.getString("firstName"));
                            mTmpUser.setLastName(myObj.getString("lastName"));
                            Image img = new Image(mTmpUser.getUserAlias());
                            img.setFilePath(myObj.getString("profilePicture"));
                            mTmpUser.setProfilePicture(img);
                            //TODO set profile picture

                            users.add(mTmpUser);
                        }

                        mUsers = users;

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
    public List<User> getUserList()
    {
        return mUsers;
    }
}
