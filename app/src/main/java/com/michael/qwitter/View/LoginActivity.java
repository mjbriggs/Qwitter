package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.PresenterFactory.ACPresenterFactory;
import com.michael.qwitter.Presenter.PresenterInterfaces.IRegistrationPresenter;
import com.michael.qwitter.Presenter.RegistrationPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.Utils.PageTracker;
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

        PageTracker.getInstance().reinit();

        mLoginPresenter = (RegistrationPresenter)
                ACPresenterFactory.getInstance().createPresenter(Global.IRegistrationView, this);

        mUserField = findViewById(R.id.user_field);
        mUserField.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    mUserField.getBackground().setColorFilter(getColor(R.color.colorBlue),
                            PorterDuff.Mode.SRC_ATOP);
                }
                else
                {
                    mUserField.getBackground().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        mPasswordField = findViewById(R.id.password_field);
        mPasswordField.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus && mPasswordField.getText().toString().length() < 8)
                {
                    mPasswordField.getBackground().setColorFilter(Color.RED,
                            PorterDuff.Mode.SRC_ATOP);
                }
                else if (!hasFocus)
                {
                    mPasswordField.getBackground().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.SRC_ATOP);
                }
                else if(hasFocus && mPasswordField.getText().toString().length() >= 8)
                {
                    mPasswordField.getBackground().setColorFilter(getColor(R.color.colorBlue),
                            PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        mPasswordField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                mPasswordField.getBackground().setColorFilter(Color.RED,
                        PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 7)
                {
                    mPasswordField.getBackground().setColorFilter(getColor(R.color.colorBlue),
                            PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
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
            mPasswordField.getBackground().setColorFilter(Color.GRAY,
                    PorterDuff.Mode.SRC_ATOP);
            startActivity(intent);
        }
        else if(view.equals(Global.SignUpActivity))
        {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            mPasswordField.getBackground().setColorFilter(Color.GRAY,
                    PorterDuff.Mode.SRC_ATOP);
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
