package com.michael.qwitter.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.LoginPresenter;
import com.michael.qwitter.Presenter.RegistrationInterface;
import com.michael.qwitter.R;
import com.michael.qwitter.View.ViewInterfaces.UserRegistration;

public class LoginActivity extends AppCompatActivity implements UserRegistration
{
    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mUserField;
    private EditText mPasswordField;
    private RegistrationInterface mLoginPresenter;
    private String mUserAlias;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginPresenter = new LoginPresenter();

        mUserField = findViewById(R.id.user_field);
        mPasswordField = findViewById(R.id.password_field);

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                grabFields();
                if(validFields())
                {
                    //creates authtoken if user exists
                    String userToken = mLoginPresenter.validateUser(mUserAlias, mPassword);

                    if(userToken.length() > 0)
                    {
                        CharSequence toastMessage = mUserAlias + " has logged in";

                        Toast toast = Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT);
                        toast.show();

                        clearFields();


                        if(mLoginPresenter.isUserCreated(mUserAlias))
                        {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("USER_NAME", mUserAlias);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(LoginActivity.this, NewUserInfoActivity.class);
                            intent.putExtra("USER_NAME", mUserAlias);
                            startActivity(intent);
                        }

                    }
                    else
                    {
                        CharSequence toastMessage = mUserAlias + " login has failed";

                        Toast toast = Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        mSignUpButton = findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clearFields();
                
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void clearFields()
    {
        mUserField.setText("");
        mPasswordField.setText("");
    }

    @Override
    public void grabFields()
    {
        mUserAlias = mUserField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    @Override
    public boolean validFields()
    {
        if(mUserAlias == null || mPassword == null)
        {
            return false;
        }

        if(mUserAlias.length() == 0 || mPassword.length() == 0)
        {
            return false;
        }

        return true;
    }

}
