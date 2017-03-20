package com.shanjingtech.secumchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.shanjingtech.secumchat.onboarding.PhoneNumActivity;

/**
 * Splash something
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, PhoneNumActivity.class);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(intent);
        finish();

    }
}
