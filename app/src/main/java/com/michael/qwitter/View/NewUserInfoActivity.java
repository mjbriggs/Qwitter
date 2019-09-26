package com.michael.qwitter.View;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.michael.qwitter.Model.Image;
import com.michael.qwitter.Presenter.NewUserInfoPresenter;
import com.michael.qwitter.Presenter.RegistrationInterface;
import com.michael.qwitter.R;
import com.michael.qwitter.View.ViewInterfaces.UserRegistration;

import java.io.File;
import java.util.List;

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

    private File mProfilePhotoFile;
    private Image mProfilePhotoImage;
    private static final int REQUEST_PHOTO= 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_info);

        mUserAlias = getIntent().getExtras().getString("USER_NAME");

        mProfilePhotoImage = new Image(mUserAlias);
        mProfilePhotoFile = mProfilePhotoImage.getFile(NewUserInfoActivity.this, "profile_picture");

        mNewUserInfoPresenter = new NewUserInfoPresenter();

        mProfilePicture = findViewById(R.id.create_profile_picture);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        final boolean canTakePhoto = mProfilePhotoFile != null &&
                captureImage.resolveActivity(getPackageManager()) != null;

        mEditPicture = findViewById(R.id.edit_picture_view);
        mEditPicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(canTakePhoto)
                {
                    Uri uri = FileProvider.getUriForFile(NewUserInfoActivity.this,
                            "com.michael.qwitter.fileprovider",
                            mProfilePhotoFile);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    List<ResolveInfo> cameraActivities = NewUserInfoActivity.this
                            .getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo activity : cameraActivities) {
                        NewUserInfoActivity.this.grantUriPermission(activity.activityInfo.packageName,
                                uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }

                    startActivityForResult(captureImage, REQUEST_PHOTO);

                    Toast toast = Toast.makeText(NewUserInfoActivity.this, "Taking photo", Toast.LENGTH_SHORT);
                    toast.show();

                    //updateProfilePicture();
                }
                else
                {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_PHOTO)
        {
            if(mProfilePhotoFile == null || data == null || NewUserInfoActivity.this == null)
            {
                System.out.println("Camera permissions already closed");
            }
            else
            {
                try
                {
                    System.out.println("closing camera permissions");
                    Uri uri = FileProvider.getUriForFile(NewUserInfoActivity.this,
                            "com.bignerdranch.android.criminalintent.fileprovider",
                            mProfilePhotoFile);

                    NewUserInfoActivity.this.revokeUriPermission(uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    updateProfilePicture();
                }
                catch(NullPointerException ex)
                {
                    ex.printStackTrace();
                    mProfilePhotoFile = null;
                    updateProfilePicture();
                }

            }
        }
    }

    private void updateProfilePicture()
    {
        if (mProfilePhotoFile == null || !mProfilePhotoFile.exists())
        {
            System.out.println("setting profilePicture imageDrawable to null");
            mProfilePicture.setImageDrawable(null);
        }
        else
        {
            Bitmap bitmap = mProfilePhotoImage.getScaledBitmap(
                    mProfilePhotoFile.getPath(), NewUserInfoActivity.this);
            mProfilePicture.setImageBitmap(bitmap);
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
