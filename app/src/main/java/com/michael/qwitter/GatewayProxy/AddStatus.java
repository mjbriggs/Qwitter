package com.michael.qwitter.GatewayProxy;

import android.util.Log;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IProxy;
import com.michael.qwitter.Model.Hashtag;
import com.michael.qwitter.Model.ModelInterfaces.IAttachment;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Utils.Global;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddStatus implements IProxy
{
    private String mLoggedUser;
    private Status mNewStatus;
    private String mAuthtoken;
    private String mUrl;

    public AddStatus(String loggedUser, Status newStatus, String authToken)
    {
        mLoggedUser = loggedUser;
        mNewStatus = newStatus;
        mUrl = Global.BASE_URL + "addstatus";
        mAuthtoken = authToken;
    }

    @Override
    public void execute()
    {
        try
        {
            JSONObject obj = new JSONObject();
            String name[] = mNewStatus.getOwnerName().split(" ");
            obj.put("username", mLoggedUser);
            obj.put("date", mNewStatus.getTimestamp());
            obj.put("firstName",name[0]);
            obj.put("lastName",  name[1]);
            obj.put("statusText", mNewStatus.getText());
            List<Hashtag> tagList = mNewStatus.getHashTags();
            String[] tagStrs = new String [tagList.size()];
            String tagStr = "[";
            for (int i = 0; i < tagList.size(); i++)
            {
               tagStrs[i] = tagList.get(i).getTag();
            }
            JSONArray arr = new JSONArray(tagStrs);

            obj.put("statusTags", arr);
            IAttachment attachment = mNewStatus.getAttachment();

            if (attachment != null)
            {
                obj.put("statusAttachment", attachment.getFilePath());
                obj.put("attachmentFormat", attachment.format());
            }
            else
            {
                obj.put("statusAttachment", "none");
                obj.put("attachmentFormat", "none");
            }

            Log.i(Global.INFO, "Reqbody for add status" + obj.toString());
            RequestBody body = RequestBody.create(obj.toString().getBytes());

            Request request = new Request.Builder()
                    .url(mUrl)
                    .post(body)
                    .addHeader("auth-header", mAuthtoken)
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
                        mAuthtoken = "";
                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.e(Global.ERROR, "exception in Follow proxy", e);
        }
    }
}
