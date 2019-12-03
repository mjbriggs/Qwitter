package com.michael.qwitter.Presenter;

import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.Utils.PageTracker;
import com.michael.qwitter.View.ViewInterfaces.IView;

import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class StoryPresenter implements StatusPresenter
{

    private String mUserFullName;
    private List<Status> mStoryList;
    private IAccessor mAccessor;
    private IView mStoryView;
    private List<String> mProfileLinks;

    public StoryPresenter()
    {
        PageTracker.getInstance().setStoryLastKey(0);
        mUserFullName = " ";
        mAccessor = new Accessor();
        mStoryList = new ArrayList<>();
        mProfileLinks = new ArrayList<>();
    }

    public StoryPresenter(IView storyView)
    {
        this();
        mStoryView = storyView;
    }

    @Override
    public List<Status> getStatuses(String username)
    {
        return mStoryList;
    }

    @Override
    public String getUserFullName()
    {
        return mUserFullName;
    }

    @Override
    public String getUserAlias(int position)
    {
        return "";
    }

    public Status getStatus(int position)
    {
        return null;
    }

    @Override
    public void update(String usernameIn)
    {

        final String username = usernameIn;
        new Thread(new Runnable() {
            public void run() {
                User user = mAccessor.getUserInfo(username);

                mUserFullName = user.getFirstName() + " " + user.getLastName();

                String lk = "";
                if (mStoryList.size() > 0)
                {
                    lk = mStoryList.get(mStoryList.size() - 1).getTimestamp();
                }
//                Log.i(Global.INFO, "story lk before update is " + PageTracker.getInstance().getStoryLastKey());
                List<Status> newStatuses = mAccessor.getStory(username, lk).getStatuses();
//                PageTracker.getInstance().addStoryLastKey(newStatuses.size());
//                Log.i(Global.INFO, "story lk after update is " + PageTracker.getInstance().getStoryLastKey());

                for (Status status: newStatuses)
                {
                  mProfileLinks.add(mAccessor.getUserInfo(username).getProfilePicture().getFilePath());

                }
                mStoryList.addAll(newStatuses);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        mStoryView.updateField(Global.STORY, null);
                    }
                });
            }
        }).start();

    }

    @Override
    public String getUserProfilePic(int pos)
    {
        return mProfileLinks.get(pos);
    }

    @Override
    public String getNameAt(int pos)
    {
        return null;
    }
}
