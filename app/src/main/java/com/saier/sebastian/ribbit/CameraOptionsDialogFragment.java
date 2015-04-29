package com.saier.sebastian.ribbit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sebastian on 29.04.2015.
 */
public class CameraOptionsDialogFragment extends DialogFragment { // Refactor this code -> class + dialog fragment?

    public static final int TAKE_PICTURE_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int CHOOSE_PICTURE_REQUEST = 2;
    public static final int CHOOSE_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    public static Uri mMediaUri; // Uniform Resource Identifier - path to a specific file in our Android system

    private DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            if (mMediaUri == null) {
                                // display an error
                                Toast.makeText(getActivity(), "There was a problem accessing your device's external storage.", Toast.LENGTH_LONG).show();
                            }
                            else {
                                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                getActivity().startActivityForResult(takePhotoIntent, TAKE_PICTURE_REQUEST); // a request code is the second parameter
                            }
                            break;
                        case 1:
                            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                            if (mMediaUri == null) {
                                // display an error
                                Toast.makeText(getActivity(), "There was a problem accessing your device's external storage.", Toast.LENGTH_LONG).show();
                            }
                            else {
                                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                getActivity().startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                            }
                            break;
                        case 2:
                            Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePhotoIntent.setType("image/*");
                            getActivity().startActivityForResult(choosePhotoIntent, CHOOSE_PICTURE_REQUEST);
                            break;
                        case 3:
                            break;
                    }
                }

                private Uri getOutputMediaFileUri(int mediaType) {
                    // To be safe, you should check that the SDCard is mounted
                    // using Environment.getExternalStorageState() before doing this.
                    if (isExternalStorageAvailable()) {
                        // get the URI

                        // 1. Get external storage directory
                        String appName = getActivity().getString(R.string.app_name);
                        File mediaStorageDir = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);
                        // 2. Create our subdirectory
                        if (! mediaStorageDir.exists()) {
                            if (! mediaStorageDir.mkdirs()) {
                                Log.e("CameraOptionsDialog", "Failed to create directory"); // define tag
                                return null;
                            }
                        }
                        // 3. Create a file name

                        // 4. Create the file
                        File mediaFile;
                        Date now = new Date();
                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now); // Compare to approach of Stormy

                        String path = mediaStorageDir.getPath() + File.separator;
                        if (mediaType == MEDIA_TYPE_IMAGE) {
                            mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
                        }
                        else if (mediaType == MEDIA_TYPE_VIDEO) {
                            mediaFile = new File(path + "VID_" + timestamp + ".mp4");
                        }
                        else {
                            return null;
                        }

                        Log.d("CameraOptionsDialog", "File: " + Uri.fromFile(mediaFile));

                        // 5. Return the file's URI
                        return Uri.fromFile(mediaFile);
                    } else {
                        return null;
                    }
                }

                private boolean isExternalStorageAvailable() {
                    String state = Environment.getExternalStorageState();

                    if (state.equals(Environment.MEDIA_MOUNTED)) { // equals is the string comparator
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            };

    public static final CameraOptionsDialogFragment newInstance(String[] cameraOptions) {
        CameraOptionsDialogFragment adf = new CameraOptionsDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putStringArray("cameraOptions", cameraOptions);
        adf.setArguments(bundle);
        return adf;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] optionsArray = getArguments().getStringArray("cameraOptions");

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Hey bro, what do you want to do?")
                .setItems(optionsArray, mDialogListener)
                .setPositiveButton("OK", null); // null to just close the window

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
