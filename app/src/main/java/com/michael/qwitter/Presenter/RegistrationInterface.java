package com.michael.qwitter.Presenter;

import android.content.Context;
import android.graphics.Bitmap;

//TODO create interface that validates user
public interface RegistrationInterface
{
    void addUser(String username, String password);
    String validateUser(String username, String password);
    void updateUserInfo(String username, String firstName, String lastName); //TODO pass in attachment arg as well
    boolean isUserCreated(String username);
    void saveImage(String username, Context context, Bitmap bitmap);
}
