package com.michael.qwitter.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import java.io.File;

//Every image is associated with a user
public class Image implements Attachment
{
    String mUsername;

    public Image(String mUsername)
    {
        setUsername(mUsername);
    }
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

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // Figure out how much to scale down by
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale :
                    widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Read in and create final bitmap
        return BitmapFactory.decodeFile(path, options);
    }
}
