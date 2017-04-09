package com.mn2square.videolistingmvp.swipetabfragments.SavedListFragment.Views;

import android.view.View;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.mn2square.videolistingmvp.activity.manager.pojo.VideoListInfo;
import com.mn2square.videolistingmvp.viewmvp.ViewMvp;

import java.util.List;

/**
 * Created by nitinagarwal on 3/13/17.
 */

public interface SavedListView extends ViewMvp{
    void bindSavedVideoList(List<String> savedVideoList, VideoListInfo videoListInfo);
    ObservableListView getSavedListView();
}
