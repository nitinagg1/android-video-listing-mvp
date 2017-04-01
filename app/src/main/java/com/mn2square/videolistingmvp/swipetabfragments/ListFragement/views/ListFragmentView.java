package com.mn2square.videolistingmvp.swipetabfragments.ListFragement.views;

import android.view.View;
import android.widget.ListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;

/**
 * Created by nitinagarwal on 3/12/17.
 */

public interface ListFragmentView {

    View getView();
    ObservableListView getListView();
}
