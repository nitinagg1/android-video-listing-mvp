package com.mn2square.videolistingmvp.swipetabfragments.ListFragement.presenter;

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
import com.mn2square.videolistingmvp.activity.presenter.VideoListingActivity;
import com.mn2square.videolistingmvp.utils.longpressmenuoptions.LongPressOptions;
import com.mn2square.videolistingmvp.swipetabfragments.ListFragement.views.ListFragmentViewImpl;
import com.mn2square.videolistingmvp.activity.manager.pojo.VideoListInfo;

import java.util.List;

/**
 * Created by nitinagarwal on 3/12/17.
 */

public class ListFragmentImpl extends Fragment implements ListFragment{

    ListFragmentViewImpl mListFragmentViewImpl;
    VideoListInfo mVideoListInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mListFragmentViewImpl = new ListFragmentViewImpl(getActivity(), container, inflater);

        mListFragmentViewImpl.getListView().addHeaderView(inflater.inflate(R.layout.padding, mListFragmentViewImpl.getListView(), false));
        return mListFragmentViewImpl.getRootView();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((VideoListingActivity)getActivity()).registerListener(this);
        ((VideoListingActivity)getActivity()).fetchVideoList();
        registerForContextMenu(mListFragmentViewImpl.getListView());

        mListFragmentViewImpl.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedVideo = mVideoListInfo.getVideosList().get(i);
                Toast.makeText(getActivity(), selectedVideo + "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mListFragmentViewImpl.getListView().setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
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
        mListFragmentViewImpl.bindVideoList(videoList, videoListInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(getUserVisibleHint()) {

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.long_press_menu_share:
                    String selectedVideoShare = mVideoListInfo.getVideosList().get(info.position);
                    LongPressOptions.shareFile(getActivity(), selectedVideoShare);

                    return true;
                case R.id.long_press_menu_delete:
                    String selectedVideoDelete = mVideoListInfo.getVideosList().get(info.position);
                    int deleteVideoId = mVideoListInfo.getVideoIdHashMap().get(selectedVideoDelete);
                    LongPressOptions.deleteFile(getActivity(), selectedVideoDelete, deleteVideoId);

                    return true;
                case R.id.long_press_menu_rename:
                    final String selectedVideoRename = mVideoListInfo.getVideosList().get(info.position);
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
        String selectedVideo = mVideoListInfo.getVideosList().get(info.position);
        menu.setHeaderTitle(selectedVideo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_video_long_press, menu);

    }

}
