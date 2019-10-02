package com.michael.qwitter.Presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Utils.FileFromBitmap;

public class NewUserInfoPresenter extends RegistrationPresenter
{
    private UserDatabase mUserDatabase;
    private Image mProfilePicture;
    private String mProfilePicturePath;

    public NewUserInfoPresenter()
    {
        mProfilePicture = null;
        mUserDatabase = UserDatabase.getInstance();
    }

    @Override
    public void updateUserInfo(String username, String firstName, String lastName) //TODO pass in attachment arg as well
    {
        if(mProfilePicture == null)
        {
            throw new NullPointerException("Profile picture must be saved before user info can be updated");
        }

        User user = mUserDatabase.getUser(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setProfilePicture(mProfilePicture);
        mUserDatabase.updateUser(username, user);
        mProfilePicture.setImagePath(mProfilePicturePath);
        System.out.println("Updated user info \n" + mUserDatabase.getUser(username).toString());
    }

    @Override
    public void saveImage(String username, Context context, Bitmap bitmap)
    {
        mProfilePicture = new Image(username);
        mProfilePicturePath = context.getFilesDir().getPath() + "\'"+ mProfilePicture.getFileName("profile_picture");
        new FileFromBitmap(bitmap, context.getApplicationContext(), mProfilePicturePath).execute();
    }
}
