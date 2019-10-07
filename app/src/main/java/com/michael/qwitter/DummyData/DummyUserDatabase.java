package com.michael.qwitter.DummyData;

import com.michael.qwitter.Model.User;

import java.util.Map;

public interface DummyUserDatabase
{
    User getUser(String username);
    void addUser(String username, String password);
    void addUser(User user);
    String generateAuthToken();
    boolean userExists(String username);
    boolean validateAuthToken(String username, String authToken);
    void updateUser(String username, User updatedUser);
    Map<String, User> getUsers();
}
