package com.michael.qwitter.View.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.michael.qwitter.R;
import com.michael.qwitter.View.RecyclerFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.feed_text, R.string.story_text,
            R.string.followers_text, R.string.following_text};
    private final Context mContext;
    private String mUserAlias;
    private String mType;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String username, String type)
    {
        super(fm);
        mContext = context;
        mUserAlias = username;
        mType = type;
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position == 0)
        {
//            Toast toast = Toast.makeText(mContext, "FEED", Toast.LENGTH_SHORT);
//            toast.show();
            return RecyclerFragment.newInstance(mUserAlias, "FEED");
        }
        else if (position == 1)
        {
//            Toast toast = Toast.makeText(mContext, "STORY", Toast.LENGTH_SHORT);
//            toast.show();
            return RecyclerFragment.newInstance(mUserAlias, "STORY");
        }
        else if (position == 2)
        {
//            Toast toast = Toast.makeText(mContext, "FOLLOWERS", Toast.LENGTH_SHORT);
//            toast.show();
            return RecyclerFragment.newInstance(mUserAlias, "FOLLOWERS");
        }
        else
        {
//            Toast toast = Toast.makeText(mContext, "FOLLOWING", Toast.LENGTH_SHORT);
//            toast.show();
            return RecyclerFragment.newInstance(mUserAlias, "FOLLOWING");
        }



    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount()
    {
        return TAB_TITLES.length;
    }
}