package com.michael.qwitter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileFromBitmap extends AsyncTask<Void, Integer, String>
{

    File mFile;
    String mFileURI;
    Context mContext;
    Bitmap mBitmap;

    public FileFromBitmap(Bitmap bitmap, Context context, String fileURI)
    {
        this.mBitmap = bitmap;
        this.mContext= context;
        this.mFileURI = fileURI;
    }

    public String getFileURI()
    {
        return mFileURI;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        // before executing doInBackground
        // update your UI
        // exp; make progressbar visible
    }

    @Override
    protected String doInBackground(Void... params)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        mFile = new File(mFileURI);
        try
        {
            FileOutputStream fo = new FileOutputStream(mFile);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        // back to main thread after finishing doInBackground
        // update your UI or take action after
        // exp; make progressbar gone
    }
}
