package com.michael.qwitter.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.michael.qwitter.Presenter.PresenterInterfaces.IRegistrationPresenter;
import com.michael.qwitter.Presenter.RegistrationPresenter;
import com.michael.qwitter.Presenter.SoloStatusPresenter;
import com.michael.qwitter.R;
import com.michael.qwitter.Utils.Month;
import com.michael.qwitter.Utils.StatusParser;

import java.util.ArrayList;
import java.util.Date;

public class SoloStatusActivity extends AppCompatActivity
{
    private String mTextString;
    private String mUserAliasString;
    private String mUserNameString;
    private Date mStatusDate;
    private Drawable mStatusImage;
    private SoloStatusPresenter mSoloPresenter;

    private TextView mAlias;
    private TextView mName;
    private TextView mStatusTimeStamp;
    private TextView mStatusText;
    private ImageView mProfilePicture;
    private ImageView mStatusImageView;
    private VideoView mStatusVideo;
    private LinearLayout mStatusContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_view);

        mStatusContainer = findViewById(R.id.status_container);

        Bundle extras = getIntent().getExtras();
        mTextString = extras.getString("TEXT");
        mUserAliasString = extras.getString("USER_NAME");
        mUserNameString = extras.getString("FULL_NAME");
        mStatusDate = (Date) extras.get("DATE");
        try
        {
            Bitmap bitmap  = (Bitmap) extras.get("IMG");
            mStatusImage = new BitmapDrawable(bitmap);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            mStatusImage = null;
        }

        mAlias = findViewById(R.id.status_user_alias);
        mAlias.setText(mUserAliasString);

        mName = findViewById(R.id.status_name);
        mName.setText(mUserNameString);


        mStatusTimeStamp = findViewById(R.id.status_timestamp);
        String date = Month.values()[mStatusDate.getMonth()] + " " + mStatusDate.getDay();
        mStatusTimeStamp.setText(date);

        mStatusText = findViewById(R.id.status_text);
        mStatusText.setText(mTextString);

        mSoloPresenter = new SoloStatusPresenter(mUserAliasString, mStatusDate, mTextString);
        mStatusImageView = findViewById(R.id.status_image);

        if(mSoloPresenter.hasImage())
        {
            System.out.println("setting image in solo activity");
            //will grab attachment info from presenter
            mStatusImageView.setVisibility(View.VISIBLE);
            mStatusImageView.setBackgroundColor(Color.BLACK);
            Drawable d = getResources().getDrawable(R.drawable.new_icon);
            mStatusImageView.setImageDrawable(d);
            mStatusImageView.setMinimumHeight(300);
            mStatusImageView.setMinimumWidth(300);
        }

        //TODO option to set video


        Linkify.addLinks(mStatusText, Linkify.WEB_URLS);

        CharSequence sequence = mStatusText.getText();
        SpannableString strBuilder = new SpannableString(sequence);
        ArrayList<Integer> hashtagIndices = StatusParser.parseForHashTags(mStatusText.getText().toString());
        ArrayList<Integer> mentionIndices = StatusParser.parseForMentions(mStatusText.getText().toString());


        System.out.println("hashtagIndices in " + mStatusText.getText().toString() + " are " +
                hashtagIndices.toString());

        for(int j = 0; j < hashtagIndices.size(); j+=2)
        {
            final int start = hashtagIndices.get(j);
            final int end = hashtagIndices.get(j + 1);
            final String str = mStatusText.getText().toString().substring(start, end + 1);
            System.out.println("start " + start + " , end " + end +
                    " tag " + mStatusText.getText().toString().substring(start, end + 1));

            final int in = start;

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent intent = new Intent(SoloStatusActivity.this, SearchActivity.class);
                    intent.putExtra("USER_NAME", mUserAliasString);
                    intent.putExtra("TYPE", "SEARCH");
                    intent.putExtra("QUERY", str);
                    startActivity(intent);
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
                notFinal = mStatusText.getText().toString().substring(start + 1, end + 1);
            else
                notFinal = "";

            final String str = notFinal;

            System.out.println("start " + start + " , end " + end +
                    " mention " + mStatusText.getText().toString().substring(start, end + 1));

            final int in = start;
            final IRegistrationPresenter existenceChecker = new RegistrationPresenter();

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent intent = new Intent(SoloStatusActivity.this, ProfileActivity.class);
                    intent.putExtra("USER_NAME", str);
                    intent.putExtra("LOGGED_USER", mUserAliasString);
                    if(existenceChecker.checkUserCompleted(str))
                    {
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(SoloStatusActivity.this, "Nothing to show, user " + str + " does not exist", Toast.LENGTH_SHORT).show();
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

        mStatusText.setText(strBuilder);
        mStatusText.setMovementMethod(LinkMovementMethod.getInstance());
        mStatusText.setHighlightColor(Color.TRANSPARENT);


    }
}
