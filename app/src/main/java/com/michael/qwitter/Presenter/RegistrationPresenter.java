package com.michael.qwitter.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.michael.qwitter.DummyData.UserDatabase;
import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.IRegistrationPresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.LoginActivity;
import com.michael.qwitter.View.NewUserInfoActivity;
import com.michael.qwitter.View.SignUpActivity;
import com.michael.qwitter.View.ViewInterfaces.IRegistrationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static com.amazonaws.mobile.client.internal.oauth2.OAuth2Client.TAG;

public class RegistrationPresenter extends AsyncTask<String, Integer, String> implements IRegistrationPresenter
{
    private User mUser;
    private UserDatabase mUserDatabase;
    private boolean mUserCompleted;
    private IRegistrationView mRegistrationView;
    private IAccessor mAccessor;

    public RegistrationPresenter(IRegistrationView registrationView)
    {
        mUser = new User("", "");
        mUserDatabase = UserDatabase.getInstance();
        mUserCompleted = false;
        mRegistrationView = registrationView;
        mAccessor = new Accessor();
    }

    public RegistrationPresenter()
    {
        mUser = new User("", "");
        mUserDatabase = UserDatabase.getInstance();
        mUserCompleted = false;
        mRegistrationView = null;
        mAccessor = new Accessor();
    }

    @Override
    public void addUser(String username, String password)
    {
        System.out.println("presenter add of user");
        mUser.setUserAlias(username);
        mUser.setPassword(password);
        mUser.setAuthToken(mUserDatabase.generateAuthToken());
//        mUserDatabase.addUser(mUser);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mAccessor.updateUserInfo(mUser);
            }
        }).start();
    }

    @Override
    public String validateUser(String username, String password)
    {
        if(mUserDatabase.userExists(username))
        {
            User potentialUser = mUserDatabase.getUser(username);
            if(potentialUser.getPassword().equals(password))
            {
                potentialUser.setAuthToken(mUserDatabase.generateAuthToken());
                mUserDatabase.updateUser(username, potentialUser);
                return potentialUser.getAuthToken();
            }
        }

        return "";
    }

    @Override
    public void updateUserInfo(String username, String firstName, String lastName) //TODO pass in attachment arg as well
    {
//        if(mProfilePicture == null)
//        {
//            throw new NullPointerException("Profile picture must be saved before user info can be updated");
//        }

        User user = new User(username, "");
        user.setFirstName(firstName);
        user.setLastName(lastName);

        final User fUser = user;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mAccessor.updateUserInfo(fUser);
            }
        }).start();
//        user.setProfilePicture(mProfilePicture);
//        mUserDatabase.updateUser(username, user);
////        mProfilePicture.setImagePath(mProfilePicturePath);
//        System.out.println("Updated user info \n" + mUserDatabase.getUser(username).toString());
    }

    @Override
    public void updateUserInfo(String username, String firstName, String lastName, String email, Bitmap profilePicture)
    {
        User user = new User(username, "");
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserEmail(email);
        Image img = new Image(username);
        img.setBitMap(profilePicture);
        user.setProfilePicture(img);

        final User fUser = user;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mAccessor.updateUserInfo(fUser);
            }
        }).start();
    }

    @Override
    public boolean checkUserCompleted(String username)
    {
        User user = mAccessor.getUserInfo(username);

        System.out.println(user.toString());

        mUserCompleted = user.getFirstName() != null && user.getFirstName().length() > 0;
        mUserCompleted &= user.getLastName() != null && user.getLastName().length() > 0;

        return mUserCompleted;
    }

    @Override
    public boolean isUserCompleted()
    {
        return mUserCompleted;
    }
    @Override
    public void saveImage(String username, Context context, Bitmap bitmap)
    {
//        mProfilePicture = new Image(username);
//        mProfilePicturePath = context.getFilesDir().getPath() + "\'"+ mProfilePicture.getFileName("profile_picture");
//        new FileFromBitmap(bitmap, context.getApplicationContext(), mProfilePicturePath).execute();
    }

    @Override
    public void login()
    {
        if(mRegistrationView.getClass().equals(LoginActivity.class))
        {
            ArrayList<String> fields = mRegistrationView.grabTextFields();
            assert fields.size() == 2;

            final String username = fields.get(0);
            final String password = fields.get(1);
            if(username != null && password != null
            && username.length() > 0 && password.length() > 0)
            {
                AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>()
                        {
                            @Override
                            public void onResult(final SignInResult signInResult)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
                                        switch (signInResult.getSignInState())
                                        {
                                            case DONE:

                                                new Thread(new Runnable()
                                                {
                                                    @Override
                                                    public void run()
                                                    {
                                                        final User loggedUser = mAccessor.getUserInfo(username);

                                                        runOnUiThread(new Runnable()
                                                        {
                                                            @Override
                                                            public void run()
                                                            {
                                                                if(loggedUser.getFirstName() == null ||
                                                                loggedUser.getLastName() == null)
                                                                {
                                                                    mRegistrationView.postToast("Unsuccessful login attempt");
                                                                }
                                                                else
                                                                {
                                                                    if(loggedUser.getFirstName().length() == 0 ||
                                                                            loggedUser.getLastName().length() == 0) //will also check profile picture
                                                                    {
                                                                        mRegistrationView.goTo(Global.NewUserInfoActivity);
                                                                    }
                                                                    else
                                                                    {
                                                                        mRegistrationView.goTo(Global.HomeActivity);
                                                                    }
                                                                }

                                                            }
                                                        });

                                                    }
                                                }).start();

                                                break;
                                            default:
                                                mRegistrationView.postToast("Unsupported sign-in confirmation: " + signInResult.getSignInState());
                                                break;
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(Exception e)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        mRegistrationView.postToast("Invalid username and/or password");
                                    }
                                });
                                //mRegistrationView.postToast("Unsupported sign-in confirmation for " + username);
                                Log.e(Global.USER_STATE, e.getMessage(), e);
                            }
                        });
            }
            else
            {
                mRegistrationView.postToast("Invalid username and/or password");
            }
        }

    }


    @Override
    public void signup()
    {
        if (mRegistrationView.getClass().equals(SignUpActivity.class))
        {
            ArrayList<String> fields = mRegistrationView.grabTextFields();
            assert fields.size() > 3;

            final String email = fields.get(0);
            final String username = fields.get(1);
            final String password = fields.get(2);

            if (email != null && username != null && password != null
                    && email.length() > 0 && username.length() > 0 && password.length() > 7
                    && email.contains("@") && email.contains("."))
            {
                final Map<String, String> attributes = new HashMap<>();
                attributes.put("email", email);
                AWSMobileClient.getInstance().signUp(username, password, attributes, null, new Callback<SignUpResult>() {
                    @Override
                    public void onResult(final SignUpResult signUpResult)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                                if (!signUpResult.getConfirmationState())
                                {
                                    final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
                                    mRegistrationView.postToast("Confirm sign-up with: " + details.getDestination());
                                    Log.i(Global.USER_STATE, "Confirm sign-up with: " + details.getDestination());

//                                    addUser(username, password);

                                    mRegistrationView.goTo(Global.VerifyPopUp);

                                }
                                else
                                {
                                    mRegistrationView.postToast("Sign-up done.");

//                                    addUser(username, password);

                                    mRegistrationView.goTo(Global.NewUserInfoActivity);
                                    mRegistrationView.postToast(username + " is logged in");


                                }
                            }
                        });
                    }
                    @Override
                    public void onError(Exception e)
                    {
                        Log.e(TAG, "Sign-up error", e);
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (email == null || email.length() == 0 || !email.contains("@") || !email.contains("."))
                                    mRegistrationView.postToast("invalid email");
                                else
                                    mRegistrationView.postToast("Unable to sign up, try again later");
                            }
                        });
                    }
                });
            }
            else
            {
                StringBuilder error = new StringBuilder();
                if (email == null || email.length() == 0 || !email.contains("@") || !email.contains("."))
                    mRegistrationView.postToast("invalid email");
                else if(username == null || username.length() == 0)
                    mRegistrationView.postToast("invalid username");
                else
                    mRegistrationView.postToast("invalid password");

                Log.e(Global.DEBUG, "email = " + email);
                Log.e(Global.DEBUG, "username = " + username);
                Log.e(Global.DEBUG, "password = " + password);
            }

        }
    }

    @Override
    public void verify()
    {
        if (mRegistrationView.getClass().equals(SignUpActivity.class))
        {
            ArrayList<String> fields = mRegistrationView.grabTextFields();
            assert fields.size() > 3;

            final String username = fields.get(1);
            final String password = fields.get(2);
            final String code = fields.get(3);

            if (username != null && code != null && password != null
                    && username.length() > 0 && code.length() > 0 && password.length() > 0)
            {
                AWSMobileClient.getInstance().confirmSignUp(username, code, new Callback<SignUpResult>()
                {
                    @Override
                    public void onResult(final SignUpResult signUpResult)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                                if (!signUpResult.getConfirmationState())
                                {
                                    final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
                                    mRegistrationView.postToast("Confirm sign-up with: " + details.getDestination());
                                }
                                else
                                {
                                    mRegistrationView.postToast("Sign-up done.");

                                    mRegistrationView.goTo(Global.NewUserInfoActivity);

                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mRegistrationView.postToast(username + " verification has failed, check fields");
                            }
                        });
                        Log.e(TAG, "Confirm sign-up error", e);
                    }
                });
            }
            else
            {
                mRegistrationView.postToast(username + " verification has failed, check fields");
                Log.e(Global.DEBUG, "username = " + username);
                Log.e(Global.DEBUG, "code = " + code);
            }

        }
    }

    @Override
    public void update(final String username)
    {
        if(mRegistrationView.getClass().equals(NewUserInfoActivity.class))
        {
            ArrayList<String> fields =  mRegistrationView.grabTextFields();
            assert fields.size() == 3;

            final String firstName = fields.get(0);
            final String lastName = fields.get(1);
            final String email = fields.get(2);

            final Bitmap profilePicture = mRegistrationView.grabImageField();

            if(profilePicture != null && firstName != null && lastName != null
            && firstName.length() > 0 && lastName.length() > 0 && email != null && email.length() > 0)
            {
                this.updateUserInfo(username, firstName, lastName, email, profilePicture);
                mRegistrationView.goTo(Global.LoginActivity);
            }
            else
            {
                if (profilePicture == null)
                    mRegistrationView.postToast("Please upload a profile picture");
                else if(firstName == null || firstName.length() == 0)
                    mRegistrationView.postToast("Please enter first name");
                else if(lastName == null || lastName.length() == 0)
                    mRegistrationView.postToast("Please enter last name");
                else
                {
                    final User user = mAccessor.getUserInfo(username);
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                final String userEmail = AWSMobileClient.getInstance().getUserAttributes().get("email");
                                if (userEmail != null && userEmail.length() > 0)
                                {
                                    updateUserInfo(username, firstName, lastName, userEmail, profilePicture);
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            mRegistrationView.goTo(Global.LoginActivity);
                                        }
                                    });
                                }
                                else
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            Log.e(Global.ERROR, username + " is not validated, email is " + userEmail);
                                            mRegistrationView.postToast("you have failed to validate your account");
                                        }
                                    });
                            }
                            catch (Exception e)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        mRegistrationView.postToast("account validation failure");
                                    }
                                });
                                Log.e(Global.ERROR, "Unable to get valid email", e);
                            }
                        }
                    }).start();


                }
            }
        }
    }

    @Override
    public void load(String activity)
    {
        if(mRegistrationView != null)
        {
            mRegistrationView.clearFields();
            mRegistrationView.goTo(activity);
        }
    }

    @Override
    public void savePicture(int requestCode, int responseCode, Intent data)
    {
        if (requestCode == Global.REQUEST_PHOTO && responseCode == Global.RESULT_OK)
        {
            Bundle extras = data.getExtras();

            Bitmap sourceBitmap = (Bitmap) extras.get("data");

            Matrix matrix = new Matrix();

            //matrix.postRotate(270);  //rotation for normal phones
            matrix.postRotate(90);  //rotation for emulator


            Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

            sourceBitmap.recycle();

            mRegistrationView.updateField(Global.PROFILE_PIC, bitmap);
        }

    }

    @Override
    protected String doInBackground(String... strings)
    {
        String type = strings[0];
        String username = strings[1];
        String email = "";
        String firstName = "";
        String lastName = "";
        if(strings.length > 2)
        {
            email = strings[2];
            firstName = strings[3];
            lastName = strings[4];
        }

        if(type.equalsIgnoreCase("login"))
        {
            User loggedUser = mAccessor.getUserInfo(username);

            if(loggedUser.getFirstName().length() == 0 ||
            loggedUser.getLastName().length() == 0) //will also check profile picture
            {
                mRegistrationView.goTo(Global.NewUserInfoActivity);
            }
            else
            {
                mRegistrationView.goTo(Global.HomeActivity);
            }
        }
        return null;
    }
}
