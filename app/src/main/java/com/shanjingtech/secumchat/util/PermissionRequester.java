package com.shanjingtech.secumchat.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by flamearrow on 2/27/17.
 */

public class PermissionRequester implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Activity activity;

    public PermissionRequester(Activity activity) {
        this.activity = activity;
    }

    public static boolean needToRequestPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Request for camera and microphone permission
     */
    public void requestPermissions() {
        if (needToRequestPermission()) {
            requestPermission(Manifest.permission.CAMERA, Constants.PERMISSION_CAMERA);
            requestPermission(Manifest.permission.RECORD_AUDIO, Constants
                    .PERMISSION_CAMERARECORD_AUDIO);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission(String permission, int code) {
        if (ContextCompat.checkSelfPermission(activity, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // activity thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

//            } else {
            // No explanation needed, we can request the permission.
            activity.requestPermissions(new String[]{permission}, code);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pass
                } else {
                    activity.finish();
                }
                return;
            }
            case Constants.PERMISSION_CAMERARECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    activity.finish();
                }
                return;
            }
            default:
                // do nothing
        }
    }
}
