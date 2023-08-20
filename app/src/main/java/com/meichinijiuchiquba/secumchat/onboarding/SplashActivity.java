package com.meichinijiuchiquba.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.meichinijiuchiquba.secumchat.ConversationPreviewActivity;
import com.meichinijiuchiquba.secumchat.R;
import com.meichinijiuchiquba.secumchat.SecumBaseActivity;
import com.meichinijiuchiquba.secumchat.model.PingResponse;
import com.meichinijiuchiquba.secumchat.model.User;
import com.meichinijiuchiquba.secumchat.model.UserNew;
import com.meichinijiuchiquba.secumchat.util.APIUtils;
import com.meichinijiuchiquba.secumchat.util.SecumDebug;

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
        setContentView(R.layout.splash_activity);
        // use existing oauth token to ping server, if it's valid, go to chat,
        // otherwise go onboarding
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (SecumDebug.isDebugMode(sharedPreferences)) {

            startOnboarding();
        } else {

            secumAPI.ping()
                    .enqueue(new Callback<PingResponse>() {
                        @Override
                        public void onResponse(Call<PingResponse> call, Response<PingResponse>
                                response) {
                            if (response.isSuccessful()) {
                                Log.d("BGLM", "ping success");
                                startSecum();
                            } else {
                                Log.d("BGLM", "ping fail");
                                startOnboarding();
                            }
                        }

                        @Override
                        public void onFailure(Call<PingResponse> call, Throwable t) {

                            Log.d("BGLM", "ping not aval");
                            startOnboarding();
                        }
                    });
        }

    }


    private void startSecum() {
        secumAPI.getProfile().enqueue(new Callback<UserNew>() {
            @Override
            public void onResponse(Call<UserNew> call, Response<UserNew> response) {
                Log.d("BGLM", "getProfile success: " + response);
                if (response.isSuccessful()) {
                    User user = response.body().userInfo;

                    APIUtils.loadBotChats(secumAPI, new APIUtils.APICallback() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashActivity.this, ConversationPreviewActivity.class);
                            currentUserProvider.setUser(user);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure() {
                            showToast(getResources().getString(R.string.something_went_wrong));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserNew> call, Throwable t) {
                showToast(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

    private void startOnboarding() {
        startActivity(new Intent(SplashActivity.this, PhoneNumActivity.class));
    }
}
