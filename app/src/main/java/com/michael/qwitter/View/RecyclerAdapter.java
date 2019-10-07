package com.michael.qwitter.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.michael.qwitter.Presenter.FeedPresenter;
import com.michael.qwitter.Presenter.FollowersPresenter;
import com.michael.qwitter.Presenter.FollowingPresenter;
import com.michael.qwitter.Presenter.RegistrationInterface;
import com.michael.qwitter.Presenter.RegistrationPresenter;
import com.michael.qwitter.Presenter.RelationPresenter;
import com.michael.qwitter.Presenter.SearchPresenter;
import com.michael.qwitter.Presenter.StatusPresenter;
import com.michael.qwitter.Presenter.StoryPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Month;
import com.michael.qwitter.Utils.StatusParser;

import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>
{

    private StatusPresenter mFeedPresenter;
    private StatusPresenter mStoryPresenter;
    private StatusPresenter mSearchPresenter;
    private RelationPresenter mFollowersPresenter;
    private RelationPresenter mFollowingPresenter;
    private String mFeedType;
    private String mUserAlias;
    private Date mStatusDate;
    private Month mStatusMonth;
    private Context mContext;
    private ViewGroup mParent;
    private String mQuery;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected LinearLayout layoutView;
        protected LinearLayout statusContainer;
        protected TextView alias;
        protected TextView name;
        protected TextView statusTimeStamp;
        protected TextView statusText;
        protected ImageView profilePicture;
        protected ImageView statusImage;
        protected VideoView statusVideo;

        public RecyclerHolder(LinearLayout v) {
            super(v);
            layoutView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(String username, String type, Context context)
    {
        mFeedPresenter = null;
        mFollowersPresenter = null;
        mStoryPresenter = null;
        mFollowingPresenter = null;
        mUserAlias = username;
        mFeedType = type;
        mContext = context;
        mQuery = "";
    }

    public RecyclerAdapter(String username, String query, String type, Context context)
    {
        mFeedPresenter = null;
        mFollowersPresenter = null;
        mStoryPresenter = null;
        mFollowingPresenter = null;
        mUserAlias = username;
        mFeedType = type;
        mContext = context;
        mQuery = query;

        System.out.println("In adapter query is " + mQuery + " and type is " + mFeedType);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        mParent = parent;
        LinearLayout v = new LinearLayout(parent.getContext());
        // create a new view
        if(mFeedType.equalsIgnoreCase("FEED") || mFeedType.equalsIgnoreCase("STORY") || mFeedType.equalsIgnoreCase("SEARCH"))
        {
             v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.status_view, parent, false);
        }
        else if(mFeedType.equalsIgnoreCase("FOLLOWERS") || mFeedType.equalsIgnoreCase("FOLLOWING"))
        {
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.follow_view, parent, false);
        }


        RecyclerHolder vh = new RecyclerHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //TODO place a lot of the logic of getting info within the presenter, right now things are messy

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mFeedType.equalsIgnoreCase("FEED") ||
        mFeedType.equalsIgnoreCase("STORY") ||
        mFeedType.equalsIgnoreCase("SEARCH"))
        {
            holder.statusText = holder.layoutView.findViewById(R.id.status_text);
            holder.alias = holder.layoutView.findViewById(R.id.status_user_alias);
            holder.name = holder.layoutView.findViewById(R.id.status_name);
            holder.statusTimeStamp = holder.layoutView.findViewById(R.id.status_timestamp);

            ViewGroup.LayoutParams params = holder.layoutView.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 700;
            holder.layoutView.setLayoutParams(params);

            if(mFeedType.equalsIgnoreCase("FEED"))
            {
                mFeedPresenter = new FeedPresenter();   //should base these on username not logged in username

                holder.statusText.setText(mFeedPresenter.getStatuses(mUserAlias).get(position).getText());
                holder.alias.setText(mFeedPresenter.getUserAlias(position));
                holder.name.setText(mFeedPresenter.getUserFullName());
                mStatusDate = mFeedPresenter.getStatuses(mUserAlias).get(position).getTimePosted();
                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date = mStatusMonth + " " + mStatusDate.getDay();
                holder.statusTimeStamp.setText(date);
                //TODO set profile picture
                //TODO set attachment

            }
            else if(mFeedType.equalsIgnoreCase("STORY"))
            {
                mStoryPresenter = new StoryPresenter();

                holder.statusText.setText(mStoryPresenter.getStatuses(mUserAlias).get(position).getText());
                holder.alias.setText("@" + mUserAlias);
                holder.name.setText(mStoryPresenter.getUserFullName());
                mStatusDate = mStoryPresenter.getStatuses(mUserAlias).get(position).getTimePosted();
                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date = mStatusMonth + " " + mStatusDate.getDay();
                holder.statusTimeStamp.setText(date);
                //TODO set profile picture
                //TODO set attachment
            }

            else if(mFeedType.equalsIgnoreCase("SEARCH"))
            {
                System.out.println("Search feed query is " + mQuery);
                mSearchPresenter = new SearchPresenter(mQuery);

                holder.statusText.setText(mSearchPresenter.getStatuses("").get(position).getText());
                holder.alias.setText("@" + mSearchPresenter.getStatus(position).getOwner());
                holder.name.setText(mSearchPresenter.getStatus(position).getOwnerName());
                mStatusDate = mSearchPresenter.getStatus(position).getTimePosted();
                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date = mStatusMonth + " " + mStatusDate.getDay();
                holder.statusTimeStamp.setText(date);
                //TODO set profile picture
                //TODO set attachment
            }

            Linkify.addLinks(holder.statusText, Linkify.WEB_URLS);

            CharSequence sequence = holder.statusText.getText();
            SpannableString strBuilder = new SpannableString(sequence);
            ArrayList<Integer> hashtagIndices = StatusParser.parseForHashTags(holder.statusText.getText().toString());
            ArrayList<Integer> mentionIndices = StatusParser.parseForMentions(holder.statusText.getText().toString());


            System.out.println("hashtagIndices in " + holder.statusText.getText().toString() + " are " +
                    hashtagIndices.toString());

            for(int j = 0; j < hashtagIndices.size(); j+=2)
            {
                final int start = hashtagIndices.get(j);
                final int end = hashtagIndices.get(j + 1);
                final String str = holder.statusText.getText().toString().substring(start, end + 1);
                System.out.println("start " + start + " , end " + end +
                        " tag " + holder.statusText.getText().toString().substring(start, end + 1));

                final int in = start;

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        Intent intent = new Intent(mContext, SearchActivity.class);
                        intent.putExtra("USER_NAME", mUserAlias);
                        intent.putExtra("TYPE", "SEARCH");
                        intent.putExtra("QUERY", str);
                        mContext.startActivity(intent);
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                    }
                };
                strBuilder.setSpan(clickableSpan, start, end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for(int j = 0; j < mentionIndices.size(); j+=2)
            {
                final int start = mentionIndices.get(j);
                final int end = mentionIndices.get(j + 1);
                String notFinal;
                if(end > start)
                    notFinal = holder.statusText.getText().toString().substring(start + 1, end + 1);
                else
                    notFinal = "";

                final String str = notFinal;

                System.out.println("start " + start + " , end " + end +
                        " mention " + holder.statusText.getText().toString().substring(start, end + 1));

                final int in = start;
                final RegistrationInterface existenceChecker = new RegistrationPresenter();

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("USER_NAME", str);
                        intent.putExtra("LOGGED_USER", mUserAlias);
                        if(existenceChecker.isUserCreated(str))
                        {
                            mContext.startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(mContext, "Nothing to show, user " + str + " does not exist", Toast.LENGTH_SHORT).show();
                        }                        //Toast.makeText(mContext, "clicked " + in, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                    }
                };
                strBuilder.setSpan(clickableSpan, start, end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            holder.statusText.setText(strBuilder);
            holder.statusText.setMovementMethod(LinkMovementMethod.getInstance());
            holder.statusText.setHighlightColor(Color.TRANSPARENT);

        }
        else
        {
            ViewGroup.LayoutParams params = holder.layoutView.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 275;
            holder.layoutView.setLayoutParams(params);

            holder.name = holder.layoutView.findViewById(R.id.follow_name);
            holder.alias = holder.layoutView.findViewById(R.id.follow_user_alias);

            if(mFeedType.equalsIgnoreCase("FOLLOWERS"))
            {
                mFollowersPresenter = new FollowersPresenter(mUserAlias);

                holder.name.setText(mFollowersPresenter.getFollowers().getFollowers().get(position).getFirstName() + " "
                        + mFollowersPresenter.getFollowers().getFollowers().get(position).getLastName());
                holder.alias.setText("@" + mFollowersPresenter.getFollowers().getFollowers().get(position).getUserAlias());
                //TODO set profile picture
            }
            else if (mFeedType.equalsIgnoreCase("FOLLOWING"))
            {
                mFollowingPresenter = new FollowingPresenter(mUserAlias);

                holder.name.setText(mFollowingPresenter.getFollowing().getFollowing().get(position).getFirstName() + " "
                        + mFollowingPresenter.getFollowing().getFollowing().get(position).getLastName());
                holder.alias.setText("@" + mFollowingPresenter.getFollowing().getFollowing().get(position).getUserAlias());
                //TODO set profile picture
            }
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mFeedType.equalsIgnoreCase("FEED"))
        {
            if(mFeedPresenter == null)
                mFeedPresenter = new FeedPresenter();

            return mFeedPresenter.getStatuses(mUserAlias).size();
        }
        else if(mFeedType.equalsIgnoreCase("STORY"))
        {
            if(mStoryPresenter == null)
                mStoryPresenter = new StoryPresenter();

            return mStoryPresenter.getStatuses(mUserAlias).size();
        }
        else if(mFeedType.equalsIgnoreCase("FOLLOWERS"))
        {
            if(mFollowersPresenter == null)
                mFollowersPresenter = new FollowersPresenter(mUserAlias);

            return mFollowersPresenter.getFollowers().getFollowers().size();
        }
        else if(mFeedType.equalsIgnoreCase("FOLLOWING"))
        {
            if(mFollowingPresenter == null)
                mFollowingPresenter = new FollowingPresenter(mUserAlias);

            return mFollowingPresenter.getFollowing().getFollowing().size();
        }

        else if(mFeedType.equalsIgnoreCase("SEARCH"))
        {
            if(mSearchPresenter == null)
                mSearchPresenter = new SearchPresenter(mQuery);

            return mSearchPresenter.getStatuses("").size();
        }

        return 0;
    }

}
