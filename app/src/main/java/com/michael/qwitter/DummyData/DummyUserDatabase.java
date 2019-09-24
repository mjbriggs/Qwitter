package com.michael.qwitter.DummyData;

import com.michael.qwitter.Model.User;

public interface DummyUserDatabase
{
    User getUser(String username);
    void addUser(String username, String password);
    void addUser(User user);
    String generateAuthToken();
    boolean validateAuthToken(String username, String authToken);
}
