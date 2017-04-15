package com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.activity.presenter.VideoListingActivity;
import com.mn2square.videolistingmvp.activity.presenter.VideoUserInteraction;
import com.mn2square.videolistingmvp.swipetabfragments.VideoListFragmentInterface.VideoListFragmentInterface;
import com.mn2square.videolistingmvp.activity.presenter.manager.pojo.VideoListInfo;
import com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.views.FolderListFragmentViewImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by nitinagarwal on 3/15/17.
 */

public class FolderListFragmentImpl extends Fragment implements VideoListFragmentInterface {

    FolderListFragmentViewImpl mFolderListFragmentView;
    VideoListInfo mVideoListInfo;
    ArrayList<String> mFolderNames;
    VideoUserInteraction mCallback;
    ObservableScrollViewCallbacks mObservableScrollViewCallbacks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFolderListFragmentView = new FolderListFragmentViewImpl(getActivity(), container, inflater);
        mFolderListFragmentView.getExpandableListView().addHeaderView(inflater.inflate(R.layout.padding, mFolderListFragmentView.getExpandableListView(), false));
        return mFolderListFragmentView.getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((VideoListingActivity)getActivity()).registerListener(this);
        ((VideoListingActivity)getActivity()).fetchFolderList();
        registerForContextMenu(mFolderListFragmentView.getExpandableListView());
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

        mFolderListFragmentView.getExpandableListView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selectedVideo = mVideoListInfo.getFolderListHashMap().get(mFolderNames.get(i)).get(i1);
                mCallback.onVideoSelected(selectedVideo);
                return false;
            }
        });

        mFolderListFragmentView.getExpandableListView().setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
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
    public boolean onContextItemSelected(MenuItem item) {

        if(getUserVisibleHint()) {
            ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();

            int group =
                    ExpandableListView.getPackedPositionGroup(info.packedPosition);

            int child =
                    ExpandableListView.getPackedPositionChild(info.packedPosition);
            String selectedVideo = mVideoListInfo.getFolderListHashMap().get(mFolderNames.get(group)).get(child);

            mCallback.onVideoLongPressed(selectedVideo, item.getItemId());
            return true;
        }
        return false;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo)menuInfo;
        int type =
                ExpandableListView.getPackedPositionType(info.packedPosition);

        if(type == ExpandableListView.PACKED_POSITION_TYPE_GROUP)
        {
            return;
        }

        int group =
                ExpandableListView.getPackedPositionGroup(info.packedPosition);

        int child =
                ExpandableListView.getPackedPositionChild(info.packedPosition);

        String selectedVideo = mVideoListInfo.getFolderListHashMap().get(mFolderNames.get(group)).get(child);

        menu.setHeaderTitle(selectedVideo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_video_long_press, menu);
    }

    @Override
    public void bindVideoList(VideoListInfo videoListInfo) {
        mVideoListInfo = videoListInfo;
        mFolderNames = new ArrayList<>();
        mFolderNames.addAll(videoListInfo.getFolderListHashMap().keySet());

        Collections.sort(mFolderNames, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if(lhs.lastIndexOf('/') > 0 && rhs.lastIndexOf('/') >0)
                {
                    String lhsString = lhs.substring(lhs.lastIndexOf('/') + 1);
                    String rhsString = rhs.substring(rhs.lastIndexOf('/') + 1);
                    return lhsString.compareToIgnoreCase(rhsString);
                } else {
                    return -1;
                }
            }
        });

        mFolderListFragmentView.bindVideoList(videoListInfo.getFolderListHashMap(), mFolderNames, videoListInfo);
    }
}
