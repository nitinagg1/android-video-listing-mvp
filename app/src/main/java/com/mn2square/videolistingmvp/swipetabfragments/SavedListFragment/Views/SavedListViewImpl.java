package com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.activity.model.VideoListInfo;
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
    public View getView() {
        return mSavedVideoListView;
    }

    @Override
    public void bindSavedVideoList(List<String> savedVideosList, VideoListInfo videoListInfo) {

        mSavedListAdapter.bindVideoList(savedVideosList, videoListInfo);
        mSavedListAdapter.notifyDataSetChanged();
    }

    @Override
    public ObservableListView getSavedListView() {
        return mSavedListView;
    }
}
