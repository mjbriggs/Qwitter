package com.michael.qwitter.Model.ModelInterfaces;

import android.content.Context;

import java.io.File;

public interface IAttachment
{
    String getFileName(String type);
    File getFile(Context context, String type);
    IAttachment clone();
    void setFilePath(String path);
    String getFilePath();
    String format();
    //Bitmap getScaledBitmap(String path, Activity activity);
}
