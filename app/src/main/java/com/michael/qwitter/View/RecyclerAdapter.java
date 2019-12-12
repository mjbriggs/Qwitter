package com.michael.qwitter.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.michael.qwitter.Model.ModelInterfaces.IAttachment;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
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
    private IView mFragmentView;

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
    public RecyclerAdapter(String username, String type, Context context, IView fragment)
    {
        mFeedPresenter = null;
        mFollowersPresenter = null;
        mStoryPresenter = null;
        mFollowingPresenter = null;
        mUserAlias = username;
        mFeedType = type;
        mContext = context;
        mQuery = "";

        mFragmentView = fragment;

        mFeedPresenter = new FeedPresenter(this);
        mStoryPresenter = new StoryPresenter(this);
        mFollowersPresenter = new FollowersPresenter(mUserAlias, this);
        mFollowingPresenter = new FollowingPresenter(mUserAlias, this);
        mSearchPresenter = new SearchPresenter();

    }

    public RecyclerAdapter(String username, String query, String type, Context context, IView fragment)
    {
        mFeedPresenter = null;
        mFollowersPresenter = null;
        mStoryPresenter = null;
        mFollowingPresenter = null;
        mUserAlias = username;
        mFeedType = type;
        mContext = context;
        mQuery = query;

        mFragmentView = fragment;

        mFeedPresenter = new FeedPresenter(this);
        mStoryPresenter = new StoryPresenter(this);
        mFollowersPresenter = new FollowersPresenter(mUserAlias, this);
        mFollowingPresenter = new FollowingPresenter(mUserAlias, this);
        mSearchPresenter = new SearchPresenter(mQuery, this);
        System.out.println("In adapter query is " + mQuery + " and type is " + mFeedType);
    }

    public void update()
    {
        mFragmentView.updateField("starting", null);
        if (mFeedType.equalsIgnoreCase(Global.FEED))
            mFeedPresenter.update(mUserAlias);
        else if (mFeedType.equalsIgnoreCase(Global.STORY))
            mStoryPresenter.update(mUserAlias);
        else if (mFeedType.equalsIgnoreCase(Global.FOLLOWERS))
            mFollowersPresenter.update(mUserAlias);
        else if (mFeedType.equalsIgnoreCase(Global.FOLLOWING))
            mFollowingPresenter.update(mUserAlias);
        else if (mFeedType.equalsIgnoreCase("SEARCH"))
            mSearchPresenter.update("");
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
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
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
            holder.profilePicture = holder.layoutView.findViewById(R.id.status_profile_picture);
            holder.statusVideo = holder.layoutView.findViewById(R.id.status_video);

            ViewGroup.LayoutParams params = holder.layoutView.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 400;
            holder.layoutView.setLayoutParams(params);

            holder.statusContainer.setOnClickListener(new View.OnClickListener()
            {
                //TODO handle if status has video
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
                        intent.putExtra("DATE", mFeedPresenter.getStatuses(mUserAlias).get(position).getTimestamp().split("-")[0]);
                        intent.putExtra("profilePicture", mFeedPresenter.getUserProfilePic(position));
                        IAttachment att = mFeedPresenter.getStatuses(mUserAlias).get(position).getAttachment();
                        if (att != null)
                        {
                            intent.putExtra("attachment", att.getFilePath());
                            intent.putExtra("type", att.format());
                            if (att.format().equalsIgnoreCase("video"))
                                return;
                        }
                        else
                        {
                            intent.putExtra("attachment", "none");
                            intent.putExtra("type", "none");
                        }

                        mContext.startActivity(intent);
                    }
                    else if(mFeedType.equalsIgnoreCase("STORY"))
                    {
//                        mStoryPresenter = new StoryPresenter();
                        Intent intent = new Intent(mContext, SoloStatusActivity.class);
                        intent.putExtra("TEXT", mStoryPresenter.getStatuses(mUserAlias).get(position).getText());
                        intent.putExtra("USER_NAME", mUserAlias);
                        intent.putExtra("FULL_NAME", mStoryPresenter.getUserFullName());
                        intent.putExtra("DATE", mStoryPresenter.getStatuses(mUserAlias).get(position).getTimestamp().split("-")[0]);
                        intent.putExtra("profilePicture", mStoryPresenter.getUserProfilePic(position));

                        IAttachment att = mStoryPresenter.getStatuses(mUserAlias).get(position).getAttachment();
                        if (att != null)
                        {
                            intent.putExtra("attachment", att.getFilePath());
                            intent.putExtra("type", att.format());
                            if (att.format().equalsIgnoreCase("video"))
                                return;
                        }
                        else
                        {
                            intent.putExtra("attachment", "none");
                            intent.putExtra("type", "none");
                        }

                        mContext.startActivity(intent);
                    }
                }
            });

            if(mFeedType.equalsIgnoreCase("FEED"))
            {
//                mFeedPresenter = new FeedPresenter();   //should base these on username not logged in username
                Status stat = mFeedPresenter.getStatuses(mUserAlias).get(position);
                holder.statusText.setText(mFeedPresenter.getStatuses(mUserAlias).get(position).getText());
                holder.alias.setText("@" + mFeedPresenter.getUserAlias(position));
                holder.name.setText(mFeedPresenter.getUserFullName());
//                mStatusDate = mFeedPresenter.getStatuses(mUserAlias).get(position).getTimePosted();
//                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date =stat.getTimestamp().split("-")[0];
                holder.statusTimeStamp.setText(date);
                Picasso.get().invalidate(mFeedPresenter.getUserProfilePic(position));
                Picasso.get().load(mFeedPresenter.getUserProfilePic(position))
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                        .into(holder.profilePicture);

                if(stat.getAttachment() != null)
                {
                    Log.i(Global.INFO, "status" + stat.getText() + "\n file path is " + stat.getAttachment().getFilePath());
                    if(stat.getAttachment().getFilePath() != null &&
                    stat.getAttachment().getFilePath().length() > 0 &&
                            stat.getAttachment().format().equalsIgnoreCase("image"))
                    {
                        ViewGroup.LayoutParams param = holder.layoutView.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        param.height = 700;
                        holder.layoutView.setLayoutParams(param);

                        Picasso.get().load(stat.getAttachment().getFilePath()).into(holder.statusImage);
                        holder.statusImage.setVisibility(View.VISIBLE);
                        holder.statusImage.setMinimumHeight(300);
                        holder.statusImage.setMinimumWidth(300);
                    }
                    else if ( stat.getAttachment().format().equalsIgnoreCase("video"))
                    {
                        ViewGroup.LayoutParams param = holder.layoutView.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        param.height = 700;
                        holder.layoutView.setLayoutParams(param);
                        final String link = stat.getAttachment().getFilePath();

                        try {
                            // Start the MediaController
                            MediaController mediacontroller = new MediaController(mContext);
                            mediacontroller.setAnchorView(holder.statusVideo);
                            // Get the URL from String videoUrl
                            Uri video = Uri.parse(link);
                            holder.statusVideo.setMediaController(mediacontroller);
                            holder.statusVideo.setVideoURI(video);

                        } catch (Exception e) {
                            Log.e("Error", e.getMessage(), e);
                        }

                        holder.statusVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mp) {
                                holder.statusVideo.start();
                            }
                        });

                        holder.statusVideo.setVisibility(View.VISIBLE);
                        holder.statusVideo.setMinimumHeight(300);
                        holder.statusVideo.setMinimumWidth(300);

                        holder.statusContainer.setClickable(false);
                    }
                }

            }
            else if(mFeedType.equalsIgnoreCase("STORY"))
            {
//                mStoryPresenter = new StoryPresenter();
                Status stat = mStoryPresenter.getStatuses(mUserAlias).get(position);
                holder.statusText.setText(mStoryPresenter.getStatuses(mUserAlias).get(position).getText());
                holder.alias.setText("@" + mUserAlias);
                holder.name.setText(mStoryPresenter.getUserFullName());
//                mStatusDate = mStoryPresenter.getStatuses(mUserAlias).get(position).getTimePosted();
//                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date = stat.getTimestamp().split("-")[0];
                holder.statusTimeStamp.setText(date);
                Picasso.get().invalidate(mStoryPresenter.getUserProfilePic(position));
                Picasso.get().load(mStoryPresenter.getUserProfilePic(position))
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                        .into(holder.profilePicture);

                if(stat.getAttachment() != null)
                {
                    Log.i(Global.INFO, "status" + stat.getText() + "\n file path is " + stat.getAttachment().getFilePath());
                    if(stat.getAttachment().getFilePath() != null &&
                            stat.getAttachment().getFilePath().length() > 0  &&
                            stat.getAttachment().format().equalsIgnoreCase("image"))
                    {
                        ViewGroup.LayoutParams param = holder.layoutView.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        param.height = 700;
                        holder.layoutView.setLayoutParams(param);

                        Picasso.get().load(stat.getAttachment().getFilePath()).into(holder.statusImage);
                        holder.statusImage.setVisibility(View.VISIBLE);
                        holder.statusImage.setMinimumHeight(300);
                        holder.statusImage.setMinimumWidth(300);
                    }
                    else if ( stat.getAttachment().format().equalsIgnoreCase("video"))
                    {
                        ViewGroup.LayoutParams param = holder.layoutView.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        param.height = 700;
                        holder.layoutView.setLayoutParams(param);
                        final String link = stat.getAttachment().getFilePath();

                        try {
                            // Start the MediaController
                            MediaController mediacontroller = new MediaController(mContext);
                            mediacontroller.setAnchorView(holder.statusVideo);
                            // Get the URL from String videoUrl
                            Uri video = Uri.parse(link);
                            holder.statusVideo.setMediaController(mediacontroller);
                            holder.statusVideo.setVideoURI(video);

                        } catch (Exception e) {
                            Log.e("Error", e.getMessage(), e);
                        }

                        holder.statusVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mp) {
                                holder.statusVideo.start();
                            }
                        });

                        holder.statusVideo.setVisibility(View.VISIBLE);
                        holder.statusVideo.setMinimumHeight(300);
                        holder.statusVideo.setMinimumWidth(300);
                    }
                }
            }

            else if(mFeedType.equalsIgnoreCase("SEARCH"))
            {
                System.out.println("Search feed query is " + mQuery);
                Status stat = mSearchPresenter.getStatuses(mUserAlias).get(position);
                holder.statusText.setText(mSearchPresenter.getStatuses("").get(position).getText());
                holder.alias.setText("@" + mSearchPresenter.getStatus(position).getOwner());
                holder.name.setText(mSearchPresenter.getNameAt(position));
//                mStatusDate = mSearchPresenter.getStatus(position).getTimePosted();
//                mStatusMonth = Month.values()[mStatusDate.getMonth()];
                String date = mSearchPresenter.getStatus(position).getTimestamp().split("-")[0];
                holder.statusTimeStamp.setText(date);
                Picasso.get().invalidate(mSearchPresenter.getUserProfilePic(position));
                Picasso.get().load(mSearchPresenter.getUserProfilePic(position))
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                        .into(holder.profilePicture);


                if(stat.getAttachment() != null)
                {
                    Log.i(Global.INFO, "status" + stat.getText() + "\n file path is " + stat.getAttachment().getFilePath());
                    if(stat.getAttachment().getFilePath() != null &&
                            stat.getAttachment().getFilePath().length() > 0 &&
                            stat.getAttachment().format().equalsIgnoreCase("image"))
                    {
                        ViewGroup.LayoutParams param = holder.layoutView.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        param.height = 700;
                        holder.layoutView.setLayoutParams(param);

                        Picasso.get().load(stat.getAttachment().getFilePath()).into(holder.statusImage);
                        holder.statusImage.setVisibility(View.VISIBLE);
                        holder.statusImage.setMinimumHeight(300);
                        holder.statusImage.setMinimumWidth(300);
                    }
                    else if ( stat.getAttachment().format().equalsIgnoreCase("video"))
                    {
                        ViewGroup.LayoutParams param = holder.layoutView.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        param.height = 700;
                        holder.layoutView.setLayoutParams(param);
                        final String link = stat.getAttachment().getFilePath();

                        try {
                            // Start the MediaController
                            MediaController mediacontroller = new MediaController(mContext);
                            mediacontroller.setAnchorView(holder.statusVideo);
                            // Get the URL from String videoUrl
                            Uri video = Uri.parse(link);
                            holder.statusVideo.setMediaController(mediacontroller);
                            holder.statusVideo.setVideoURI(video);

                        } catch (Exception e) {
                            Log.e("Error", e.getMessage(), e);
                        }

                        holder.statusVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mp) {
                                holder.statusVideo.start();
                            }
                        });

                        holder.statusVideo.setVisibility(View.VISIBLE);
                        holder.statusVideo.setMinimumHeight(300);
                        holder.statusVideo.setMinimumWidth(300);

                        holder.statusContainer.setClickable(false);
                    }
                }
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
            holder.profilePicture = holder.layoutView.findViewById(R.id.follow_profile_picture);

            if(mFeedType.equalsIgnoreCase("FOLLOWERS"))
            {
//                mFollowersPresenter = new FollowersPresenter(mUserAlias);

                holder.name.setText(mFollowersPresenter.getFollowers().getFollowers().get(position).getFirstName() + " "
                        + mFollowersPresenter.getFollowers().getFollowers().get(position).getLastName());
                holder.alias.setText("@" + mFollowersPresenter.getFollowers().getFollowers().get(position).getUserAlias());

                String url = mFollowersPresenter.getFollowers().getFollowers().get(position).getProfilePicture().getFilePath();
                if (url != null)
                {
                    Picasso.get().invalidate(url);
                    Picasso.get().load(url)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                            .into(holder.profilePicture);
                }

//                mFollowersPresenter.getFollowers().getFollowers().get(position).getProfilePicture();

            }
            else if (mFeedType.equalsIgnoreCase("FOLLOWING"))
            {
//                mFollowingPresenter = new FollowingPresenter(mUserAlias);


                holder.name.setText(mFollowingPresenter.getFollowing().getFollowing().get(position).getFirstName() + " "
                        + mFollowingPresenter.getFollowing().getFollowing().get(position).getLastName());
                holder.alias.setText("@" + mFollowingPresenter.getFollowing().getFollowing().get(position).getUserAlias());

                String url = mFollowingPresenter.getFollowing().getFollowing().get(position).getProfilePicture().getFilePath();
                if (url != null)
                {
                    Picasso.get().invalidate(url);
                    Picasso.get().load(url)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                            .into(holder.profilePicture);
                }
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

    public boolean isEmpty()
    {
        if(mFeedType.equalsIgnoreCase(Global.FEED))
        {
            return mFeedPresenter.getStatuses(mUserAlias).size() == 0;
        }
        return true;
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
//            if(mSearchPresenter == null)
//                mSearchPresenter = new SearchPresenter(mQuery);

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
        if (field.equalsIgnoreCase(Global.FEED) || field.equalsIgnoreCase(Global.STORY) ||
                field.equalsIgnoreCase(Global.FOLLOWING) || field.equalsIgnoreCase(Global.FOLLOWERS) ||
                field.equalsIgnoreCase("SEARCH"))
        {
            this.notifyDataSetChanged();
            mFragmentView.updateField("done", null);
        }
    }

    @Override
    public void postToast(String message)
    {

    }
}
