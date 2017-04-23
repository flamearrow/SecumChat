package com.shanjingtech.secumchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.SecumDebug;

/**
 * Legacy login activity, direct access to SecumChat and debug api
 */

public class LoginActivity extends SecumBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SecumDebug.enableDebugMode(sharedPreferences);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean useUser11;

    public void button0(View view) {
//        Log.d("MLGB", countryCode());
    }

    private String countryCode() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        return countryCode;
    }

    public void button1(View view) {
        useUser11 = true;
        requestCameraAudioPermissions();
    }

    private void logInAs11() {
        // login as phone_11
        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_11);
        User user = new User();
        user.setUsername("phone_11");
        // accesstoken: "vUXSNoPy3XLb3oh51zrhrYqQoDaVGd"
        Intent intent = new Intent(this, SecumChatActivity.class);
        intent.putExtra(Constants.CURRENT_USER, user);
        startActivity(intent);
    }

    public void button2(View view) {
        useUser11 = false;
        requestCameraAudioPermissions();
    }

    private void logInAs22() {
        // login as phone_22
        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_22);
        User user = new User();
        user.setUsername("phone_22");
        // accesstoken: "zhvG2zIf4xXFzzFfTJjnfOycXTjBZn"
        Intent intent = new Intent(this, SecumChatActivity.class);
        intent.putExtra(Constants.CURRENT_USER, user);
        startActivity(intent);
    }

    @Override
    protected void onAudioCameraPermissionGranted() {
        if (useUser11) {
            logInAs11();
        } else {
            logInAs22();
        }
    }

    /**
     * TODO udpate this
     *
     * @return
     */
    String getPhoneNumber() {
        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService
                (Context.TELEPHONY_SERVICE);
        String s = manager.getNetworkCountryIso();
        return manager.getLine1Number();
    }

}
