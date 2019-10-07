package com.michael.qwitter.View;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.michael.qwitter.R;

public class SearchActivity extends AppCompatActivity implements RecyclerFragment.OnFragmentInteractionListener
{

    private String mType;
    private String mUserAlias;
    private String mQuery;
    private TextView mQueryView;
    private RecyclerFragment rf;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

       Bundle extras = getIntent().getExtras();
       mType = extras.getString("TYPE");
       mQuery = extras.getString("QUERY");
       mUserAlias = extras.getString("USER_NAME");

       System.out.println("type " + mType + " query " + mQuery + " user " + mUserAlias);

       mQueryView = findViewById(R.id.search_type);
       mQueryView.setText(mQuery);

       rf = RecyclerFragment.newInstance(mUserAlias, mQuery, mType);

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.search_container, rf);

        ft.commit();
    }

    public void onFragmentInteraction(int position)
    {
        Toast toast = Toast.makeText(SearchActivity.this, "Clicked " + position, Toast.LENGTH_SHORT);
        toast.show();
    }
}
