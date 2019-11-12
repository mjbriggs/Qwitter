package com.michael.qwitter.GatewayProxy;

import android.util.Log;

import com.michael.qwitter.GatewayProxy.ProxyInterfaces.IGetStatusList;
import com.michael.qwitter.Model.Hashtag;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.ModelInterfaces.IAttachment;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Utils.Global;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetStatusList implements IGetStatusList
{
    private List<Status> mStatuses;
    private List<Hashtag> mHashTags;
    private Status mTmpStatus;
    private DateFormat mDateFormat;
    private String mUrl;
    private String mType;

    public GetStatusList(String username, String feedType, String lastKey, String pagesize)
    {
        mType = feedType.toLowerCase();
        mDateFormat = new SimpleDateFormat("dd:MM:yyyy");
        mUrl = Global.BASE_URL + "users/" + username + "/" + mType + "?lastkey=" + lastKey + "&pagesize=" + pagesize;
        mStatuses = null;
        mHashTags = new ArrayList<>();
    }
    public GetStatusList(String username, String feedType, String lastKey, String pagesize, String url)
    {
        this(username, feedType, lastKey, pagesize);
        mUrl = url;
    }

    @Override
    public void execute()
    {
        try
        {
            Request request = new Request.Builder()
                    .url(mUrl)
                    .build();

//            final OkHttpClient client = new OkHttpClient();
            Log.i(Global.INFO, "get page req url is " + mUrl);

            Global.client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException{
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        List<Status> statuses = new ArrayList<>();

                        Log.i(Global.INFO, "api responded with " + response.code() + " status code");
                        Log.i(Global.INFO, "url is " + mUrl);

                        Object obj = new JSONTokener(response.body().string()).nextValue();

                        // typecasting obj to JSONObject
                        JSONObject jo = (JSONObject) obj;
                        JSONArray jArr = jo.getJSONArray(mType);

                        int jLen = jArr.length();
                        Log.i(Global.INFO, mType + " list is of length " + jLen);


                        for (int i = 0; i < jLen; i++)
                        {
                            JSONObject myObj = jArr.getJSONObject(i);
                            mTmpStatus = new Status(myObj.getString("statusText"));
                            mTmpStatus.setTimePosted(mDateFormat.parse(myObj.getString("date")));
                            mTmpStatus.setOwner(myObj.getString("username"));
                            IAttachment attachment = new Image(mTmpStatus.getOwner());
                            attachment.setFilePath(myObj.getString("attachment")
                                    .replace("[","")
                                    .replace("]",""));
                            mTmpStatus.setAttachment(attachment);
                            JSONArray tags = myObj.getJSONArray("tags");
                            mHashTags.clear();
                            for(int j = 0; j < tags.length(); j++)
                            {
                                mHashTags.add(new Hashtag(tags.getString(j)));
                            }
                            mTmpStatus.setHashTags(mHashTags);
                            statuses.add(mTmpStatus);
                        }

                       mStatuses = statuses;

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
    public List<Status> getStatusList()
    {
        return mStatuses;
    }
}
