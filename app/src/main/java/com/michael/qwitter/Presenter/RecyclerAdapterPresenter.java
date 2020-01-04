package com.michael.qwitter.Presenter;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Presenter.PresenterInterfaces.IRecyclerAdapterPresenter;
import com.michael.qwitter.Presenter.PresenterInterfaces.IRegistrationPresenter;
import com.michael.qwitter.Presenter.PresenterInterfaces.RelationPresenter;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.Utils.StatusParser;
import com.michael.qwitter.View.ProfileActivity;
import com.michael.qwitter.View.RecyclerAdapter;
import com.michael.qwitter.View.SearchActivity;
import com.michael.qwitter.View.ViewInterfaces.IView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class RecyclerAdapterPresenter implements IRecyclerAdapterPresenter
{
    private StatusPresenter mFeedPresenter;
    private StatusPresenter mStoryPresenter;
    private StatusPresenter mSearchPresenter;
    private RelationPresenter mFollowersPresenter;
    private RelationPresenter mFollowingPresenter;
    private String mUserAlias;

    public RecyclerAdapterPresenter(IView adapterView, String username)
    {
        mUserAlias = username;
        mFeedPresenter = new FeedPresenter(adapterView);
        mStoryPresenter = new StoryPresenter(adapterView);
        mFollowersPresenter = new FollowersPresenter(mUserAlias, adapterView);
        mFollowingPresenter = new FollowingPresenter(mUserAlias, adapterView);
        mSearchPresenter = new SearchPresenter();
    }

    public RecyclerAdapterPresenter(IView adapterView, String username, String query)
    {
        this(adapterView, username);
        mSearchPresenter = new SearchPresenter(query, adapterView);
    }


    @Override
    public void handleStatusClick(Context context, int position, String type)
    {
        if(type.equalsIgnoreCase(Global.FEED))
        {
            mFeedPresenter.handleStatusClick(context, position);
        }
        else if(type.equalsIgnoreCase(Global.STORY))
        {
            mStoryPresenter.handleStatusClick(context, position);
        }
    }

    @Override
    public void update(String type)
    {
        if (type.equalsIgnoreCase(Global.FEED))
            mFeedPresenter.update(mUserAlias);
        else if (type.equalsIgnoreCase(Global.STORY))
            mStoryPresenter.update(mUserAlias);
        else if (type.equalsIgnoreCase(Global.FOLLOWERS))
            mFollowersPresenter.update(mUserAlias);
        else if (type.equalsIgnoreCase(Global.FOLLOWING))
            mFollowingPresenter.update(mUserAlias);
        else if (type.equalsIgnoreCase("SEARCH"))
            mSearchPresenter.update("");
    }


    @Override
    public int listSize(String type)
    {
        if(type.equalsIgnoreCase(Global.FEED))
        {
            return mFeedPresenter.getStatuses(mUserAlias).size();
        }
        else if(type.equalsIgnoreCase(Global.STORY))
        {
            return mStoryPresenter.getStatuses(mUserAlias).size();
        }
        else if(type.equalsIgnoreCase(Global.FOLLOWERS))
        {

            return mFollowersPresenter.getFollowers().getFollowers().size();
        }
        else if(type.equalsIgnoreCase(Global.FOLLOWING))
        {

            return mFollowingPresenter.getFollowing().getFollowing().size();
        }

        else if(type.equalsIgnoreCase("SEARCH"))
        {
            return mSearchPresenter.getStatuses("").size();
        }

        return 0;
    }

    @Override
    public void bind(final String type, final RecyclerAdapter.RecyclerHolder holder, final int position, final Context context)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(type.equalsIgnoreCase(Global.FEED))
                {
                    bindStatusToFeed(holder, position, context);
                    linkifyStatus(holder, position, context);
                }
                else if(type.equalsIgnoreCase(Global.STORY))
                {
                    bindStatusToStory(holder, position, context);
                    linkifyStatus(holder, position, context);
                }
                else if(type.equalsIgnoreCase("SEARCH"))
                {
                    bindStatusToSearchFeed(holder, position, context);
                    linkifyStatus(holder, position, context);
                }

            }
        });

    }

    private void bindStatusToFeed(final RecyclerAdapter.RecyclerHolder holder, final int position, final Context context)
    {
        Status stat = mFeedPresenter.getStatuses(mUserAlias).get(position);
        holder.statusText.setText(mFeedPresenter.getStatuses(mUserAlias).get(position).getText());
        holder.alias.setText("@" + mFeedPresenter.getUserAlias(position));
        holder.name.setText(mFeedPresenter.getUserFullName());
        String date =stat.getTimestamp().split("-")[0];
        holder.statusTimeStamp.setText(date);
        Glide.with(context).
                load(mFeedPresenter.getUserProfilePic(position)).
                diskCacheStrategy(DiskCacheStrategy.NONE).
                skipMemoryCache(true).
                into(holder.profilePicture);

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
            else if (stat.getAttachment().format().equalsIgnoreCase("video"))
            {
                ViewGroup.LayoutParams param = holder.layoutView.getLayoutParams();
                // Changes the height and width to the specified *pixels*
                param.height = 700;
                holder.layoutView.setLayoutParams(param);
                final String link = stat.getAttachment().getFilePath();

                try {
                    // Start the MediaController
                    MediaController mediacontroller = new MediaController(context);
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

    private void bindStatusToStory(final RecyclerAdapter.RecyclerHolder holder, final int position, final Context context)
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

        Glide.with(context).
                load(mStoryPresenter.getUserProfilePic(position)).
                diskCacheStrategy(DiskCacheStrategy.NONE).
                skipMemoryCache(true).
                into(holder.profilePicture);

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
                    MediaController mediacontroller = new MediaController(context);
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

    private void bindStatusToSearchFeed(final RecyclerAdapter.RecyclerHolder holder, final int position, final Context context)
    {
        Status stat = mSearchPresenter.getStatuses(mUserAlias).get(position);
        holder.statusText.setText(mSearchPresenter.getStatuses("").get(position).getText());
        holder.alias.setText("@" + mSearchPresenter.getStatus(position).getOwner());
        holder.name.setText(mSearchPresenter.getNameAt(position));
//                mStatusDate = mSearchPresenter.getStatus(position).getTimePosted();
//                mStatusMonth = Month.values()[mStatusDate.getMonth()];
        String date = mSearchPresenter.getStatus(position).getTimestamp().split("-")[0];
        holder.statusTimeStamp.setText(date);
        Glide.with(context).
                load(mSearchPresenter.getUserProfilePic(position)).
                diskCacheStrategy(DiskCacheStrategy.NONE).
                skipMemoryCache(true).
                into(holder.profilePicture);


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
            else if (stat.getAttachment().format().equalsIgnoreCase("video"))
            {
                ViewGroup.LayoutParams param = holder.layoutView.getLayoutParams();
                // Changes the height and width to the specified *pixels*
                param.height = 700;
                holder.layoutView.setLayoutParams(param);
                final String link = stat.getAttachment().getFilePath();

                try {
                    // Start the MediaController
                    MediaController mediacontroller = new MediaController(context);
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

    private void linkifyStatus(final RecyclerAdapter.RecyclerHolder holder, final int position, final Context context)
    {
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
                    Intent intent = new Intent(context, SearchActivity.class);
                    intent.putExtra("USER_NAME", mUserAlias);
                    intent.putExtra("TYPE", "SEARCH");
                    intent.putExtra("QUERY", str);
                    context.startActivity(intent);
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
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("USER_NAME", str);
                    intent.putExtra("LOGGED_USER", mUserAlias);
                    if(existenceChecker.checkUserCompleted(str))
                    {
                        context.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context, "Nothing to show, user " + str + " does not exist", Toast.LENGTH_SHORT).show();
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



}
