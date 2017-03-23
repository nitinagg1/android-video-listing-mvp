package com.mn2square.videolistingmvp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

/**
 * Created by lenovo on 9/28/2014.
 */
public class Converters {
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
    public static int SlowMoFactorConverter(int slowMoConstant)
    {
        slowMoConstant=slowMoConstant-150;
        int y=slowMoConstant/50;
        return y+2;

    }

    public static String BytesToMb(String bytes)
    {
        String size;
        Double bytesInDouble = Double.parseDouble(bytes);
        Double kb = bytesInDouble/1024.0;
        Double mb = kb/1024.0;
        Double gb = kb/1048576.0;

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( gb>1 ) {
            size = dec.format(gb).concat(" GB");
        } else if ( mb>1 ) {
            size = dec.format(mb).concat(" MB");
        } else {
            size = dec.format(kb).concat(" KB");
        }
        return size;
    }
}
