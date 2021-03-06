package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.PresenterFactory.ACPresenterFactory;
import com.michael.qwitter.Presenter.PresenterInterfaces.IRegistrationPresenter;
import com.michael.qwitter.Presenter.RegistrationPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IRegistrationView;

import java.util.ArrayList;

public class NewUserInfoActivity extends AppCompatActivity implements IRegistrationView
{

    private IRegistrationPresenter mNewUserInfoPresenter;
    private ImageView mProfilePicture;
    private TextView mEditPicture;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private String mFirstName;
    private String mLastName;
    private Button mCreateButton;
    private String mUserAlias;
    private Bitmap mBitmap;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_info);

        mUserAlias = getIntent().getExtras().getString("USER_NAME");
        mEmail = getIntent().getExtras().getString("EMAIL");



        mNewUserInfoPresenter = new RegistrationPresenter(this);
        mNewUserInfoPresenter = (IRegistrationPresenter)
                ACPresenterFactory.getInstance().createPresenter(Global.IRegistrationView, this);

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
        mFirstNameField.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    mFirstNameField.getBackground().setColorFilter(getColor(R.color.colorBlue),
                            PorterDuff.Mode.SRC_ATOP);
                }
                else
                {
                    mFirstNameField.getBackground().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        mLastNameField = findViewById(R.id.last_name_field);
        mLastNameField.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    mLastNameField.getBackground().setColorFilter(getColor(R.color.colorBlue),
                            PorterDuff.Mode.SRC_ATOP);
                }
                else
                {
                    mLastNameField.getBackground().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        mCreateButton = findViewById(R.id.create_profile_button);
        mCreateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mNewUserInfoPresenter.update(mUserAlias);
            }
        });

    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, Global.REQUEST_PHOTO);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mNewUserInfoPresenter.savePicture(requestCode, resultCode, data);
    }

    @Override
    public void clearFields()
    {
        mFirstNameField.setText("");
        mLastNameField.setText("");
        mProfilePicture.setImageResource(R.color.colorBlack);
    }

    @Override
    public ArrayList<String> grabTextFields()
    {
        mFirstName = mFirstNameField.getText().toString();
        mLastName = mLastNameField.getText().toString();


        ArrayList<String> fields = new ArrayList<>();

        fields.add(mFirstName);
        fields.add(mLastName);
        fields.add(mEmail);

        return fields;
    }

    @Override
    public Bitmap grabImageField()
    {
        return mBitmap;
    }

    @Override
    public void goTo(String view)
    {
        if(view.equals(Global.HomeActivity))
        {
            Intent intent = new Intent(NewUserInfoActivity.this, HomeActivity.class);
            intent.putExtra("USER_NAME", mUserAlias);
            startActivity(intent);
            finish();
        }
        else if(view.equalsIgnoreCase(Global.LoginActivity))
        {
            Intent intent = new Intent(NewUserInfoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void postToast(String message)
    {
        Toast toast = Toast.makeText(NewUserInfoActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void updateField(String field, Object object)
    {
        if(field.equals(Global.PROFILE_PIC))
        {
            if(object.getClass().equals(Bitmap.class))
            {
                Bitmap bitmap = (Bitmap) object;

                mBitmap = bitmap;
                mProfilePicture.setImageBitmap(mBitmap);
            }
        }
    }
}
