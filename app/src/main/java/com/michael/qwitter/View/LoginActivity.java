package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.PresenterInterfaces.IRegistrationPresenter;
import com.michael.qwitter.Presenter.RegistrationPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IRegistrationView;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements IRegistrationView
{
    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mUserField;
    private EditText mPasswordField;
    private IRegistrationPresenter mLoginPresenter;
    private String mUserAlias;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginPresenter = new RegistrationPresenter(this);


        mUserField = findViewById(R.id.user_field);
        mPasswordField = findViewById(R.id.password_field);

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mLoginPresenter.login();
            }
        });

        mSignUpButton = findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mLoginPresenter.load(Global.SignUpActivity);
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
    public Bitmap grabImageField()
    {
        return null;
    }

    @Override
    public ArrayList<String> grabTextFields()
    {
        mUserAlias = mUserField.getText().toString();
        mPassword = mPasswordField.getText().toString();

        ArrayList<String> fields = new ArrayList<>();
        fields.add(mUserAlias);
        fields.add(mPassword);

        return fields;
    }

    @Override
    public void goTo(String view)
    {
        if(view.equals(Global.HomeActivity))
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("USER_NAME", mUserAlias);
            intent.putExtra("PROFILE", false);
            startActivity(intent);
        }
        else if(view.equals(Global.NewUserInfoActivity))
        {
            Intent intent = new Intent(LoginActivity.this, NewUserInfoActivity.class);
            intent.putExtra("USER_NAME", mUserAlias);
            startActivity(intent);
        }
        else if(view.equals(Global.SignUpActivity))
        {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void postToast(String message)
    {
        Toast toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void updateField(String field, Object object)
    {

    }

}
