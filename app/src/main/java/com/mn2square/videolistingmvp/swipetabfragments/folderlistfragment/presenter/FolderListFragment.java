package com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.presenter;

import com.mn2square.videolistingmvp.activity.model.VideoListInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nitinagarwal on 3/15/17.
 */

public interface FolderListFragment {
    void bindVideoList(HashMap<String, List<String>> folderListHaspMap, VideoListInfo videoListInfo);
}
