package com.michael.qwitter.View.ViewInterfaces;

import android.graphics.Bitmap;

import java.util.ArrayList;

public interface UserRegistration
{
    ArrayList<String> grabTextFields();
    Bitmap grabImageField();
    void clearFields();
    void goTo(String view);
    void postToast(String message);
    void updateField(String field, Object object);
}
