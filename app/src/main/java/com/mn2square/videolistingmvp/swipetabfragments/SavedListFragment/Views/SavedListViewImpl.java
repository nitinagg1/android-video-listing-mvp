package com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.Views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.activity.presenter.manager.pojo.VideoListInfo;
import com.mn2square.videolistingmvp.swipetabfragments.adapters.VideoListAdapter;

import java.util.List;

/**
 * Created by nitinagarwal on 3/13/17.
 */

public class SavedListViewImpl implements SavedListView{

    VideoListAdapter mSavedListAdapter;
    View mSavedVideoListView;
    ObservableListView mSavedListView;

    public SavedListViewImpl(Context context, ViewGroup container, LayoutInflater inflater)
    {
        mSavedVideoListView = inflater.inflate(R.layout.tab_videolist, container, false);
        mSavedListAdapter = new VideoListAdapter(context, R.layout.tab_child);
        mSavedListView = (ObservableListView) mSavedVideoListView.findViewById(R.id.ListView);
        mSavedListView.setAdapter(mSavedListAdapter);
    }

    @Override
    public void bindSavedVideoList(List<String> savedVideoList, VideoListInfo videoListInfo) {
        mSavedListAdapter.bindVideoList(savedVideoList, videoListInfo);
        mSavedListAdapter.notifyDataSetChanged();
    }

    @Override
    public ObservableListView getSavedListView() {
        return mSavedListView;
    }

    @Override
    public View getRootView() {
        return mSavedVideoListView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }
}
