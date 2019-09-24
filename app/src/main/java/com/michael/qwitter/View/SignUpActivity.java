package com.michael.qwitter.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.LoginPresenter;
import com.michael.qwitter.Presenter.LoginPresenterInterface;
import com.michael.qwitter.R;
import com.michael.qwitter.View.ViewInterfaces.UserRegistration;

public class SignUpActivity extends AppCompatActivity implements UserRegistration
{

    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mUserField;
    private EditText mPasswordField;
    private LoginPresenterInterface mLoginPresenter;
    private String mUserAlias;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserField = findViewById(R.id.user_field);

        mPasswordField = findViewById(R.id.password_field);

        mLoginPresenter = new LoginPresenter();

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setVisibility(View.GONE);

        mSignUpButton = findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                grabFields();
                if(validFields())
                {
                    if(mLoginPresenter.validateUser(mUserAlias, mPassword).length() > 0)
                    {
                        CharSequence toastMessage = mUserAlias + " already exists";

                        Toast toast = Toast.makeText(SignUpActivity.this, toastMessage, Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    mLoginPresenter.addUser(mUserAlias, mPassword);

                    mUserField.setText("");
                    mPasswordField.setText("");
                    String userToken = mLoginPresenter.validateUser(mUserAlias, mPassword);

                    if(userToken.length() > 0)
                    {
                        CharSequence toastMessage = mUserAlias + " has logged in";

                        Toast toast = Toast.makeText(SignUpActivity.this, toastMessage, Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = new Intent(SignUpActivity.this, RecyclerActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        CharSequence toastMessage = mUserAlias + " login has failed";

                        Toast toast = Toast.makeText(SignUpActivity.this, toastMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }
        });

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
