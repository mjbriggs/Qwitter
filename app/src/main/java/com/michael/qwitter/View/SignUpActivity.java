package com.michael.qwitter.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.RegistrationInterface;
import com.michael.qwitter.Presenter.SignUpPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.View.ViewInterfaces.UserRegistration;

public class SignUpActivity extends AppCompatActivity implements UserRegistration
{

    private Button mSignUpButton;
    private EditText mEmailField;
    private EditText mUserField;
    private EditText mPasswordField;
    private RegistrationInterface mSignUpPresenter;
    private String mEmail;
    private String mUserAlias;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailField = findViewById(R.id.email_field);

        mUserField = findViewById(R.id.user_field);

        mPasswordField = findViewById(R.id.password_field);

        mSignUpPresenter = new SignUpPresenter();

        mSignUpButton = findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                grabFields();
                if(validFields())
                {
                    if(mSignUpPresenter.validateUser(mUserAlias, mPassword).length() > 0)
                    {
                        CharSequence toastMessage = mUserAlias + " already exists";

                        Toast toast = Toast.makeText(SignUpActivity.this, toastMessage, Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    mSignUpPresenter.addUser(mUserAlias, mPassword);

                    String userToken = mSignUpPresenter.validateUser(mUserAlias, mPassword);

                    if(userToken.length() > 0)
                    {
                        CharSequence toastMessage = mUserAlias + " has logged in";

                        clearFields();

                        Toast toast = Toast.makeText(SignUpActivity.this, toastMessage, Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = new Intent(SignUpActivity.this, NewUserInfoActivity.class);
                        intent.putExtra("USER_NAME", mUserAlias);
                        startActivity(intent);
                        finish();
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
    public void clearFields()
    {
        mUserField.setText("");
        mPasswordField.setText("");
        mEmailField.setText("");
    }

    @Override
    public void grabFields()
    {
        mEmail = mEmailField.getText().toString();
        mUserAlias = mUserField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    @Override
    public boolean validFields()
    {
        if(mUserAlias == null || mPassword == null || mEmail == null)
        {
            return false;
        }

        if(mUserAlias.length() == 0 || mPassword.length() == 0 || mEmail.length() == 0)
        {
            return false;
        }

        if(!mEmail.contains("@"))
        {
            return false;
        }

        return true;
    }
}
