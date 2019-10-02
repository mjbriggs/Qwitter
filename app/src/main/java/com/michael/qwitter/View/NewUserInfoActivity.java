package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private Bitmap mBitmap;

    private static final int REQUEST_PHOTO= 2;

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

                try
                {
                    dispatchTakePictureIntent();

                    Toast toast = Toast.makeText(NewUserInfoActivity.this, "Taking photo", Toast.LENGTH_SHORT);
                    toast.show();

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast toast = Toast.makeText(NewUserInfoActivity.this, "Cannot take photo with device", Toast.LENGTH_SHORT);
                    toast.show();
                }
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
                    try
                    {
                        mNewUserInfoPresenter.updateUserInfo(mUserAlias, mFirstName, mLastName);
                        Toast toast = Toast.makeText(NewUserInfoActivity.this, "Creating new user " + mFirstName + " " + mLastName, Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = new Intent(NewUserInfoActivity.this, HomeActivity.class);
                        intent.putExtra("USER_NAME", mUserAlias);
                        startActivity(intent);
                        finish();
                    }
                    catch (NullPointerException ex)
                    {
                        Toast toast = Toast.makeText(NewUserInfoActivity.this, "Cannot create user, check if fields are valid", Toast.LENGTH_SHORT);
                        toast.show();
                        ex.printStackTrace();
                    }
                }

            }
        });

    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_PHOTO);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();

            Bitmap sourceBitmap = (Bitmap) extras.get("data");

            Matrix matrix = new Matrix();

            //matrix.postRotate(270);  //rotation for normal phones
            matrix.postRotate(90);  //rotation for emulator


            mBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

            sourceBitmap.recycle();

            mProfilePicture.setImageBitmap(mBitmap);
        }

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
        mNewUserInfoPresenter.saveImage(mUserAlias, getApplicationContext(), mBitmap);
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
