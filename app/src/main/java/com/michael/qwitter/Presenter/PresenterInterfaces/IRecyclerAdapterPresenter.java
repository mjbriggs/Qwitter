package com.michael.qwitter.Presenter.PresenterInterfaces;

import android.content.Context;

import com.michael.qwitter.View.RecyclerAdapter;

public interface IRecyclerAdapterPresenter
{
    void handleStatusClick(Context context, int pos, String type);
    void update(String type);
    int listSize(String type);
    void bind(String type, RecyclerAdapter.RecyclerHolder holder, int position, Context context);
}
