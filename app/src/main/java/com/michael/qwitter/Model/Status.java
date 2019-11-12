package com.michael.qwitter.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.michael.qwitter.Model.ModelInterfaces.IAttachment;
import com.michael.qwitter.Utils.StatusParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Status
{
    private String mOwner;
    private String mOwnerName;
    private String mText;
    private List<Hashtag> mHashTags;
    //TODO add hashtag collection
    //TODO add mentions collection
    //TODO add URLs collection
    private IAttachment mAttachment;
    private Date mTimePosted;

    public Status(String text)
    {
        mText = text;
        mTimePosted = new Date();
        mHashTags = new ArrayList<>();
        mOwner = "";
        mOwnerName = "";
    }

    public Status(String text, String owner, String ownerName)
    {
        mOwnerName = ownerName;
        mHashTags = new ArrayList<>();
        mOwner = owner;
        System.out.println("new status owner is " + owner + ", name is " + ownerName);
        mText = text;
        mTimePosted = new Date();

        this.findHashTags();
        System.out.println("tags for status " + mHashTags.toString());
    }

    public String getText()
    {
        return mText;
    }

    public void setText(String mText)
    {
        this.mText = mText;
        this.findHashTags();
    }

    public IAttachment getAttachment()
    {
        return mAttachment;
    }

    public void setAttachment(IAttachment mAttachment)
    {
        this.mAttachment = mAttachment;
    }

    public Date getTimePosted()
    {
        return mTimePosted;
    }

    public void setTimePosted(Date mTimePosted)
    {
        this.mTimePosted = mTimePosted;
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

    public void setHashTags(List<Hashtag> mHashTags)
    {
        this.mHashTags = mHashTags;
    }

    public void findHashTags()
    {
        int start;
        int end;
        String tagStr;
        ArrayList<Integer> tagLocations = StatusParser.parseForHashTags(mText);
        for(int i = 0; i < tagLocations.size(); i += 2)
        {
            start = tagLocations.get(i);
            end = tagLocations.get(i + 1);
            tagStr = mText.substring(start, end + 1);
            Hashtag tag = new Hashtag(tagStr);
            mHashTags.add(tag);
        }
    }

    public List<Hashtag> getHashTags()
    {
        return mHashTags;
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
        if(this.mAttachment != null)
            status.setAttachment(this.mAttachment);
        status.setOwnerName(this.mOwnerName);
        status.setOwner(this.mOwner);

        return status;
    }
}
