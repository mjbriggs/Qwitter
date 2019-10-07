package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private PopupWindow mSearchWindow;
    private RelativeLayout mSearchContainer;
    private HomePresenter mHomePresenter;
    private String mUserAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //should validate if user is logged in before this
        mUserAlias = getIntent().getExtras().getString("USER_NAME");
        System.out.println("home user alias " + mUserAlias);

        //this is what blows up the feed fragment
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), mUserAlias, "FEED");
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorColor(Color.WHITE);
        FloatingActionButton fab = findViewById(R.id.fab);

//        mUserAlias = getIntent().getExtras().getString("USER_NAME");
//        System.out.println("home user alias " + mUserAlias);

        mHomePresenter = new HomePresenter();

        try
        {
            mHomePresenter.findLoggedInUser(mUserAlias);
            System.out.println(mHomePresenter.getHomeUser().getFeed().getStatusList().toString());
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        System.out.println("inflating custom menu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout_button:
                mHomePresenter.logoutUser();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_search_button:
                final SearchView mSearchView;
                Toast toast = Toast.makeText(HomeActivity.this, "clicked search", Toast.LENGTH_SHORT);
                toast.show();
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.search_view, null);

                // create the popup window
                int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token
                popupWindow.showAtLocation(new View(HomeActivity.this), Gravity.CENTER_VERTICAL, 0, 0);

                mSearchView = popupView.findViewById(R.id.search_view_bar);
                mSearchView.setSubmitButtonEnabled(true);
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
                {
                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        if(query.charAt(0) == '@')
                        {
                            if(mHomePresenter.doesUserExist(query.substring(1)))
                            {
                                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                                intent.putExtra("USER_NAME", query.substring(1));
                                intent.putExtra("LOGGED_USER", mUserAlias);
                                startActivity(intent);
                                popupWindow.dismiss();
                            }
                            else
                            {
                                Toast toasty = Toast.makeText(HomeActivity.this, query + " cannot be found", Toast.LENGTH_SHORT);
                                toasty.show();
                            }
                        }
                        else if(query.charAt(0) == '#')
                        {
                            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                            intent.putExtra("USER_NAME", mUserAlias);
                            intent.putExtra("TYPE", "SEARCH");
                            intent.putExtra("QUERY", query);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast toasty = Toast.makeText(HomeActivity.this, "search format not supported", Toast.LENGTH_SHORT);
                            toasty.show();
                        }


                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText)
                    {
                        return false;
                    }
                });

                Button mCloseButton = popupView.findViewById(R.id.close_search_button);
                mCloseButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        popupWindow.dismiss();
                    }
                });
                // dismiss the popup window when touched
//                popupView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        popupWindow.dismiss();
//                        return true;
//                    }
//                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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