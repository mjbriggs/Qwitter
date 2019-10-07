package com.michael.qwitter.Model;

import java.util.ArrayList;

public class Story
{
    private ArrayList<Status> mStatuses; //serves as both story and feed at the moment

    public Story()
    {
        mStatuses = new ArrayList<>();
    }
    public Story(ArrayList<Status> mStatuses)
    {
        this.mStatuses = mStatuses;
    }

    public ArrayList<Status> getStatuses()
    {
        return mStatuses;
    }

    public void setStatuses(ArrayList<Status> mStatuses)
    {
        this.mStatuses = mStatuses;
    }

    public void addStatus(Status status)
    {
        mStatuses.add(0, status);
    }
}
