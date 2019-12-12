package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.CreateStatusPresenter;
import com.michael.qwitter.Presenter.HomePresenter;
import com.michael.qwitter.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class CreateStatusActivity extends AppCompatActivity
{

    private Button mShareButton;
    private Button mCloseButton;
    private TextView mName;
    private TextView mAlias;
    private EditText mStatus;
    private EditText mUrl;
    private ImageView mProfilePicture;
    private CreateStatusPresenter mCreateStatusPresenter;
    private HomePresenter mHomePresenter;
    private String mUserAlias;
    private Button mAddImageButton;
    private Button mAddVideoButton;

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, 2);
        }
    }

    private void dispatchTakeVideoIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, 2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_status_window);

        mUserAlias = getIntent().getExtras().getString("USER_NAME");

        System.out.println("Username " + mUserAlias);

        mCreateStatusPresenter = new CreateStatusPresenter();

        mCreateStatusPresenter.setStatusUser(mUserAlias);

        mUrl = findViewById(R.id.create_status_attachment_url);

        mStatus = findViewById(R.id.status_text);
        mStatus.setSelection(0);

        mName = findViewById(R.id.create_status_name);
        mName.setText(mCreateStatusPresenter.getUserFullName());

        mAlias = findViewById(R.id.create_status_user_alias);
        mAlias.setText(mCreateStatusPresenter.getUserAlias());

        mProfilePicture = findViewById(R.id.create_status_profile_picture);
        Picasso.get().invalidate(mCreateStatusPresenter.getImageURL());
        Picasso.get().load(mCreateStatusPresenter.getImageURL())
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(mProfilePicture);

        mShareButton = findViewById(R.id.share_button);
        mShareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCreateStatusPresenter.setStatus(mStatus.getText().toString());
                mCreateStatusPresenter.addStatusToUser();

                Intent intent = new Intent(CreateStatusActivity.this, HomeActivity.class);
                intent.putExtra("USER_NAME", mUserAlias);
                intent.putExtra("PROFILE", false);
                startActivity(intent);
                finish();
            }
        });

        mCloseButton = findViewById(R.id.close_button);
        mCloseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CreateStatusActivity.this, HomeActivity.class);
                intent.putExtra("USER_NAME", mUserAlias);
                intent.putExtra("PROFILE", false);
                startActivity(intent);
                finish();
            }
        });

        mAddImageButton = findViewById(R.id.add_image_button);
        mAddImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAddImageButton.setBackgroundColor(Color.DKGRAY);
                mAddVideoButton.setBackgroundColor(Color.LTGRAY);
                mCreateStatusPresenter.addAsImage(mUrl.getText().toString());
//                dispatchTakePictureIntent();
            }
        });

        mAddVideoButton = findViewById(R.id.add_video_button);
        mAddVideoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAddVideoButton.setBackgroundColor(Color.DKGRAY);
                mAddImageButton.setBackgroundColor(Color.LTGRAY);
                mCreateStatusPresenter.addAsVideo(mUrl.getText().toString());
            }
        });
    }
}
