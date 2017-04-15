package com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.activity.presenter.VideoUserInteraction;
import com.mn2square.videolistingmvp.swipetabfragments.VideoListFragmentInterface.VideoListFragmentInterface;
import com.mn2square.videolistingmvp.activity.presenter.manager.pojo.VideoListInfo;
import com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.Views.SavedListViewImpl;
import com.mn2square.videolistingmvp.activity.presenter.VideoListingActivity;

/**
 * Created by nitinagarwal on 3/13/17.
 */

public class SavedListFragmentImpl extends Fragment implements VideoListFragmentInterface {

    SavedListViewImpl mSavedListViewImpl;
    VideoListInfo mVideoListInfo;
    VideoUserInteraction mCallback;
    ObservableScrollViewCallbacks mObservableScrollViewCallbacks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mSavedListViewImpl = new SavedListViewImpl(getActivity(), container, inflater);
        mSavedListViewImpl.getSavedListView().addHeaderView(inflater.inflate(R.layout.padding, mSavedListViewImpl.getSavedListView(), false));

        return mSavedListViewImpl.getRootView();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((VideoListingActivity)getActivity()).registerListener(this);
        ((VideoListingActivity)getActivity()).fetchSavedList();
        registerForContextMenu(mSavedListViewImpl.getSavedListView());

        try
        {
            mCallback = ((VideoListingActivity)getActivity());
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement VideoUserInteraction");
        }

        try
        {
            mObservableScrollViewCallbacks = ((VideoListingActivity) getActivity()).getVideoListActivityView();

        }
        catch (ClassCastException ex)
        {
            throw new ClassCastException("videolistingactivityview must implement ObservalbleScrollViewCallbacks");
        }
        mSavedListViewImpl.getSavedListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedVideo = mVideoListInfo.getSavedVideoList().get(i - 1); // adjust for padding
                mCallback.onVideoSelected(selectedVideo);
            }
        });

        mSavedListViewImpl.getSavedListView().setScrollViewCallbacks(new ObservableScrollViewCallbacks() {

            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                mObservableScrollViewCallbacks.onScrollChanged(scrollY, firstScroll, dragging);
            }

            @Override
            public void onDownMotionEvent() {
                mObservableScrollViewCallbacks.onDownMotionEvent();
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                mObservableScrollViewCallbacks.onUpOrCancelMotionEvent(scrollState);
            }

        });
    }

    @Override
    public void bindVideoList(VideoListInfo videoListInfo) {
        mVideoListInfo = videoListInfo;
        mSavedListViewImpl.bindSavedVideoList(videoListInfo.getSavedVideoList(), videoListInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint())
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String selectedVideo = mVideoListInfo.getSavedVideoList().get(info.position - 1);
            mCallback.onVideoLongPressed(selectedVideo, item.getItemId());
            return true;
        }
        return false;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String selectedVideo = mVideoListInfo.getSavedVideoList().get(info.position);
        menu.setHeaderTitle(selectedVideo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_video_long_press, menu);

    }

}
