package com.michael.qwitter.Model;

import android.content.Context;

import java.io.File;

//Every image is associated with a user
public class Image implements Attachment
{
    String mUsername;

    public void setUsername(String username)
    {
        mUsername = username;
    }

    @Override
    public File getFile(Context context, String type)
    {
        File filesDir = context.getFilesDir();
        return new File (filesDir, getFileName(type));
    }

    @Override
    public String getFileName(String type)
    {
        if(type.equalsIgnoreCase("profile_picture"))
        {
            return "IMG_" + mUsername + ".jpg";
        }

        return "";
    }
}
