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
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.utils.longpressmenuoptions.LongPressOptions;
import com.mn2square.videolistingmvp.activity.manager.pojo.VideoListInfo;
import com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.Views.SavedListViewImpl;
import com.mn2square.videolistingmvp.activity.presenter.VideoListingActivity;

import java.util.List;

/**
 * Created by nitinagarwal on 3/13/17.
 */

public class SavedListFragmentImpl extends Fragment implements SavedListFragment{

    SavedListViewImpl mSavedListViewImpl;
    VideoListInfo mVideoListInfo;

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
        mSavedListViewImpl.getSavedListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedVideo = mVideoListInfo.getVideosList().get(i);
                Toast.makeText(getActivity(), selectedVideo + "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mSavedListViewImpl.getSavedListView().setScrollViewCallbacks(new ObservableScrollViewCallbacks() {

            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                ((VideoListingActivity)getActivity()).onScrollChanged(scrollY, firstScroll, dragging);
            }

            @Override
            public void onDownMotionEvent() {
                ((VideoListingActivity)getActivity()).onDownMotionEvent();
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                ((VideoListingActivity)getActivity()).onUpOrCancelMotionEvent(scrollState);
            }
        });
    }

    @Override
    public void bindVideoList(List<String> videoList, VideoListInfo videoListInfo) {
        mVideoListInfo = videoListInfo;
        mSavedListViewImpl.bindSavedVideoList(videoList, videoListInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint())
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.long_press_menu_share:
                    String selectedVideoShare = mVideoListInfo.getSavedVideoList().get(info.position);
                    LongPressOptions.shareFile(getActivity(), selectedVideoShare);

                    return true;
                case R.id.long_press_menu_delete:
                    String selectedVideoDelete = mVideoListInfo.getSavedVideoList().get(info.position);
                    int deleteVideoId = mVideoListInfo.getVideoIdHashMap().get(selectedVideoDelete);
                    LongPressOptions.deleteFile(getActivity(), selectedVideoDelete, deleteVideoId);

                    return true;
                case R.id.long_press_menu_rename:
                    String selectedVideoRename = mVideoListInfo.getSavedVideoList().get(info.position);
                    String selectedVideoTitleWithExtension = mVideoListInfo.getVideoTitleHashMap().get(selectedVideoRename);
                    int index = selectedVideoTitleWithExtension.lastIndexOf('.');
                    String selectedVideoTitleForRename;
                    String extensionValue;
                    if (index > 0) {
                        selectedVideoTitleForRename = selectedVideoTitleWithExtension.substring(0, index);
                        extensionValue = selectedVideoTitleWithExtension.substring(index, selectedVideoTitleWithExtension.length());
                    } else {
                        selectedVideoTitleForRename = selectedVideoTitleWithExtension;
                        extensionValue = "";
                    }

                    int renameVideoId = mVideoListInfo.getVideoIdHashMap().get(selectedVideoRename);
                    LongPressOptions.renameFile(getActivity(), selectedVideoTitleForRename, selectedVideoRename,
                            extensionValue, renameVideoId);

                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
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
