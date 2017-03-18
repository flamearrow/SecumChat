package com.shanjingtech.secumchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.shanjingtech.secumchat.util.SecumCounter;

public class DebugActivity extends Activity implements SecumCounter.SecumCounterListener {
    private final static String TAG = "DebugActivity";
    SecumCounter secumCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_activity);
        secumCounter = (SecumCounter) findViewById(R.id.chronometer);
        secumCounter.setSecumCounterListener(this);

    }

    public void start(View view) {
//        secumCounter.start();
        secumCounter.initialize();
    }

    public void meAdd(View view) {
        secumCounter.meAdd();
    }

    public void peerAdd(View view) {
        secumCounter.peerAdd();
    }

    @Override
    public void onCounterStart() {
        Log.d(TAG, "onCounterStart");

    }

    @Override
    public void onCounterExpire() {
        Log.d(TAG, "onCounterExpire");

    }

    @Override
    public void onAddTimePaired(int secondsLeft) {
        Log.d(TAG, "onAddTimePaired " + secondsLeft);

    }

    @Override
    public void onMeAdd() {
        Log.d(TAG, "onMeAdd");

    }

    @Override
    public void onPeerAdd() {
        Log.d(TAG, "onPeerAdd");
    }
}
