package com.shanjingtech.secumchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.shanjingtech.secumchat.injection.CurrentUserProvider;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserNew;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.util.SecumDebug;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Legacy login activity, direct access to SecumChat and debug api
 */

public class LegacyLoginActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SecumAPI secumAPI;
    @Inject
    CurrentUserProvider currentUserProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SecumApplication) getApplication()).getNetComponet().inject(this);
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
//        requestCameraAudioLocationPermissions();
        logInAs11();
    }

    private void logInAs11() {
        // login as phone_11
        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_11);
//        User user = new User();
//        user.setUsername("phone_11");
        // accesstoken: "vUXSNoPy3XLb3oh51zrhrYqQoDaVGd"
//        Intent intent = new Intent(this, SecumChatActivity.class);
//        intent.putExtra(Constants.CURRENT_USER, user);
//        startActivity(intent);
        logInAsCurrentuser();

    }

    public void button2(View view) {
        useUser11 = false;
//        requestCameraAudioLocationPermissions();
        logInAs22();
    }

    private void logInAs22() {
        // login as phone_22
        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_22);
//        User user = new User();
//        user.setUsername("phone_22");
//        // accesstoken: "zhvG2zIf4xXFzzFfTJjnfOycXTjBZn"
//        Intent intent = new Intent(this, SecumChatActivity.class);
//        intent.putExtra(Constants.CURRENT_USER, user);
//        startActivity(intent);
        logInAsCurrentuser();
    }

    private void logInAsCurrentuser() {
        secumAPI.getProfile().enqueue(new Callback<UserNew>() {
            @Override
            public void onResponse(Call<UserNew> call, Response<UserNew> response) {
                if (response.isSuccessful()) {
                    Log.d("BGLM", "getProfile success");
                    User user = response.body().userInfo;
                    currentUserProvider.setUser(user);
                    startActivity(new Intent(LegacyLoginActivity.this, SecumChatActivity.class));

                }
            }

            @Override
            public void onFailure(Call<UserNew> call, Throwable t) {
                User fakeUser = new User();

                Log.d("BGLM", "getProfile failure" + t);

                // set some fake values to fakeUser
                fakeUser.setUsername("fakeUser");

                currentUserProvider.setUser(fakeUser);
                startActivity(new Intent(LegacyLoginActivity.this, SecumChatActivity.class));
            }
        });
    }

//    @Override
//    protected void onAudioCameraLocationPermissionGranted() {
//        if (useUser11) {
//            logInAs11();
//        } else {
//            logInAs22();
//        }
//    }

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
