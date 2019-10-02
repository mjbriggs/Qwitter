package com.michael.qwitter.View;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.michael.qwitter.R;

public class RecyclerActivity extends AppCompatActivity implements RecyclerFragment.OnFragmentInteractionListener
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public RecyclerActivity()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view);

    }

    public void onFragmentInteraction(int position)
    {
        Toast toast = Toast.makeText(RecyclerActivity.this, "Clicked " + position, Toast.LENGTH_SHORT);
        toast.show();
    }
}
