package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Presenter.HomePresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.View.ui.main.SectionsPagerAdapter;

public class HomeActivity extends AppCompatActivity implements RecyclerFragment.OnFragmentInteractionListener
{

    private CoordinatorLayout mHomeLayout;
    private PopupWindow createStatusWindow;
    private HomePresenter mHomePresenter;
    private String mUserAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //this is what blows up the feed fragment
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorColor(Color.WHITE);
        FloatingActionButton fab = findViewById(R.id.fab);

        mUserAlias = getIntent().getExtras().getString("USER_NAME");
        System.out.println("home user alias " + mUserAlias);

        mHomePresenter = new HomePresenter();

        try
        {
            mHomePresenter.findLoggedInUser(mUserAlias);
            System.out.println(mHomePresenter.getHomeUser().getStatuses().toString());
            Status stat = mHomePresenter.getHomeUser().getStatuses().get(mHomePresenter.getHomeUser().getStatuses().size() - 1);
            Toast toast = Toast.makeText(HomeActivity.this, stat.toString()
                    , Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mHomeLayout = findViewById(R.id.activity_home);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mHomePresenter.findLoggedInUser(mUserAlias))
                {
                    Intent intent = new Intent(HomeActivity.this, CreateStatusActivity.class);
                    intent.putExtra("USER_NAME", mUserAlias);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mHomePresenter.findLoggedInUser(mUserAlias);
        System.out.println(mHomePresenter.getHomeUser().getStatuses().toString());
    }

    public void onFragmentInteraction(int position)
    {
        Toast toast = Toast.makeText(HomeActivity.this, "Clicked " + position, Toast.LENGTH_SHORT);
        toast.show();
    }
}