package com.michael.qwitter.View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.michael.qwitter.R;
import com.michael.qwitter.View.ViewInterfaces.IView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecyclerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecyclerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerFragment extends Fragment implements IView
{

    private OnFragmentInteractionListener mListener;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mUserAlias;
    private String mFeedType;
    private String mQuery;
    private Button mLoadButton;
    private static IView mHomeView;
    private ProgressBar mLoadingIcon;


    public RecyclerFragment()
    {
        // Required empty public constructor
       // myDataset = new String[] {"1", "2", "3"};
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecyclerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecyclerFragment newInstance(String username, String type, IView homeView)
    {
        mHomeView = homeView;
        System.out.println("username in new instance " + username);
        System.out.println("type in new instance " + type);
        RecyclerFragment fragment = new RecyclerFragment();
        Bundle args = new Bundle();
        args.putString("USER_NAME", username);
        args.putString("TYPE", type);
        fragment.setArguments(args);
        return fragment;
    }

    public static RecyclerFragment newInstance(String username, String query, String type, IView homeView)
    {
        mHomeView = homeView;
        System.out.println("username in new instance " + username);
        System.out.println("type in new instance " + type);
        System.out.println("query in new instance " + query);
        RecyclerFragment fragment = new RecyclerFragment();
        Bundle args = new Bundle();
        args.putString("USER_NAME", username);
        args.putString("TYPE", type);
        args.putString("QUERY", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = view.findViewById(R.id.my_fragment_recycler_view);

        System.out.println("in fragment onCreateView");

        mUserAlias = getArguments().getString("USER_NAME");
        mFeedType = getArguments().getString("TYPE");
        try
        {
            mQuery = getArguments().getString("QUERY");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            mQuery = "";
        }


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mLoadingIcon = view.findViewById(R.id.search_progress_bar);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLoadButton = view.findViewById(R.id.load_button);
        mLoadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                mLoadButton.setTextColor(Color.BLACK);
//                mLoadButton.setBackgroundColor(Color.WHITE);
                mAdapter.update();
//                mLoadButton.setTextColor(Color.WHITE);
//                mLoadButton.setBackgroundResource(R.color.colorBlue);
            }
        });

        // specify an adapter (see also next example)
        if(mFeedType.equalsIgnoreCase("SEARCH"))
        {
            System.out.println("Setting search adapter");
            mAdapter = new RecyclerAdapter(mUserAlias, mQuery, mFeedType, getContext(), this);
            mLoadingIcon.setVisibility(View.VISIBLE);
            mLoadButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            mAdapter = new RecyclerAdapter(mUserAlias, mFeedType, getContext(), this);
        }
        mRecyclerView.setAdapter(mAdapter);
        System.out.println("Adapter is set");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int position)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(position);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
        mHomeView.updateField("done", null);
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(int position);
    }

    @Override
    public void updateField(String field, Object object)
    {
        if (field.equalsIgnoreCase("done"))
        {
            mHomeView.updateField("done", null);
            mLoadingIcon.setVisibility(View.INVISIBLE);
        }
        else if (field.equalsIgnoreCase("starting"))
        {
            mHomeView.updateField("starting", null);
            mLoadingIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void goTo(String view)
    {

    }

    @Override
    public void postToast(String message)
    {

    }
}
