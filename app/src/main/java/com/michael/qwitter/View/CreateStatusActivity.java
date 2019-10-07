package com.michael.qwitter.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.CreateStatusPresenter;
import com.michael.qwitter.Presenter.HomePresenter;
import com.michael.qwitter.R;

public class CreateStatusActivity extends AppCompatActivity
{

    private Button mShareButton;
    private Button mCloseButton;
    private TextView mName;
    private TextView mAlias;
    private EditText mStatus;
    private ImageView mProfilePicture;
    private CreateStatusPresenter mCreateStatusPresenter;
    private HomePresenter mHomePresenter;
    private String mUserAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_status_window);

        mUserAlias = getIntent().getExtras().getString("USER_NAME");

        System.out.println("Username " + mUserAlias);

        mCreateStatusPresenter = new CreateStatusPresenter();

        mCreateStatusPresenter.setStatusUser(mUserAlias);

        mStatus = findViewById(R.id.status_text);
        mStatus.setSelection(0);

        mName = findViewById(R.id.create_status_name);
        mName.setText(mCreateStatusPresenter.getUserFullName());

        mAlias = findViewById(R.id.create_status_user_alias);
        mAlias.setText(mCreateStatusPresenter.getUserAlias());

        mProfilePicture = findViewById(R.id.create_profile_picture);
        //TODO fix image loading and saving
//        try
//        {
//            mProfilePicture.setImageBitmap(mCreateStatusPresenter.getUserProfilePicture());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            mProfilePicture.setImageBitmap(null);
//        }

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
    }
}
