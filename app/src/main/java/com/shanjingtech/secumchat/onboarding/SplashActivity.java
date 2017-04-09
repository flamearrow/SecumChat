package com.shanjingtech.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.SecumChatActivity;
import com.shanjingtech.secumchat.model.PingResponse;
import com.shanjingtech.secumchat.model.User;
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
                } else {
                    startOnboarding();
                }
            }

            @Override
            public void onFailure(Call<PingResponse> call, Throwable t) {
                startOnboarding();
            }
        });
    }

    private void startSecum() {
        secumAPI.getProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // TODO: pass in User
                    User user = response.body();
                    Intent intent = new Intent(SplashActivity.this, SecumChatActivity.class);
                    intent.putExtra(Constants.CURRENT_USER, user);
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void startOnboarding() {
        Intent intent = new Intent(SplashActivity.this, PhoneNumActivity.class);
        startActivity(intent);
    }
}
