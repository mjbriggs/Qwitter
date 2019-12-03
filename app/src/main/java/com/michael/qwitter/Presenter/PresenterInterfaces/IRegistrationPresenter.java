package com.michael.qwitter.Presenter.PresenterInterfaces;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

//TODO create interface that validates user
public interface IRegistrationPresenter
{
    void addUser(String username, String password);
    String validateUser(String username, String password);
    void updateUserInfo(String username, String firstName, String lastName); //TODO pass in attachment arg as well
    void updateUserInfo(String username, String firstName, String lastName, String email, Bitmap profilePicture);

    /**
     * has nothing to do with user authentication, merely determines if user has a name, email and photo associated with them
     * */
    boolean checkUserCompleted(String username);
    boolean isUserCompleted();
    void saveImage(String username, Context context, Bitmap bitmap);
    void login();
    void verify();
    void signup();
    void update(String username);
    void load(String activity);
    void savePicture(int requestCode, int responseCode, Intent data);
}
