package com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.presenter;

import com.mn2square.videolistingmvp.activity.manager.pojo.VideoListInfo;

import java.util.List;

/**
 * Created by nitinagarwal on 3/13/17.
 */

public interface SavedListFragment {
    void bindVideoList(List<String> videoList, VideoListInfo videoListInfo);
}
