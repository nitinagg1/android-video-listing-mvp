package com.mn2square.videolistingmvp.utils.thumbnailutils;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;


import com.mn2square.videolistingmvp.VideoListingMvpApplication;

import java.lang.ref.WeakReference;

/**
 * Created by nitinagarwal on 10/29/15.
 */
public class ThumbnailCreateor {




    public static class BitmapWorkerTask extends AsyncTask<String,Void,Bitmap>
    {
        private long data;
        private String videoData;
        private WeakReference<ImageView> imageViewReference;
        private ContentResolver cr;
        private String url;
        public BitmapWorkerTask(ImageView view, ContentResolver cr, String url)
        {
            imageViewReference = new WeakReference<ImageView>(view);
            this.cr = cr;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String...videoId) {
            this.data = Long.parseLong(videoId[0]);
            this.videoData = videoId[1];
            Bitmap found = BitmapCache.GetInstance().getBitmapFromDiskCache(videoData);
            if (found != null)
                return found;
            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, this.data, MediaStore.Video.Thumbnails.MINI_KIND,null);
            bitmap = BitmapUtil.scaleDownBitmap(VideoListingMvpApplication.getAppContext(),bitmap,105);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            BitmapCache.GetInstance().AddBitmapToCache(url, bm);
            if (imageViewReference != null)
            {
                ImageView imageView = imageViewReference.get();
                if (imageView != null)
                {
                    BitmapWorkerTask bitmapDownloaderTask = getBitmapWorkerTask(imageView);
                    if (this == bitmapDownloaderTask) {
                        imageView.setImageBitmap(bm);
                    }
                }
            }
        }

    }

    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }


    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }


    public static boolean cancelPotentialWork(long data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final long bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

}
