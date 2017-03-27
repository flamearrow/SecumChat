package com.shanjingtech.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.SecumChatActivity;
import com.shanjingtech.secumchat.model.PingResponse;
import com.shanjingtech.secumchat.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Splash something
 */

public class SplashActivity extends SecumBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(this, PhoneNumActivity.class);
//        try {
//        finish();
        // use existing oauth token to ping server, if it's valid, go to chat,
        // otherwise go onboarding
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        secumAPI.ping().enqueue(new Callback<PingResponse>() {
            @Override
            public void onResponse(Call<PingResponse> call, Response<PingResponse> response) {
                if (response.isSuccessful()) {
                    startSecum();
                    Log.d("MLGB", "success");
                } else {
                    startOnboarding();
                    Log.d("MLGB", "fail");
                }
            }

            @Override
            public void onFailure(Call<PingResponse> call, Throwable t) {
                Log.d("MLGB", "fail");
                startOnboarding();
            }
        });
    }

    private void startSecum() {
        // TODO: get user details
        Intent intent = new Intent(this, SecumChatActivity.class);
        String age = "23";
        String name = "mlgb";

        intent.putExtra(Constants.MY_NAME, name);
        intent.putExtra(Constants.MY_AGE, age);
        intent.putExtra(Constants.ME_MALE, false);
        startActivity(intent);
    }

    private void startOnboarding() {
        Intent intent = new Intent(SplashActivity.this, PhoneNumActivity.class);
        startActivity(intent);
    }
}
