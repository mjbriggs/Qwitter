package com.michael.qwitter.Utils;

import okhttp3.OkHttpClient;

public final class Global
{
    public static final String HomeActivity = "HomeActivity";
    public static final String NewUserInfoActivity = "NewUserInfoActivity";
    public static final String SignUpActivity = "SignUpActivity";
    public static final String VerifyPopUp = "VerifyPopUp";
    public static final String CreateStatusActivity = "CreateStatusActivity";
    public static final String LoginActivity = "LoginActivity";
    public static final String SearchView = "SearchView";
    public static final String ProfileActivity = "ProfileActivity";
    public static final String SearchActivity = "SearchActivity";
    public static final String IRegistrationView = "IRegistrationView";

    public static final String FEED = "FEED";
    public static final String STORY = "STORY";
    public static final String FOLLOWERS = "FOLLOWERS";
    public static final String FOLLOWING = "FOLLOWING";

    public static final int REQUEST_PHOTO = 2;
    public static final int RESULT_OK = -1;
    public static final String PROFILE_PIC = "PROFILE_PIC";
    public static final String USER_STATE = "USER_STATE";
    public static final String DEBUG = "DEBUG";
    public static final String ERROR = "ERROR";
    public static final String INFO = "INFO";

    public static final String BASE_URL = "https://kioe3is321.execute-api.us-west-2.amazonaws.com/dev/";
    public static final OkHttpClient client = new OkHttpClient();
}
