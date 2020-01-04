package com.michael.qwitter.Presenter;

import android.content.Context;
import android.content.Intent;

import com.michael.qwitter.GatewayFacade.Accessor;
import com.michael.qwitter.GatewayFacade.IAccessor;
import com.michael.qwitter.Model.ModelInterfaces.IAttachment;
import com.michael.qwitter.Model.Status;
import com.michael.qwitter.Model.User;
import com.michael.qwitter.Presenter.PresenterInterfaces.StatusPresenter;
import com.michael.qwitter.Utils.Global;
import com.michael.qwitter.Utils.PageTracker;
import com.michael.qwitter.View.SoloStatusActivity;
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
    private String mUserAlias;

    public StoryPresenter()
    {
        PageTracker.getInstance().setStoryLastKey(0);
        mUserFullName = " ";
        mAccessor = new Accessor();
        mStoryList = new ArrayList<>();
        mProfileLinks = new ArrayList<>();
        mUserAlias = "";
    }

    public StoryPresenter(IView storyView)
    {
        this();
        mStoryView = storyView;
    }
    public StoryPresenter(IView storyView, String username)
    {
        this(storyView);
        mUserAlias = username;
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

    @Override
    public Intent getIntent()
    {
        return null;
    }

    @Override
    public void handleStatusClick(Context context, int position)
    {
        Intent intent = new Intent(context, SoloStatusActivity.class);
        Status status = this.mStoryList.get(position);
        intent.putExtra("TEXT", status.getText());
        intent.putExtra("USER_NAME", this.mUserAlias);
        intent.putExtra("FULL_NAME", this.getUserFullName());
        intent.putExtra("DATE", status.getTimestamp().split("-")[0]);
        intent.putExtra("profilePicture", this.getUserProfilePic(position));

        IAttachment att = status.getAttachment();
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

        final Context fContext = context;
        final Intent fIntent = intent;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                fContext.startActivity(fIntent);
            }
        });
    }
}
