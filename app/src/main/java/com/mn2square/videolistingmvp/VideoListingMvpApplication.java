package com.mn2square.videolistingmvp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by nitinagarwal on 3/19/17.
 */

public class VideoListingMvpApplication extends Application{

    private static VideoListingMvpApplication instance;

@Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    }


    /**
     * @return the main context of the Application
     */
    public static Context getAppContext()
    {
        return instance;
    }

    /**
     * @return the main resources from the Application
     */
    public static Resources getAppResources()
    {
        return instance.getResources();
    }
}
