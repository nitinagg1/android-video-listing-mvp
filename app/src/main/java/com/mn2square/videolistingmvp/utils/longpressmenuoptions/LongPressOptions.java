package com.mn2square.videolistingmvp.utils.longpressmenuoptions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mn2square.videolistingmvp.R;
import com.mn2square.videolistingmvp.activity.presenter.manager.VideoListUpdateManager;

import java.io.File;

/**
 * Created by nitinagarwal on 3/21/17.
 */

public class LongPressOptions {


    public static void deleteFile(final Context context, final String selectedVideoDelete, final int id,
                                  final VideoListUpdateManager videoListUpdateManager)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("Confirm Delete...");
        alertDialog.setMessage("Are you sure you want to Delete:\n\n" + selectedVideoDelete);
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File fileToDelete = new File(selectedVideoDelete);
                boolean deletedSuccessfully = fileToDelete.delete();
                if (deletedSuccessfully) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                        MediaScannerConnection.scanFile(context,
                                new String[]{selectedVideoDelete}, null, null);

                    } else {
                        context.sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_MOUNTED,
                                Uri.parse("file://"
                                        + Environment.getExternalStorageDirectory())));
                    }
                    videoListUpdateManager.updateForDeleteVideo(id);
                }

            }
        });
        alertDialog.show();
    }

    public static void renameFile(final Context context, final String selectedVideoTitleForRename, final String selectedVideoRenamePath,
                                  final String extensionValue, final int id, final VideoListUpdateManager videoListUpdateManager)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View renameVideoView = li.inflate(R.layout.rename_video, null);
        final EditText input = (EditText)renameVideoView.findViewById(R.id.rename_edit_text);
        input.setText(selectedVideoTitleForRename);

        alert.setView(renameVideoView);
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File fileToRename = new File(selectedVideoRenamePath);
                File fileNameNew = new File(selectedVideoRenamePath.replace(
                        selectedVideoTitleForRename, input.getText().toString()));
                if(fileNameNew.exists())
                {
                    Toast.makeText(context,
                            context.getResources().getString(R.string.same_title_exists), Toast.LENGTH_LONG).show();
                }
                else {

                    String updatedTitle = input.getText().toString() + extensionValue;
                    fileToRename.renameTo(fileNameNew);

                    String newFilePath = fileNameNew.toString();
                    videoListUpdateManager.updateForRenameVideo(id, newFilePath, updatedTitle);
                }}});

        alert.show();

    }

    public static void shareFile(final Context context, final String selectedVideoShare)
    {
        MediaScannerConnection.scanFile(context, new String[] { selectedVideoShare },

                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Intent shareIntent = new Intent(
                                android.content.Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra("VSMP", "https://play.google.com/store/apps/details?id=" + context.getPackageName());
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        context.startActivity(Intent.createChooser(shareIntent,
                                context.getString(R.string.share_text)));

                    }
                });

    }

}
