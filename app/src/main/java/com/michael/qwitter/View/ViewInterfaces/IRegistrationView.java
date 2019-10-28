package com.michael.qwitter.View.ViewInterfaces;

import android.graphics.Bitmap;

import java.util.ArrayList;

public interface IRegistrationView extends IView
{
    ArrayList<String> grabTextFields();
    Bitmap grabImageField();
    void clearFields();
}
