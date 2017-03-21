package com.shanjingtech.secumchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.shanjingtech.secumchat.log.AddTimePairedFactory;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.util.Constants;

import javax.inject.Inject;

/**
 * Supers all Secum activities, handles dagger injection and permissions
 */

public class SecumBaseActivity
        extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        DialogInterface.OnClickListener {
    @Inject
    protected SecumAPI secumAPI;
    @Inject
    protected Answers answers;
    @Inject
    AddTimePairedFactory addTimePairedFactory;

    private static final String PERMISSION_TAG = "SecumPermission";
    private AlertDialog permissionAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SecumApplication) getApplication()).getNetComponet().inject(this);
        Resources resources = getResources();
        permissionAlertDialog = new AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.permission_dialog_header))
                .setMessage(resources.getString(R.string.permission_dialog_message))
                .setPositiveButton(resources.getString(R.string.to_settings), this)
                .setNegativeButton(resources.getString(R.string.cancel), this)
                .setIcon(R.mipmap.ic_launcher)
                .create();
    }

    /**
     * Check if we need to explicitly request for permissions
     */
    private boolean needToRequestPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Request permissions SecumChat requires.
     * Camera and Microphone
     *
     * @return success or not - true to go ahead
     */
    protected boolean requestSecumPermissions() {
        // 你说前半生就这样吧还有明天
        if (needToRequestPermission()) {
            return requestPermission(
                    android.Manifest.permission.CAMERA, Constants.PERMISSION_CAMERA) &&
                    requestPermission(android.Manifest.permission.RECORD_AUDIO, Constants
                            .PERMISSION_CAMERARECORD_AUDIO);
        } else {
            return true;
        }
    }

    /**
     * Request the permission if we don't have it yet.
     *
     * @param permission to request
     * @param code       to tag a permission
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean requestPermission(String permission, int code) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            boolean shouldRequestPermission = ActivityCompat
                    .shouldShowRequestPermissionRationale(this, permission);
            Log.d(PERMISSION_TAG, "shouldRequestPermission: " + shouldRequestPermission);

            if (shouldRequestPermission) {
                showBlockingPermissionDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, code);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Show a dialog to direct user to app settings
     */
    private void showBlockingPermissionDialog() {
        if (!permissionAlertDialog.isShowing()) {
            permissionAlertDialog.show();
        }
    }

    /**
     * Bring user to app settings to enable the permission
     */
    private void gotToSettings() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + this.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        this.startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_CAMERA: {
                // user denied camera permission
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showBlockingPermissionDialog();
                }
                break;
            }
            case Constants.PERMISSION_CAMERARECORD_AUDIO: {
                // user denied audio permission
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showBlockingPermissionDialog();
                }
                break;
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            Log.d(PERMISSION_TAG, "You clicked settings");
            gotToSettings();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            Log.d(PERMISSION_TAG, "You clicked cancel");
        }
    }
}
