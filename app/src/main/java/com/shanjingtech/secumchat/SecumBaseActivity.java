package com.shanjingtech.secumchat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.injection.CurrentUserProvider;
import com.shanjingtech.secumchat.log.AddTimePairedFactory;
import com.shanjingtech.secumchat.message.CurrentPeerUserNameProvider;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.net.FirebaseImageUploader;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.net.SecumNetDBSynchronizer;
import com.shanjingtech.secumchat.onboarding.SplashActivity;
import com.shanjingtech.secumchat.pushy.PushyInitializer;
import com.shanjingtech.secumchat.util.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    @Inject
    protected SharedPreferences sharedPreferences;
    @Inject
    @Singleton
    protected CurrentUserProvider currentUserProvider;
    @Inject
    PushyInitializer pushyInitializer;
    @Inject
    protected MessageDAO messageDAO;
    @Inject
    protected MessageDAO userDAO;
    // Used to identify if a newly received message should be marked as read or unread.
    @Inject
    protected CurrentPeerUserNameProvider currentPeerNameProvider;
    @Inject
    protected SecumNetDBSynchronizer secumNetDBSynchronizer;

    @Inject
    FirebaseImageUploader firebaseImageUploader;


    private static final String PERMISSION_TAG = "SecumPermission";

    private AlertDialog permissionAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!shouldDeferInjection()) {
            ((SecumApplication) getApplication()).getNetComponet().inject(this);
        }
        Resources resources = getResources();

        permissionAlertDialog = new AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.permission_dialog_header))
                .setPositiveButton(resources.getString(R.string.to_settings), this)
                .setNegativeButton(resources.getString(R.string.cancel), this)
                .setIcon(R.drawable.cat_head)
                .create();
    }

    protected boolean shouldDeferInjection() {
        return false;
    }

    protected void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SecumBaseActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Check if we need to explicitly request for permissions
     */
    private boolean needToRequestPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Sequentially request audio, camera and location permissions, if user already has both or
     * granted both,
     * callback {@link #onAudioCameraLocationPermissionGranted()}, otherwise bring user to
     * settings page.
     */
    protected void requestCameraAudioLocationPermissions() {
        // 你说前半生就这样吧还有明天
        if (needToRequestPermission()) {
            requestAudioPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestAudioPermission() {
        if (hasPermission(android.Manifest.permission.RECORD_AUDIO)) {
            requestCameraPermission();
        } else {
            requestPermission(
                    android.Manifest.permission.RECORD_AUDIO,
                    Constants.PERMISSION_RECORD_AUDIO);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        if (hasPermission(android.Manifest.permission.CAMERA)) {
            requestLocationPermission();
        } else {
            requestPermission(
                    android.Manifest.permission.CAMERA,
                    Constants.PERMISSION_CAMERA);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            onAudioCameraLocationPermissionGranted();
        } else {
            requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Constants.PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Request read phone state permission, if user already has it granted,
     * callback {@link #onPhoneStatePermissionGranted()}, otherwise bring user to settings page.
     */
    protected void requestPhoneStatePermissions() {
        if (needToRequestPermission()) {
            if (hasPermission(android.Manifest.permission.READ_PHONE_STATE)) {
                onPhoneStatePermissionGranted();
            } else {
                requestPermission(
                        Manifest.permission.READ_PHONE_STATE,
                        Constants.PERMISSION_PHONE_STATE);
            }
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
        if (!hasPermission(permission)) {
            boolean shouldRequestPermission = ActivityCompat
                    .shouldShowRequestPermissionRationale(this, permission);
            Log.d(PERMISSION_TAG, "shouldRequestPermission: " + shouldRequestPermission);

            if (shouldRequestPermission) {
                showBlockingPermissionDialog(permission);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, code);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager
                .PERMISSION_GRANTED;
    }

    /**
     * Show a dialog to direct user to app settings
     */
    protected void showBlockingPermissionDialog(String permission) {
        switch (permission) {
            case Manifest.permission.READ_PHONE_STATE:
                if (!permissionAlertDialog.isShowing()) {
                    permissionAlertDialog.setTitle(
                            getResources().getString(R.string
                                    .permission_dialog_message_phone));
                    permissionAlertDialog.show();
                }
                break;
            case Manifest.permission.RECORD_AUDIO:
            case Manifest.permission.CAMERA:
                if (!permissionAlertDialog.isShowing()) {
                    permissionAlertDialog.setTitle(
                            getResources().getString(R.string
                                    .permission_dialog_message_audio_camera));
                    permissionAlertDialog.show();
                }
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (!permissionAlertDialog.isShowing()) {
                    permissionAlertDialog.setTitle(
                            getResources().getString(R.string
                                    .permission_access_fine_location));
                    permissionAlertDialog.show();
                }
                break;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_CAMERA: {
                // user denied camera permission
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showBlockingPermissionDialog(Manifest.permission.CAMERA);
                } else {
                    requestLocationPermission();
                }
                break;
            }
            case Constants.PERMISSION_RECORD_AUDIO: {
                // user denied audio permission
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showBlockingPermissionDialog(Manifest.permission.RECORD_AUDIO);
                } else {
                    requestCameraPermission();
                }
                break;
            }
            case Constants.PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showBlockingPermissionDialog(Manifest.permission.ACCESS_FINE_LOCATION);
                } else {
                    onAudioCameraLocationPermissionGranted();
                }
            }
            case Constants.PERMISSION_PHONE_STATE: {
                // user denied phone state permission
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showBlockingPermissionDialog(Manifest.permission.READ_PHONE_STATE);
                } else {
                    onPhoneStatePermissionGranted();
                }
                break;
            }
        }
    }

    protected void onAudioCameraLocationPermissionGranted() {
    }

    protected void onPhoneStatePermissionGranted() {
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

    protected void logOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREF_ACCESS_TOKEN);
        editor.commit();
        startActivity(new Intent(this, SplashActivity.class));
    }

    /**
     * @return my username
     */
    protected String getMyName() {
        return "MLGB";
//        return currentUserProvider.getUser().getUsername();
    }

    /**
     * @return my {@link User}
     */
    protected User getMyUser() {
        return currentUserProvider.getUser();
    }

}
