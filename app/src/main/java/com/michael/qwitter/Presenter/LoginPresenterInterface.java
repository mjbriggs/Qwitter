package com.michael.qwitter.Presenter;

public interface LoginPresenterInterface
{
    void addUser(String username, String password);
    String validateUser(String username, String password);
}
