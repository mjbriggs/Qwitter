package com.michael.qwitter.DummyData;

import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.Status;
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

    private User dummyInitialize(User user, int num)
    {
        User returnUser = new User(user.getUserAlias(), user.getPassword());
        String alias = user.getUserAlias();
        String firstName = "us";
        String lastName = "er";
        //TODO profile picture

        if(num > 1)
        {
            firstName += num;
            lastName += num;
            returnUser.setFirstName(firstName);
            returnUser.setLastName(lastName);
            returnUser.setProfilePicture(new Image("us" + num));
        }
        else
        {
            returnUser.setFirstName(firstName);
            returnUser.setLastName(lastName);
            returnUser.setProfilePicture(new Image("us"));
        }

        String fullName = firstName + " " + lastName;
        returnUser.addStatusToFeed(new Status("status 1 " + num, alias, fullName));
        returnUser.addStatusToFeed(new Status("status 2 " + num, alias, fullName));
        returnUser.addStatusToFeed(new Status("status 3 " + num, alias, fullName));

        returnUser.addStatusToStory(new Status("status 1 " + num, alias, fullName));
        returnUser.addStatusToStory(new Status("status 2 " + num, alias, fullName));
        returnUser.addStatusToStory(new Status("status 3 " + num, alias, fullName));

        return returnUser;
    }

    private void dummyAddUser(User user)
    {
        addUser(user);
    }

    private UserDatabase()
    {
        mUsers = new HashMap<>();
        User user = new User("user", "password");
        User user2 = new User("user2", "user2");
        User user3 = new User("user3", "user3");
        User user4 = new User("user4", "user4");
        User user5 = new User("user5", "user5");

        user = new User(dummyInitialize(user, 0));
        user2 = new User(dummyInitialize(user2, 2));
        user3 = new User(dummyInitialize(user3, 3));
        user4 = new User(dummyInitialize(user4, 4));
        user5 = new User(dummyInitialize(user5, 5));

        user.addStatusToStory(new Status("https://www.javatpoint.com/android-linkify-example", user.getUserAlias(),
                user.getFirstName() + " " + user.getLastName()));
        user.addStatusToStory(new Status("#yeet", user.getUserAlias(),
                user.getFirstName() + " " + user.getLastName()));

        user.addFollower(user2);
        user2.addFollowing(user);

        user.addFollower(user2);
        user2.addFollowing(user);

        user.addFollower(user3);
        user3.addFollowing(user);

        user.addFollowing(user4);
        user4.addFollower(user);

        user.addFollowing(user5);
        user5.addFollower(user);

        dummyAddUser(user);
        dummyAddUser(user2);
        dummyAddUser(user3);
        dummyAddUser(user4);
        dummyAddUser(user5);
    }

    @Override
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

    @Override
    public void updateUser(String username, User updatedUser)
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

    @Override
    public Map<String, User> getUsers()
    {
        return mUsers;
    }
}
