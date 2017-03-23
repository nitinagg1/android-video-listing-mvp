package com.mn2square.videolistingmvp.activity.model;

/**
 * Created by nitinagarwal on 3/7/17.
 */

public interface VideoListManager {

    interface VideoListManagerListener {
        void onVideoListUpdate(VideoListInfo videoListInfo);
    }

    void getVideosWithNewSorting(int sortType);
    void registerListener(VideoListManagerListener videoListManagerListener);

    void UnRegisterListener();
}
