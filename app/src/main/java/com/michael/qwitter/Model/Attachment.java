package com.michael.qwitter.Model;

import android.content.Context;

import java.io.File;

public interface Attachment
{
    String getFileName(String type);
    File getFile(Context context, String type);
    //Bitmap getScaledBitmap(String path, Activity activity);
}
