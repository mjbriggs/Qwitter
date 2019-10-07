package com.michael.qwitter.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public class Status
{
    private String mOwner;
    private String mOwnerName;
    private String mText;
    //TODO add hashtag collection
    //TODO add mentions collection
    //TODO add URLs collection
//    private Attachment mAttachment;
    private Date mTimePosted;

    public Status(String text)
    {
        mText = text;
        mTimePosted = new Date();
//        mAttachment = new Image("");
    }

    public Status(String text, String owner, String ownerName)
    {
        mOwnerName = ownerName;
        mOwner = owner;
        System.out.println("new status owner is " + owner + ", name is " + ownerName);
        mText = text;
        mTimePosted = new Date();
//        mAttachment = new Image("");
    }

    public String getText()
    {
        return mText;
    }

    public void setText(String mText)
    {
        this.mText = mText;
    }

//    public Attachment getAttachment()
//    {
//        return mAttachment;
//    }
//
//    public void setAttachment(Attachment mAttachment)
//    {
//        this.mAttachment = mAttachment;
//    }

    public Date getTimePosted()
    {
        return mTimePosted;
    }

    public void setTimePosted(Date mTimePosted)
    {
        this.mTimePosted = mTimePosted;
    }

    @NonNull
    @Override
    public String toString()
    {
        return mText;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (obj.getClass() != this.getClass())
        {
            return false;
        }

        Status s = (Status) obj;

        if(s.getTimePosted().compareTo(this.mTimePosted) != 0)
            return false;

        if(s.getText().compareTo(this.mText) != 0)
            return false;

//        if(!s.getAttachment().equals(this.mAttachment))
//            return false;

        return true;
    }

    public Status clone(){
        Status status = new Status("");

        status.setText(this.getText());
        status.setTimePosted((Date) this.mTimePosted.clone());
//        status.setAttachment(this.mAttachment);
        status.setOwnerName(this.mOwnerName);
        status.setOwner(this.mOwner);

        return status;
    }

    public String getOwner()
    {
        return mOwner;
    }

    public void setOwner(String mOwner)
    {
        this.mOwner = mOwner;
    }

    public String getOwnerName()
    {
        return mOwnerName;
    }

    public void setOwnerName(String mOwnerName)
    {
        this.mOwnerName = mOwnerName;
    }
}
