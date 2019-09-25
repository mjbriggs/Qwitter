package com.michael.qwitter.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.NewUserInfoPresenter;
import com.michael.qwitter.Presenter.RegistrationInterface;
import com.michael.qwitter.R;
import com.michael.qwitter.View.ViewInterfaces.UserRegistration;

public class NewUserInfoActivity extends AppCompatActivity implements UserRegistration
{

    private RegistrationInterface mNewUserInfoPresenter;
    private ImageView mProfilePicture;
    private TextView mEditPicture;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private String mFirstName;
    private String mLastName;
    private Button mCreateButton;
    private String mUserAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_info);

        mUserAlias = getIntent().getExtras().getString("USER_NAME");

        mNewUserInfoPresenter = new NewUserInfoPresenter();

        mProfilePicture = findViewById(R.id.create_profile_picture);

        mEditPicture = findViewById(R.id.edit_picture_view);
        mEditPicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast toast = Toast.makeText(NewUserInfoActivity.this, "will allow user to edit picture", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        mFirstNameField = findViewById(R.id.first_name_field);

        mLastNameField = findViewById(R.id.last_name_field);

        mCreateButton = findViewById(R.id.create_profile_button);
        mCreateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                grabFields();
                if(validFields())
                {
                    mNewUserInfoPresenter.updateUserInfo(mUserAlias, mFirstName, mLastName);

                    Toast toast = Toast.makeText(NewUserInfoActivity.this, "Creating new user " + mFirstName + " " + mLastName, Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(NewUserInfoActivity.this, RecyclerActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    @Override
    public void clearFields()
    {
        mFirstNameField.setText("");
        mLastNameField.setText("");
        mProfilePicture.setImageResource(R.color.colorBlack);
    }

    @Override
    public void grabFields()
    {
        mFirstName = mFirstNameField.getText().toString();
        mLastName = mLastNameField.getText().toString();
        //TODO set attachment to image field contents
    }

    @Override
    public boolean validFields()
    {
        if(mFirstName == null || mLastName == null)
        {
            return false;
        }

        if(mFirstName.length() == 0 || mLastName.length() == 0)
        {
            return false;
        }

        //TODO check for profile picture

        return true;
    }
}
