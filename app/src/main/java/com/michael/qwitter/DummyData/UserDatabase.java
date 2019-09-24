package com.michael.qwitter.DummyData;

import com.michael.qwitter.Model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserDatabase implements DummyUserDatabase
{
    //create an object of SingleObject
    private static UserDatabase instance = new UserDatabase();

    //Get the only object available
    public static UserDatabase getInstance(){
        return instance;
    }

    Map<String, User> mUsers;
    final String ALPHABET = "0123456789ABCDEabcde";
    final int ALPHABET_LENGTH = ALPHABET.length();

    private  UserDatabase()
    {
        mUsers = new HashMap<>();
    }

    public boolean userExists(String username)
    {
        System.out.println("Checking if user " + username + " exists");
        for (String user : mUsers.keySet())
        {
            System.out.println(user);
            if(user.equalsIgnoreCase(username))
            {
                System.out.println(username + " exists");
                return true;
            }
        }
        System.out.println(username + " does not exist");
        return false;
    }

    @Override
    public User getUser(String username)
    {
        return mUsers.get(username);
    }

    @Override
    public void addUser(String username, String password)
    {
        if(!userExists(username))
        {
            mUsers.put(username, new User(username, password));
        }
    }

    public  void updateUser(String username, User updatedUser)
    {
        mUsers.put(username, updatedUser);
    }

    @Override
    public  void addUser(User user)
    {
        assert user != null;
        if(!userExists(user.getUserAlias()))
        {
            mUsers.put(user.getUserAlias(), user);
        }
    }
    @Override
    public boolean validateAuthToken(String username, String authToken)
    {
        User user = mUsers.get(username);
        if(user != null)
        {
            if(user.getAuthToken().equals(authToken))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateAuthToken()
    {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 15; i++) {
            sb.append(ALPHABET.charAt(r.nextInt(ALPHABET_LENGTH)));
        }
        return sb.toString();
    }
}
