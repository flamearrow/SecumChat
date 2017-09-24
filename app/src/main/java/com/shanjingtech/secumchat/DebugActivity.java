package com.shanjingtech.secumchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shanjingtech.secumchat.contacts.ContactsActivity;
import com.shanjingtech.secumchat.pushy.PushyInitializer;
import com.shanjingtech.secumchat.ui.DialingReceivingWaitingLayout;
import com.shanjingtech.secumchat.ui.HeartMagicLayout;
import com.shanjingtech.secumchat.ui.HeartSecumCounter;
import com.shanjingtech.secumchat.ui.SecumCounter;
import com.shanjingtech.secumchat.util.SecumDebug;

import javax.inject.Inject;

public class DebugActivity extends SecumBaseActivity implements SecumCounter
        .SecumCounterListener, PushyInitializer.PushyInitializedCallback {
    private final static String TAG = "DebugActivity";
    HeartSecumCounter heartSecumCounter;
    HeartMagicLayout heart;
    DialingReceivingWaitingLayout dialingReceivingWaitingLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_activity);

        // always use phone11's token
        SecumDebug.enableDebugMode(sharedPreferences);
        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_11);

        dialingReceivingWaitingLayout = (DialingReceivingWaitingLayout) findViewById(R.id.drw);
        heartSecumCounter = (HeartSecumCounter) findViewById(R.id.chronometer);
//        catHead = (PulseImageView) findViewById(R.id.cat_head);
        heart = (HeartMagicLayout) findViewById(R.id.heart);

        // To be used in splash
        initializePushy();

    }

    private void initializePushy() {
        pushyInitializer.setPushyInitializedCallback(this);
//        pushyInitializer.initializePushy();
    }

    public void clickHeart(View view) {
        heart.meLike();
    }

    public void b1(View view) {
        pushyInitializer.initializePushy();
//        heartSecumCounter.explode();
//        secumCounter.initialize();
//        heart.switchState(PairLikeImageView.LikeState.ME_LIKE);
//        secumCounter.initialize();
//        catHead.startPulse();
//        dialingReceivingWaitingLayout.switchUIState(SecumChatActivity.State.DIALING);
//        secumCounter.meAdd();
    }

    public void b2(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
//        heart.switchState(PairLikeImageView.LikeState.PEER_LIKE);
//        heart.peerLike();
//        secumCounter.peerAdd();
//        secumCounter.shake();
//        catHead.stopPulse();
//        secumCounter.zoom();
//        secumCounter.meAdd();
//        dialingReceivingWaitingLayout.switchUIState(SecumChatActivity.State.WAITING);
//        secumCounter.peerAdd();
    }

    public void b3(View view) {
//        heart.switchState(HeartMagicLayout.LikeState.NO_LIKE);
//        secumCounter.peerAdd();
//        secumCounter.freeze();
//        heart.meLike();
//        secumCounter.meAdd();
//        secumCounter.initialize();
    }

    public void b4(View view) {
//        heart.switchState(HeartMagicLayout.LikeState.BOTH_LIKE);
//        secumCounter.bounce();
//        secumCounter.freeze();
//        secumCounter.explode();
    }

    @Override
    public void onCounterStart() {
        Log.d(TAG, "onCounterStart");

    }

    @Override
    public void onCounterStop() {
        Log.d(TAG, "onCounterStop");
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

    public void acceptChat(View view) {
        Log.d(TAG, "acceptChat");
    }

    public void rejectChat(View view) {
        Log.d(TAG, "rejectChat");
    }

    @Override
    public void onPushyInitialized() {
        Toast.makeText(getApplicationContext(), "onPushyInitialized", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onPushyInitializeFailed() {
        Toast.makeText(getApplicationContext(), "onPushyInitialized", Toast.LENGTH_SHORT)
                .show();
    }
}
