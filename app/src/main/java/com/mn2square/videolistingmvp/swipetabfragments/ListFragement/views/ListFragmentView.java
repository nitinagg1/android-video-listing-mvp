package com.mn2square.videolistingmvp.swipetabfragments.ListFragement.views;

import android.view.View;
import android.widget.ListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.mn2square.videolistingmvp.activity.manager.pojo.VideoListInfo;
import com.mn2square.videolistingmvp.viewmvp.ViewMvp;

import java.util.List;

/**
 * Created by nitinagarwal on 3/12/17.
 */

public interface ListFragmentView extends ViewMvp {

    void bindVideoList(List<String> videoList, VideoListInfo videoListInfo);
    ObservableListView getListView();
}
