package com.michael.qwitter.DummyData;

import com.michael.qwitter.Model.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserDatabaseTest
{
    private DummyUserDatabase mUserDatabase;
    private String mTestString;

    @Before
    public void setUp()
    {
        mTestString = "Test";
        mUserDatabase = new UserDatabase();
        mUserDatabase.addUser(mTestString, mTestString);
    }
    @Test
    public void generateAuthtokenTest()
    {
        User user = mUserDatabase.getUser(mTestString);
        user.setAuthToken(mUserDatabase.generateAuthToken());
        System.out.println(user.getAuthToken());
        Assert.assertNotEquals(user.getAuthToken(), mUserDatabase.generateAuthToken());
        Assert.assertNotEquals(user.getAuthToken(), mUserDatabase.generateAuthToken());
    }
}
