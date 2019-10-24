package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.RegistrationInterface;
import com.michael.qwitter.Presenter.RegistrationPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.UserRegistration;

import java.util.ArrayList;

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

        mSignUpPresenter = new RegistrationPresenter(this);

        mSignUpButton = findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mSignUpPresenter.signup();
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
    public ArrayList<String> grabTextFields()
    {
        mEmail = mEmailField.getText().toString();
        mUserAlias = mUserField.getText().toString();
        mPassword = mPasswordField.getText().toString();

        ArrayList<String> fields = new ArrayList<>();
        fields.add(mEmail);
        fields.add(mUserAlias);
        fields.add(mPassword);

        return fields;
    }

    @Override
    public Bitmap grabImageField()
    {
        return null;
    }

    @Override
    public void goTo(String view)
    {
        if(view.equals(Global.NewUserInfoActivity))
        {
            Intent intent = new Intent(SignUpActivity.this, NewUserInfoActivity.class);
            intent.putExtra("USER_NAME", mUserAlias);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void postToast(String message)
    {
        Toast toast = Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void updateField(String field, Object object)
    {

    }
}
