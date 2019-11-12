package com.michael.qwitter.GatewayProxy;

import android.util.Log;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IProxy;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Utils.Global;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UpdateUserInfo implements IProxy
{
    private User mUser;
    private String mUrl;

    public UpdateUserInfo(User user)
    {
        mUser = user;
        mUrl = Global.BASE_URL + "updateinfo";
    }

    @Override
    public void execute()
    {
        try
        {
            JSONObject obj = new JSONObject();
            obj.put("username", mUser.getUserAlias());
            obj.put("firstName",mUser.getFirstName());
            obj.put("lastName",  mUser.getLastName());
            obj.put("email", mUser.getUserEmail());
            obj.put("profilePicture", mUser.getProfilePicture().getImagePath());

//            Log.i(Global.INFO, "Reqbody for add status" + obj.toString());
            RequestBody body = RequestBody.create(obj.toString().getBytes());

            Request request = new Request.Builder()
                    .url(mUrl)
                    .post(body)
                    .build();

            Global.client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException{
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        Object obj = new JSONTokener(response.body().string()).nextValue();

                        // typecasting obj to JSONObject
                        JSONObject jo = (JSONObject) obj;
                        String message = jo.getString("message");

                        if(message.contains("Failure! "))
                        {
                            throw new Exception(message);
                        }
                        else
                        {
                            Log.i(Global.INFO, message);
                        }

                    }
                    catch (Exception e) {
                        Log.e(Global.ERROR, e.getMessage(), e);
                    }
                    finally
                    {
                        response.body().close();
                        Global.client.connectionPool().evictAll();
//                        mAuthtoken = "";
                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, "exception in UpdateUserInfo proxy", e);
        }
    }
}
