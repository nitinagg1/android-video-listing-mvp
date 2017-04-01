package com.mn2square.videolistingmvp.swipetabfragments.ListFragement.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.swipetabfragments.adapters.VideoListAdapter;
import com.mn2square.videolistingmvp.activity.model.VideoListInfo;

import java.util.List;

/**
 * Created by nitinagarwal on 3/12/17.
 */

public class ListFragmentViewImpl implements ListFragmentView {

    View mFragemntVideoListView;
    VideoListAdapter mVideoListAdapter;
    ObservableListView mListView;
    public ListFragmentViewImpl(Context context, ViewGroup container, LayoutInflater inflater)
    {
        mFragemntVideoListView = inflater.inflate(R.layout.tab_videolist, container, false);
        mVideoListAdapter = new VideoListAdapter(context, R.layout.tab_child);
        mListView = (ObservableListView) mFragemntVideoListView.findViewById(R.id.ListView);
        mListView.setAdapter(mVideoListAdapter);
    }

    @Override
    public View getView() {
        return mFragemntVideoListView;
    }

    @Override
    public ObservableListView getListView() {
        return mListView;
    }

    public void bindVideoList(List<String> videos, VideoListInfo videoListInfo)
    {
        mVideoListAdapter.bindVideoList(videos, videoListInfo);
        mVideoListAdapter.notifyDataSetChanged();
    }
}
