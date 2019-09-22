package com.michael.qwitter.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.LoginPresenter;
import com.michael.qwitter.Presenter.LoginPresenterInterface;
import com.michael.qwitter.R;

public class LoginActivity extends AppCompatActivity
{
    private Button mLoginButton;
    private EditText mUserField;
    private EditText mPasswordField;
    private LoginPresenterInterface mLoginPresenter;

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
                String userAlias = mUserField.getText().toString();
                String password = mPasswordField.getText().toString();
                mLoginPresenter.addUser(userAlias, password);

                CharSequence toastMessage = userAlias + " has logged in";

                Toast toast = Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT);
                toast.show();

                mUserField.setText("");
                mPasswordField.setText("");

                finish();
            }
        });

    }
}
