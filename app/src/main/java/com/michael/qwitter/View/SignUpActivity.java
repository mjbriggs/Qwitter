package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.PresenterFactory.ACPresenterFactory;
import com.michael.qwitter.Presenter.PresenterInterfaces.IRegistrationPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IRegistrationView;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements IRegistrationView
{

    private Button mSignUpButton;
    private EditText mEmailField;
    private EditText mUserField;
    private EditText mPasswordField;
    private IRegistrationPresenter mSignUpPresenter;
    private String mEmail;
    private String mUserAlias;
    private String mPassword;
    private PopupWindow mVerifyWindow;
    private String mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailField = findViewById(R.id.email_field);
        mEmailField.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus && mEmailField.getText().toString().contains("@") && mEmailField.getText().toString().contains("."))
                {
                    mEmailField.getBackground().setColorFilter(getColor(R.color.colorBlue),
                            PorterDuff.Mode.SRC_ATOP);
                }
                else if(hasFocus && (!mEmailField.getText().toString().contains("@") || !mEmailField.getText().toString().contains(".")))
                {
                    mEmailField.getBackground().setColorFilter(Color.RED,
                            PorterDuff.Mode.SRC_ATOP);
                }
                else
                {
                    mEmailField.getBackground().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        mEmailField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                mEmailField.getBackground().setColorFilter(Color.RED,
                        PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().contains("@") && s.toString().contains("."))
                {
                    mEmailField.getBackground().setColorFilter(getColor(R.color.colorBlue),
                            PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

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

        mSignUpPresenter = (IRegistrationPresenter)
                ACPresenterFactory.getInstance().createPresenter(Global.IRegistrationView, this);
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
        mCode = "";
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
        fields.add(mCode);

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
            intent.putExtra("EMAIL", mEmail);
            startActivity(intent);
            finish();
        }
        else if (view.equals(Global.VerifyPopUp))
        {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = inflater.inflate(R.layout.verification_pop_up, null);

            // create the popup window
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels / 2;
            int height = displayMetrics.heightPixels / 4;
            boolean focusable = true; // lets taps outside the popup also dismiss it

            mVerifyWindow = new PopupWindow(popupView, width, height, focusable);

            mVerifyWindow.showAtLocation(new View(SignUpActivity.this), Gravity.CENTER_VERTICAL, 0, 0);

            final EditText codeField = popupView.findViewById(R.id.code_field);
            Button verifyButton = popupView.findViewById(R.id.verify_button);
            verifyButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mCode = codeField.getText().toString();
                    mSignUpPresenter.verify();
                }
            });
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
