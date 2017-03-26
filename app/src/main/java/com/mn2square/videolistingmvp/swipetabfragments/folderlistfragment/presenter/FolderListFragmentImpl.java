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
import android.widget.Toast;

import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.activity.presenter.VideoListingActivity;
import com.mn2square.videolistingmvp.utils.longpressmenuoptions.LongPressOptions;
import com.mn2square.videolistingmvp.activity.model.VideoListInfo;
import com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.views.FolderListFragmentViewImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nitinagarwal on 3/15/17.
 */

public class FolderListFragmentImpl extends Fragment implements FolderListFragment{

    FolderListFragmentViewImpl mFolderListFragmentView;
    VideoListInfo mVideoListInfo;
    ArrayList<String> mFolderNames;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFolderListFragmentView = new FolderListFragmentViewImpl(getActivity(), container, inflater);
        return mFolderListFragmentView.getView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((VideoListingActivity)getActivity()).registerListener(this);
        ((VideoListingActivity)getActivity()).fetchFolderList();
        registerForContextMenu(mFolderListFragmentView.getExpandableListView());
        mFolderListFragmentView.getExpandableListView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selectedVideo = mVideoListInfo.getFolderListHashMap().get(mFolderNames.get(i)).get(i1);
                Toast.makeText(getActivity(), selectedVideo + "clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void bindVideoList(HashMap<String, List<String>> folderListHaspMap, VideoListInfo videoListInfo) {
        mVideoListInfo = videoListInfo;
        mFolderNames = new ArrayList<>();
        mFolderNames.addAll(folderListHaspMap.keySet());

        Collections.sort(mFolderNames, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if(lhs.lastIndexOf('/') > 0 && rhs.lastIndexOf('/') >0)
                {
                    String lhsString = lhs.substring(lhs.lastIndexOf('/') + 1);
                    String rhsString = rhs.substring(rhs.lastIndexOf('/') + 1);
                    return lhsString.compareToIgnoreCase(rhsString);
                    //this case we need the comparison to be ignore case
                } else {
                    return -1;
                }
            }
        });

        mFolderListFragmentView.bindVideoList(folderListHaspMap, mFolderNames, videoListInfo);
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

            switch (item.getItemId()) {
                case R.id.long_press_menu_share:
                    LongPressOptions.shareFile(getActivity(), selectedVideo);
                    return true;

                case R.id.long_press_menu_delete:
                    int deleteVideoId = mVideoListInfo.getVideoIdHashMap().get(selectedVideo);
                    LongPressOptions.deleteFile(getActivity(), selectedVideo, deleteVideoId);
                    return true;

                case R.id.long_press_menu_rename:
                    String selectedVideoTitleWithExtension = mVideoListInfo.getVideoTitleHashMap().get(selectedVideo);
                    int index = selectedVideoTitleWithExtension.lastIndexOf('.');
                    final String selectedVideoTitleForRename;
                    final String extensionValue;
                    if (index > 0) {
                        selectedVideoTitleForRename = selectedVideoTitleWithExtension.substring(0, index);
                        extensionValue = selectedVideoTitleWithExtension.substring(index, selectedVideoTitleWithExtension.length());
                    } else {
                        selectedVideoTitleForRename = selectedVideoTitleWithExtension;
                        extensionValue = "";
                    }

                    int renameVideoId = mVideoListInfo.getVideoIdHashMap().get(selectedVideo);
                    LongPressOptions.renameFile(getActivity(), selectedVideoTitleForRename, selectedVideo,
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

}
