/*****************************************************************************
 * BitmapUtil.java
 *****************************************************************************
 * Copyright © 2011-2014 VLC authors and VideoLAN
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package com.mn2square.videolistingmvp.utils.thumbnailutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtil {
    public final static String TAG = "VLC/Util/BitmapUtil";

    public static Bitmap scaleDownBitmap(Context context, Bitmap bitmap, int width) {
        if (bitmap != null) {

            try {
                final float densityMultiplier = context.getResources().getDisplayMetrics().density;
                int bitmapHeight = bitmap.getHeight();
                int bitmapWidth = bitmap.getWidth();
                int w, h, x, y;
                if (bitmapHeight > (bitmapWidth * 3) / 4) {
                    w = bitmapWidth;
                    h = (bitmapWidth * 3) / 4;
                    x = 0;
                    y = (bitmapHeight - h) / 2;
                } else if (bitmapWidth > (bitmapHeight * 4) / 3) {
                    h = bitmapHeight;
                    w = (bitmapHeight * 4) / 3;
                    y = 0;
                    x = (bitmapWidth - w) / 2;
                } else {
                    w = bitmapWidth;
                    h = bitmapHeight;
                    x = 0;
                    y = 0;
                }

                bitmap = Bitmap.createBitmap(bitmap, x, y, w, h);
                int dstWidth = (int) (width * densityMultiplier);
                int dstHeight = (dstWidth * 3) / 4;
                if (w > dstWidth || h > dstHeight) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false);
                }
            }
            catch (Exception ex)
            {
                //under some situation the the value of h becomes <= zero in createbitmap, adding a try catch and returning the original bitmap
                return bitmap;
            }
        }
        return bitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        if(bitmap == null)
            return null;
        Bitmap output;
        try {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);

            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        }
        catch(Exception ex)
        {

            //adding as a precaution because finding a few crashes in bitmap modification
            return bitmap;
        }
        return output;
    }
}
