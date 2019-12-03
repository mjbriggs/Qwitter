package com.michael.qwitter.Model;


import android.content.Context;

import com.michael.qwitter.Model.ModelInterfaces.IAttachment;

import java.io.File;

public class Video implements IAttachment
{
    private String mFilePath;

    public Video(){}

    public Video(String url)
    {
        this.mFilePath = url;
    }

    @Override
    public String format()
    {
        return "video";
    }

    @Override
    public String getFileName(String type)
    {
        return null;
    }

    @Override
    public String getFilePath()
    {
        return this.mFilePath;
    }

    @Override
    public IAttachment clone()
    {
        return null;
    }

    @Override
    public File getFile(Context context, String type)
    {
        return null;
    }

    @Override
    public void setFilePath(String path)
    {
        this.mFilePath = path;
    }
}
