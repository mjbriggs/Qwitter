package com.michael.qwitter.Model;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.michael.qwitter.Model.ModelInterfaces.IAuthentication;

public class AuthHandler implements IAuthentication
{
    AWSMobileClient mClient;

    public AuthHandler()
    {
        mClient = AWSMobileClient.getInstance();
    }

    @Override
    public boolean authenticated()
    {
        return mClient.isSignedIn();
    }

    @Override
    public void login(String username, String password)
    {

    }

    @Override
    public void confirm(String username, String code)
    {

    }

    @Override
    public void signup(String username, String password, String email)
    {
//        final String lUsername = username;
//        final String lPassword = password;
//
//        final Map<String, String> attributes = new HashMap<>();
//        attributes.put("email", email);
//        AWSMobileClient.getInstance().signUp(username, password, attributes, null, new Callback<SignUpResult>() {
//            @Override
//            public void onResult(final SignUpResult signUpResult)
//            {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
//                        if (!signUpResult.getConfirmationState())
//                        {
//                            final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
//                            mRegistrationView.postToast("Confirm sign-up with: " + details.getDestination());
//                            Log.i(Global.USER_STATE, "Confirm sign-up with: " + details.getDestination());
//
//                            addUser(lUsername, lPassword);
//
//                            mRegistrationView.goTo(Global.VerifyPopUp);
//                        }
//                        else
//                        {
//                            mRegistrationView.postToast("Sign-up done.");
//
//                            addUser(lUsername, lPassword);
//
//                            final String userToken = validateUser(lUsername, lPassword);
//
//                            if(userToken.length() > 0)
//                            {
//                                mRegistrationView.goTo(Global.NewUserInfoActivity);
//                                mRegistrationView.postToast(lUsername + " is logged in");
//                            }
//                        }
//                    }
//                });
//            }
//            @Override
//            public void onError(Exception e)
//            {
//                Log.e(TAG, "Sign-up error", e);
////                            mRegistrationView.postToast(username + " " + e.getMessage());
//            }
//        });
    }
}
