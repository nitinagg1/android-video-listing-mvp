package com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.views;

import android.view.View;
import android.widget.ExpandableListView;

import com.mn2square.videolistingmvp.swipetabfragments.folderlistfragment.ObservableFolderList.ObservableExpandableListView;
import com.mn2square.videolistingmvp.viewmvp.ViewMvp;

/**
 * Created by nitinagarwal on 3/15/17.
 */

public interface FolderListFragmentView extends ViewMvp{
    ObservableExpandableListView getExpandableListView();
}
