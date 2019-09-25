package com.michael.qwitter.Presenter;

public interface RegistrationInterface
{
    void addUser(String username, String password);
    String validateUser(String username, String password);
    void updateUserInfo(String username, String firstName, String lastName); //TODO pass in attachment arg as well
    boolean isUserCreated(String username);
}
