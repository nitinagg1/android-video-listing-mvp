package com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.views;

import android.view.View;
import android.widget.ExpandableListView;

import com.mn2square.videolistingmvp.activity.manager.pojo.VideoListInfo;
import com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.ObservableFolderList.ObservableExpandableListView;
import com.mn2square.videolistingmvp.viewmvp.ViewMvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nitinagarwal on 3/15/17.
 */

public interface FolderListFragmentView extends ViewMvp{
    ObservableExpandableListView getExpandableListView();
    void bindVideoList(HashMap<String, List<String>> folderListHashMap, ArrayList<String> folderNames, VideoListInfo videoListInfo);
}
