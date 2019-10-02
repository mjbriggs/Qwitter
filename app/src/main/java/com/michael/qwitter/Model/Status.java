package com.michael.qwitter.Model;

import androidx.annotation.NonNull;

import java.sql.Time;

public class Status
{
    private String mText;
    //TODO add hashtag collection
    //TODO add mentions collection
    //TODO add URLs collection
    private Attachment mAttachment;
    private Time mTimePosted;

    public Status(String text)
    {
        mText = text;
    }

    public String getText()
    {
        return mText;
    }

    public void setText(String mText)
    {
        this.mText = mText;
    }

    public Attachment getAttachment()
    {
        return mAttachment;
    }

    public void setAttachment(Attachment mAttachment)
    {
        this.mAttachment = mAttachment;
    }

    public Time getTimePosted()
    {
        return mTimePosted;
    }

    public void setTimePosted(Time mTimePosted)
    {
        this.mTimePosted = mTimePosted;
    }

    @NonNull
    @Override
    public String toString()
    {
        return mText;
    }
}
