package com.michael.qwitter.View.ViewInterfaces;

public interface IView
{
    void postToast(String message);
    void goTo(String view);
    void updateField(String field, Object object);
}
