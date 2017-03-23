package com.mn2square.videolistingmvp.swipetabfragments.ListFragement.presenter;

import com.mn2square.videolistingmvp.activity.model.VideoListInfo;

import java.util.List;

/**
 * Created by nitinagarwal on 3/13/17.
 */

public interface ListFragment {
    void bindVideoList(List<String> videoList, VideoListInfo videoListInfo);
}
