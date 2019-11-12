package com.michael.qwitter.DummyData;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Utils.Global;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
    private static void writeString(String str, OutputStream os) throws IOException
    {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
        sw.close();
    }
    public void callhttp(final String token)
    {
        Log.i(Global.INFO, "token in callhttp is " + token);
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                Gson gson = new Gson();

                Log.i(Global.INFO, "testing aws client");
//                ApiClientFactory apiClientFactory = new ApiClientFactory();

//                QwitterClient client = apiClientFactory.build(QwitterClient.class);

                try {
                    String endpoint = "https://kioe3is321.execute-api.us-west-2.amazonaws.com/dev/follow";

                    String reqData =
                    "{\n" +
                            "\"loggedUser\": \"potato2\",\n" +
                            "\"userToFollow\": \"potato3\"\n" +
                            "}";


                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"), reqData);

                    Request request = new Request.Builder()
                            .url(endpoint)
                            .addHeader("auth-header", token)
                            .post(body)
                            .build();

                    OkHttpClient client = new OkHttpClient();

                    Call call = client.newCall(request);
                    Response response = call.execute();

                    Log.i(Global.INFO, "api responded with " + response.code() + " status code");
                    Log.i(Global.INFO, "ap rsp is " + response.body().string());
//                    URL url = new URL("https://kioe3is321.execute-api.us-west-2.amazonaws.com/dev/follow");
//                    //URL url = new URL("http://localhost:8080/user/login");
//
//                    // Start constructing our HTTP request
//                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
//
//                    // Specify that we are sending an HTTP GET request
//                    http.setRequestMethod("POST");
//                    // Indicate that this request will not contain an HTTP request body
//                    http.setDoOutput(true);
//                    http.setDoInput(true);
//                    // Add an auth token to the request in the HTTP "Authorization" header
////                     http.addRequestProperty("Content-Type", "application/xml");
//                    // Specify that we would like to receive the server's response in JSON
//                    // format by putting an HTTP "Accept" header on the request (this is not
//                    // necessary because our server only returns JSON responses, but it
//                    // provides one more example of how to add a header to an HTTP request).
////                    http.addRequestProperty("auth-header", "token");
//                    http.addRequestProperty("auth-header", token);
////                    for (int i = 0; i < http.getHeaderFields().size(); i++)
////                    {
////                        if(http.getHeaderField(i) != null)
////                            Log.i(Global.INFO, http.getHeaderFieldKey(i) + " : " + http.getHeaderField(i));
////                        else
////                            Log.i(Global.INFO, "null contents at " + i + " " + http.getHeaderFieldKey(i) + " : ");
////                    }
//                    // Connect to the server and send the HTTP request
//                    http.connect();
//
//                    // By the time we get here, the HTTP response has been received from the server.
//                    // Check to make sure that the HTTP response from the server contains a 200
//                    // status code, which means "success".  Treat anything else as a failure.
//
//            String reqData =
//                    "{\n" +
//                            "\"loggedUser\": \"potato2\"\n" +
//                            "\"userToFollow\": \"potato3\"\n" +
//                            "}";
//                    Log.i(Global.INFO, reqData);
////                    String reqData = gson.toJson(loginRequest);
//
//                    // Get the output stream containing the HTTP request body
//                    OutputStream reqBody = http.getOutputStream();
//                    // Write the JSON data to the request body
//                    writeString(reqData, reqBody);
//                    // Close the request body output stream, indicating that the
//                    // request is complete
////                    OutputStreamWriter sw = new OutputStreamWriter(reqBody);
////                    sw.write(reqData);
////                    sw.flush();
////                    sw.close();
//                    reqBody.close();
//
//                    if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
//
//                        // Get the input stream containing the HTTP response body
//                        InputStream respBody = http.getInputStream();
//                        // Extract JSON data from the HTTP response body
//                        String respData = readString(respBody);
//                        // Display the JSON data returned from the server
//                        Log.d(Global.DEBUG, "respData " + respData);
//                        // System.out.println(respData);
//
//                    }
//                    else {
//                        // The HTTP response status code indicates an error
//                        // occurred, so print out the message from the HTTP response
//                        http.getContent();
//                        Log.d(Global.ERROR, "http err " + http.getResponseCode() + " "  + http.getResponseMessage());
//                    }

                }
                catch (Exception e) {
                    Log.d(Global.ERROR, "exception trying to post ", e);
                }

            }
        });
    }


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

        Status status = new Status("This has an image #very#cool", user.getUserAlias(),
            user.getFirstName() + " " + user.getLastName());
        status.setAttachment(new Image(user.getUserAlias() + "1").clone());

        if(status.getAttachment() == null)
            System.out.println("We failed to set attachment");
        else
            System.out.println("attachment is not null");

        user.addStatusToStory(status.clone());

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
