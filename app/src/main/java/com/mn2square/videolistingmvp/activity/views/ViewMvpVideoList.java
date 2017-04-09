package com.mn2square.videolistingmvp.activity.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mn2square.videolistingmvp.viewmvp.ViewMvp;

/**
 * Created by nitinagarwal on 3/5/17.
 */

public interface ViewMvpVideoList extends ViewMvp{
    ViewPager getViewPager();
}
