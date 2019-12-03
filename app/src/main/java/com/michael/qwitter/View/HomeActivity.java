package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.michael.qwitter.Presenter.HomePresenter;
import com.michael.qwitter.Presenter.PresenterFactory.ACPresenterFactory;
import com.michael.qwitter.Presenter.PresenterInterfaces.IHomePresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.View.ViewInterfaces.IHomeView;
import com.michael.qwitter.View.ui.main.SectionsPagerAdapter;

public class HomeActivity extends AppCompatActivity implements RecyclerFragment.OnFragmentInteractionListener, IHomeView
{

    private CoordinatorLayout mHomeLayout;
    private PopupWindow createStatusWindow;
    private PopupWindow mSearchWindow;
    private RelativeLayout mSearchContainer;
    private IHomePresenter mHomePresenter; //TODO update to interface
    private ACPresenterFactory mPresenterFactory;
    private String mUserAlias;
    private String mQuery;
    private SearchView mSearchView;
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private Button mLoadButton;
    private ProgressBar mLoadingIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //should validate if user is logged in before this
        mUserAlias = getIntent().getExtras().getString("USER_NAME");
        System.out.println("home user alias " + mUserAlias);

        mHomePresenter = (HomePresenter)
                ACPresenterFactory.getInstance().createPresenter(Global.HomeActivity, this);

        mHomePresenter.setHomeUser(mUserAlias);

        //this is what blows up the feed fragment
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), mUserAlias, "FEED", this);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorColor(Color.WHITE);
        FloatingActionButton fab = findViewById(R.id.fab);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        mPopupView = inflater.inflate(R.layout.search_view, null);

        // create the popup window
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        mPopupWindow = new PopupWindow(mPopupView, width, height, focusable);

        mSearchView = mPopupView.findViewById(R.id.search_view_bar);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                mQuery = query;
                mHomePresenter.handleQuery(mQuery);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        Button mCloseButton = mPopupView.findViewById(R.id.close_search_button);
        mCloseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPopupWindow.dismiss();
            }
        });

        mHomeLayout = findViewById(R.id.activity_home);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mHomePresenter.openView(Global.CreateStatusActivity);
            }
        });

//        mLoadButton = findViewById(R.id.load_button);
//        mLoadButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//            }
//        });


        mLoadingIcon = findViewById(R.id.home_progress_bar);
        mLoadingIcon.setVisibility(View.INVISIBLE);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mHomePresenter.savePicture(requestCode, resultCode, data);
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, 2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        return mHomePresenter.handleMenu(item);
    }

    @Override
    public String user()
    {
        return mUserAlias;
    }

    @Override
    public void postToast(String message)
    {
        Toast toasty = Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT);
        toasty.show();
    }

    @Override
    public void goTo(String view)
    {
        if(view.equals(Global.CreateStatusActivity))
        {
            Intent intent = new Intent(HomeActivity.this, CreateStatusActivity.class);
            intent.putExtra("USER_NAME", mUserAlias);
            startActivity(intent);
            finish();
        }
        else if (view.equals(Global.LoginActivity))
        {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(view.equals(Global.ProfileActivity))
        {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("USER_NAME", mQuery.substring(1));
            intent.putExtra("LOGGED_USER", mUserAlias);
            startActivity(intent);
            mPopupWindow.dismiss();
        }
        else if (view.equals(Global.SearchView))
        {
            Log.d(Global.DEBUG, "searchview clicked");
            mPopupWindow.showAtLocation(new View(HomeActivity.this), Gravity.CENTER_VERTICAL, 0, 0);
        }
        else if(view.equals(Global.ProfileActivity))
        {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("USER_NAME", mQuery.substring(1));
            intent.putExtra("LOGGED_USER", mUserAlias);
            startActivity(intent);
            mPopupWindow.dismiss();
        }
        else if (view.equals(Global.SearchActivity))
        {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("USER_NAME", mUserAlias);
            intent.putExtra("TYPE", "SEARCH");
            intent.putExtra("QUERY", mQuery);
            startActivity(intent);
        }
    }

    @Override
    public void takePhoto()
    {
        dispatchTakePictureIntent();
    }

    @Override
    public void updateField(String field, Object object)
    {
        if(field.equalsIgnoreCase(SearchView.class.toString()))
        {
            mSearchView.setQuery((String) object, false);
        }
        else if (field.equalsIgnoreCase("done"))
        {
            mLoadingIcon.setVisibility(View.INVISIBLE);
        }
        else if (field.equalsIgnoreCase("starting"))
        {
            mLoadingIcon.setVisibility(View.VISIBLE);
        }
    }

    public void onFragmentInteraction(int position)
    {
        Toast toast = Toast.makeText(HomeActivity.this, "Clicked " + position, Toast.LENGTH_SHORT);
        toast.show();
    }
}