package com.michael.qwitter.Model.ModelInterfaces;

import android.content.Context;

import java.io.File;

public interface IAttachment
{
    String getFileName(String type);
    File getFile(Context context, String type);
    IAttachment clone();
    //Bitmap getScaledBitmap(String path, Activity activity);
}
