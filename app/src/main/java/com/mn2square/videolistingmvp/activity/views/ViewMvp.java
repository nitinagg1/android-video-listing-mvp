package com.mn2square.videolistingmvp.activity.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by nitinagarwal on 3/5/17.
 */

public interface ViewMvp {

    View getRootView();

    ViewPager getViewPager();

    Bundle getViewState();
}
