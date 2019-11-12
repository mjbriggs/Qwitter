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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Presenter.FeedPresenter;
import com.michael.qwitter.Presenter.FollowersPresenter;
import com.michael.qwitter.Presenter.FollowingPresenter;
import com.michael.qwitter.Presenter.PresenterInterfaces.IRegistrationPresenter;
import com.michael.qwitter.Presenter.PresenterInterfaces.RelationPresenter;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;
import com.michael.qwitter.Presenter.RegistrationPresenter;
import com.michael.qwitter.Presenter.SearchPresenter;
import com.michael.qwitter.Presenter.StoryPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.Utils.Month;
import com.michael.qwitter.Utils.StatusParser;
import com.michael.qwitter.View.ViewInterfaces.IView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> implements IView
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
    private RecyclerHolder mLocalHolder;

    /**
     * In order to adhere to presenter pattern, I will have the local holder be set and then I will set local holder to view holder
     * */
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

        mFeedPresenter = new FeedPresenter();
        mStoryPresenter = new StoryPresenter();
        mFollowersPresenter = new FollowersPresenter(mUserAlias);
        mFollowingPresenter = new FollowingPresenter(mUserAlias);
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

        mFeedPresenter = new FeedPresenter();
        mStoryPresenter = new StoryPresenter();
        mFollowersPresenter = new FollowersPresenter(mUserAlias);
        mFollowingPresenter = new FollowingPresenter(mUserAlias);

        System.out.println("In adapter query is " + mQuery + " and type is " + mFeedType);
    }

    public void update()
    {
        if (mFeedType.equalsIgnoreCase(Global.FEED))
            mFeedPresenter.update(mUserAlias);
        if (mFeedType.equalsIgnoreCase(Global.STORY))
            mStoryPresenter.update(mUserAlias);
        if (mFeedType.equalsIgnoreCase(Global.FOLLOWERS))
            mFollowersPresenter.update(mUserAlias);
        if (mFeedType.equalsIgnoreCase(Global.FOLLOWING))
            mFollowingPresenter.update(mUserAlias);
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
    public void onBindViewHolder(RecyclerHolder holder, final int position) {
        //TODO place a lot of the logic of getting info within the presenter, right now things are messy

        mLocalHolder = holder;
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
            holder.statusImage = holder.layoutView.findViewById(R.id.status_image);
            holder.statusImage.setVisibility(View.INVISIBLE);
            holder.statusContainer = holder.layoutView.findViewById(R.id.status_container);

            ViewGroup.LayoutParams params = holder.layoutView.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 400;
            holder.layoutView.setLayoutParams(params);

            holder.statusContainer.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(mFeedType.equalsIgnoreCase("FEED"))
                    {
//                        mFeedPresenter = new FeedPresenter();
                        Intent intent = new Intent(mContext, SoloStatusActivity.class);
                        intent.putExtra("TEXT", mFeedPresenter.getStatuses(mUserAlias).get(position).getText());
                        intent.putExtra("USER_NAME", mFeedPresenter.getUserAlias(position));
                        intent.putExtra("FULL_NAME", mFeedPresenter.getUserFullName());
                        intent.putExtra("DATE", mFeedPresenter.getStatuses(mUserAlias).get(position).getTimePosted());
//                        if(mFeedPresenter.getStatuses(mUserAlias).get(position).getAttachment() != null)
//                        {
//                            Drawable d = mContext.getResources().getDrawable(R.drawable.new_icon);
//                            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//                            intent.putExtra("IMG", bitmap);
//                        }
                        mContext.startActivity(intent);
                    }
                    else if(mFeedType.equalsIgnoreCase("STORY"))
                    {
//                        mStoryPresenter = new StoryPresenter();
                        Intent intent = new Intent(mContext, SoloStatusActivity.class);
                        intent.putExtra("TEXT", mStoryPresenter.getStatuses(mUserAlias).get(position).getText());
                        intent.putExtra("USER_NAME", mUserAlias);
                        intent.putExtra("FULL_NAME", mStoryPresenter.getUserFullName());
                        intent.putExtra("DATE", mStoryPresenter.getStatuses(mUserAlias).get(position).getTimePosted());
                        mContext.startActivity(intent);
                    }
//                    else if(mFeedType.equalsIgnoreCase("SEARCH"))
//                    {
//                        mSearchPresenter = new SearchPresenter();
//                        Intent intent = new Intent(mContext, SoloStatusActivity.class);
//                        intent.putExtra("TEXT", mSearchPresenter.getStatuses(mUserAlias).get(position).getText());
//                        intent.putExtra("USER_NAME", mSearchPresenter.getUserAlias(position));
//                        intent.putExtra("FULL_NAME", mSearchPresenter.getUserFullName());
//                        intent.putExtra("DATE", mSearchPresenter.getStatuses(mUserAlias).get(position).getTimePosted());
//                    }
                }
            });

            if(mFeedType.equalsIgnoreCase("FEED"))
            {
//                mFeedPresenter = new FeedPresenter();   //should base these on username not logged in username
                Status stat = mFeedPresenter.getStatuses(mUserAlias).get(position);
                holder.statusText.setText(mFeedPresenter.getStatuses(mUserAlias).get(position).getText());
                holder.alias.setText("@" + mFeedPresenter.getUserAlias(position));
                holder.name.setText(mFeedPresenter.getUserFullName());
                mStatusDate = mFeedPresenter.getStatuses(mUserAlias).get(position).getTimePosted();
                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date = mStatusMonth + " " + mStatusDate.getDay();
                holder.statusTimeStamp.setText(date);

                if(stat.getAttachment() != null)
                {
                    Log.i(Global.INFO, "status" + stat.getText() + "\n file path is " + stat.getAttachment().getFilePath());
                    if(stat.getAttachment().getFilePath() != null &&
                    stat.getAttachment().getFilePath().length() > 0)
                    {
                        Picasso.get().load(stat.getAttachment().getFilePath()).into(holder.statusImage);
                        holder.statusImage.setVisibility(View.VISIBLE);
                    }
                }
//                if(mFeedPresenter.getStatuses(mUserAlias).get(position).getAttachment() != null)
//                {
//                    System.out.println("status " + mFeedPresenter.getStatuses(mUserAlias).get(position).getText() + " at position " + position + " has image, setting image resource");
//                    holder.statusImage.setVisibility(View.VISIBLE);
//                    Drawable d = mContext.getResources().getDrawable(R.drawable.new_icon);
//                    holder.statusImage.setBackgroundColor(Color.BLACK);
//                    holder.statusImage.setImageDrawable(d);
//                    //holder.statusImage.setImageResource(R.drawable.new_icon);
//                    holder.statusImage.setMinimumHeight(300);
//                    holder.statusImage.setMinimumWidth(300);
//                    params.height = 700;
//                    holder.layoutView.setLayoutParams(params);
//                }
//                else
//                {
//                    System.out.println("status " + mFeedPresenter.getStatuses(mUserAlias).get(position).getText() + " has no image, image is null");
//
//                }
                //TODO set profile picture
                //TODO set attachment

            }
            else if(mFeedType.equalsIgnoreCase("STORY"))
            {
//                mStoryPresenter = new StoryPresenter();
                Status stat = mStoryPresenter.getStatuses(mUserAlias).get(position);
                holder.statusText.setText(mStoryPresenter.getStatuses(mUserAlias).get(position).getText());
                holder.alias.setText("@" + mUserAlias);
                holder.name.setText(mStoryPresenter.getUserFullName());
                mStatusDate = mStoryPresenter.getStatuses(mUserAlias).get(position).getTimePosted();
                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date = mStatusMonth + " " + mStatusDate.getDay();
                holder.statusTimeStamp.setText(date);

                if(stat.getAttachment() != null)
                {
                    Log.i(Global.INFO, "status" + stat.getText() + "\n file path is " + stat.getAttachment().getFilePath());
                    if(stat.getAttachment().getFilePath() != null &&
                            stat.getAttachment().getFilePath().length() > 0)
                    {
                        Picasso.get().load(stat.getAttachment().getFilePath()).into(holder.statusImage);
                        holder.statusImage.setVisibility(View.VISIBLE);
                    }
                }
                //TODO set profile picture
                //TODO set attachment
            }

            else if(mFeedType.equalsIgnoreCase("SEARCH"))
            {
                System.out.println("Search feed query is " + mQuery);
                mSearchPresenter = new SearchPresenter(mQuery);
                Status stat = mSearchPresenter.getStatuses(mUserAlias).get(position);
                holder.statusText.setText(mSearchPresenter.getStatuses("").get(position).getText());
                holder.alias.setText("@" + mSearchPresenter.getStatus(position).getOwner());
                holder.name.setText(mSearchPresenter.getStatus(position).getOwnerName());
                mStatusDate = mSearchPresenter.getStatus(position).getTimePosted();
                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date = mStatusMonth + " " + mStatusDate.getDay();
                holder.statusTimeStamp.setText(date);

                if(stat.getAttachment() != null)
                {
                    Log.i(Global.INFO, "status" + stat.getText() + "\n file path is " + stat.getAttachment().getFilePath());
                    if(stat.getAttachment().getFilePath() != null &&
                            stat.getAttachment().getFilePath().length() > 0)
                    {
                        Picasso.get().load(stat.getAttachment().getFilePath()).into(holder.statusImage);
                        holder.statusImage.setVisibility(View.VISIBLE);
                    }
                }
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
                        " mention " + str);

                final int in = start;
                final IRegistrationPresenter existenceChecker = new RegistrationPresenter();

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("USER_NAME", str);
                        intent.putExtra("LOGGED_USER", mUserAlias);
                        if(existenceChecker.checkUserCompleted(str))
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
//                mFollowersPresenter = new FollowersPresenter(mUserAlias);

                holder.name.setText(mFollowersPresenter.getFollowers().getFollowers().get(position).getFirstName() + " "
                        + mFollowersPresenter.getFollowers().getFollowers().get(position).getLastName());
                holder.alias.setText("@" + mFollowersPresenter.getFollowers().getFollowers().get(position).getUserAlias());
                //TODO set profile picture
            }
            else if (mFeedType.equalsIgnoreCase("FOLLOWING"))
            {
//                mFollowingPresenter = new FollowingPresenter(mUserAlias);

                holder.name.setText(mFollowingPresenter.getFollowing().getFollowing().get(position).getFirstName() + " "
                        + mFollowingPresenter.getFollowing().getFollowing().get(position).getLastName());
                holder.alias.setText("@" + mFollowingPresenter.getFollowing().getFollowing().get(position).getUserAlias());
                //TODO set profile picture
            }
        }


        try
        {
            Log.i(Global.INFO, "holder copy to string " + mLocalHolder.toString());
        }
        catch(Exception e)
        {
            Log.e(Global.ERROR, "failed to access holder copy", e);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if(mFeedType.equalsIgnoreCase("FEED"))
        {
//            if(mFeedPresenter == null)
//                mFeedPresenter = new FeedPresenter();

            return mFeedPresenter.getStatuses(mUserAlias).size();
        }
        else if(mFeedType.equalsIgnoreCase("STORY"))
        {
//            if(mStoryPresenter == null)
//                mStoryPresenter = new StoryPresenter();

            return mStoryPresenter.getStatuses(mUserAlias).size();
        }
        else if(mFeedType.equalsIgnoreCase("FOLLOWERS"))
        {
//            if(mFollowersPresenter == null)
//                mFollowersPresenter = new FollowersPresenter(mUserAlias);

            return mFollowersPresenter.getFollowers().getFollowers().size();
        }
        else if(mFeedType.equalsIgnoreCase("FOLLOWING"))
        {
//            if(mFollowingPresenter == null)
//                mFollowingPresenter = new FollowingPresenter(mUserAlias);

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

    @Override
    public void goTo(String view)
    {

    }

    @Override
    public void updateField(String field, Object object)
    {

    }

    @Override
    public void postToast(String message)
    {

    }
}
