package com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.Views;

import android.view.View;
import android.widget.ListView;

import com.mn2square.videolistingmvp.activity.model.VideoListInfo;

import java.util.List;

/**
 * Created by nitinagarwal on 3/13/17.
 */

public interface SavedListView {
    View getView();
    void bindSavedVideoList(List<String> savedVideoList, VideoListInfo videoListInfo);
    ListView getSavedListView();
}
