package com.michael.qwitter.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.michael.qwitter.DummyData.UserDatabase;
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

public class RegistrationPresenter implements IRegistrationPresenter
{
    private User mUser;
    private UserDatabase mUserDatabase;
    private boolean mUserCompleted;
    private IRegistrationView mRegistrationView;

    public RegistrationPresenter(IRegistrationView registrationView)
    {
        mUser = new User("", "");
        mUserDatabase = UserDatabase.getInstance();
        mUserCompleted = false;
        mRegistrationView = registrationView;
    }

    public RegistrationPresenter()
    {
        mUser = new User("", "");
        mUserDatabase = UserDatabase.getInstance();
        mUserCompleted = false;
        mRegistrationView = null;
    }

    @Override
    public void addUser(String username, String password)
    {
        System.out.println("presenter add of user");
        mUser.setUserAlias(username);
        mUser.setPassword(password);
        mUser.setAuthToken(mUserDatabase.generateAuthToken());
        mUserDatabase.addUser(mUser);
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

        User user = mUserDatabase.getUser(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
//        user.setProfilePicture(mProfilePicture);
        mUserDatabase.updateUser(username, user);
//        mProfilePicture.setImagePath(mProfilePicturePath);
        System.out.println("Updated user info \n" + mUserDatabase.getUser(username).toString());
    }
    @Override
    public boolean checkUserCompleted(String username)
    {
        User user = mUserDatabase.getUser(username);

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
                                                mRegistrationView.postToast("Sign-in done.");
                                                String userToken = validateUser(username, password);

                                                if (userToken.length() == 0)
                                                {
                                                    addUser(username, password);
                                                }
                                                userToken = validateUser(username, password);
                                                mUserCompleted = false;

                                                if(userToken.length() > 0)
                                                {
                                                    mUserCompleted = checkUserCompleted(username);
                                                    mRegistrationView.clearFields();

                                                    if(mUserCompleted)
                                                    {
                                                        mRegistrationView.goTo(Global.HomeActivity);
                                                    }
                                                    else
                                                    {
                                                        mRegistrationView.goTo(Global.NewUserInfoActivity);
                                                    }

                                                    mRegistrationView.postToast(username + " has logged in");
                                                }
                                                else
                                                {
                                                    Log.e(Global.DEBUG, "something went wrong, is user in database?");
                                                }
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
                String userToken = this.validateUser(username, password);
                mUserCompleted = false;

                if (userToken.length() > 0)
                {
                    mRegistrationView.postToast(username + " already exists");
                }
                else
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

                                        addUser(username, password);

                                        mRegistrationView.goTo(Global.VerifyPopUp);
                                    }
                                    else
                                    {
                                        mRegistrationView.postToast("Sign-up done.");

                                        addUser(username, password);

                                        final String userToken = validateUser(username, password);

                                        if(userToken.length() > 0)
                                        {
                                            mRegistrationView.goTo(Global.NewUserInfoActivity);
                                            mRegistrationView.postToast(username + " is logged in");
                                        }
                                    }
                                }
                            });
                        }
                        @Override
                        public void onError(Exception e)
                        {
                            Log.e(TAG, "Sign-up error", e);
//                            mRegistrationView.postToast(username + " " + e.getMessage());
                        }
                    });
                }
            }
            else
            {
                mRegistrationView.postToast(username + " login has failed, check fields");
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

                                    final String userToken = validateUser(username, password);

                                    if(userToken.length() > 0)
                                    {
                                        mRegistrationView.goTo(Global.NewUserInfoActivity);
                                        mRegistrationView.postToast(username + " is logged in");
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e)
                    {
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
    public void update(String username)
    {
        if(mRegistrationView.getClass().equals(NewUserInfoActivity.class))
        {
            ArrayList<String> fields =  mRegistrationView.grabTextFields();
            assert fields.size() == 2;

            String firstName = fields.get(0);
            String lastName = fields.get(1);

            Bitmap profilePicture = mRegistrationView.grabImageField();

            if(profilePicture != null && firstName != null && lastName != null
            && firstName.length() > 0 && lastName.length() > 0)
            {
                this.updateUserInfo(username, firstName, lastName);
                mRegistrationView.goTo(Global.HomeActivity);
            }
            else
            {
                mRegistrationView.postToast("Unable to add info to " + username + ". Make sure all fields are valid");
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
}
