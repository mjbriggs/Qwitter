package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.michael.qwitter.Presenter.PresenterFactory.ACPresenterFactory;
import com.michael.qwitter.Presenter.PresenterInterfaces.IProfilePresenter;
import com.michael.qwitter.Presenter.ProfilePresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IProfileView;
import com.michael.qwitter.View.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;

public class ProfileActivity extends HomeActivity implements IProfileView
{
    private LinearLayout mUserInfoLayout;
    private LinearLayout mFollowLayout;
    private String mUserAlias;
    private TextView mUserName;
    private TextView mUserAliasView;
    private ImageView mUserProfilePict;
    private Button mFollowButton;
    private Button mBackButton;
    private IProfilePresenter mProfilePresenter;
    private ACPresenterFactory mPresenterFactory;
    private String mLoggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mProfilePresenter = (ProfilePresenter)
                ACPresenterFactory.getInstance().createPresenter(Global.ProfileActivity, this);
        //should validate if user is logged in before this
        mUserAlias = getIntent().getExtras().getString("USER_NAME");
//        System.out.println("profile user alias " + mUserAlias);

        mLoggedUser = getIntent().getExtras().getString("LOGGED_USER");
//        System.out.println("Logger user " + mLoggedUser);

        mUserInfoLayout = findViewById(R.id.profile_info_container);
        mUserInfoLayout.setVisibility(View.VISIBLE);
        mUserInfoLayout.setMinimumHeight(300);

        mFollowLayout = findViewById(R.id.follow_view);
        mFollowLayout.setBackgroundColor(Color.WHITE);

        mUserName = findViewById(R.id.follow_name);

        mUserAliasView = findViewById(R.id.follow_user_alias);

        //TODO set profile picture

        mFollowButton = findViewById(R.id.follow_button);
        mFollowButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mProfilePresenter.handleFollowClick();
            }
        });

        mProfilePresenter.setProfileInfo();

//        mUserProfilePict = findViewById(R.id.follow_profile_picture);
//        mUserProfilePict.setImageResource(R.drawable.ic_launcher_foreground);

        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        //this is what blows up the feed fragment
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.setMargins(0, 300, 0,0);
        appBarLayout.setLayoutParams(params);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), mUserAlias, "FEED");
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorColor(Color.WHITE);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
//        mHomePresenter.findLoggedInUser(mUserAlias);
//        System.out.println(mHomePresenter.getHomeUser().getStatuses().toString());
    }

    public void onFragmentInteraction(int position)
    {
        Toast toast = Toast.makeText(ProfileActivity.this, "Clicked " + position, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void postToast(String message)
    {
        super.postToast(message);
    }

    @Override
    public void goTo(String view)
    {
        super.goTo(view);
    }

    @Override
    public void updateField(String field, Object object)
    {
        super.updateField(field, object);
        if(field.equalsIgnoreCase("R.id.follow_name"))
        {
            String s = (String) object;
            mUserName.setText(s);
        }
        else if(field.equalsIgnoreCase("R.id.follow_user_alias"))
        {
            String s = (String) object;
            mUserAliasView.setText(s);
        }
        else if (field.equalsIgnoreCase("Hide.R.id.follow_button"))
        {
            mFollowButton.setVisibility(View.INVISIBLE);
        }
        else if (field.equalsIgnoreCase("R.id.follow_button"))
        {
            mFollowButton.setVisibility(View.VISIBLE);
            String s = (String) object;
            mFollowButton.setText(s);
        }
    }

    @Override
    public ArrayList<String> profileInfo()
    {
        ArrayList<String> info = new ArrayList<>();
        info.add(mUserAlias);
        info.add(mLoggedUser);
        return info;
    }
}
