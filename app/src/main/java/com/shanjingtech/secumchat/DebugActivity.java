package com.shanjingtech.secumchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.shanjingtech.secumchat.ui.DialingReceivingWaitingLayout;
import com.shanjingtech.secumchat.ui.HeartMagicLayout;
import com.shanjingtech.secumchat.ui.HeartSecumCounter;
import com.shanjingtech.secumchat.ui.SecumCounter;

public class DebugActivity extends Activity implements SecumCounter.SecumCounterListener {
    private final static String TAG = "DebugActivity";
    HeartSecumCounter heartSecumCounter;
    HeartMagicLayout heart;
    DialingReceivingWaitingLayout dialingReceivingWaitingLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_activity);
        dialingReceivingWaitingLayout = (DialingReceivingWaitingLayout) findViewById(R.id.drw);
        heartSecumCounter = (HeartSecumCounter) findViewById(R.id.chronometer);
//        catHead = (PulseImageView) findViewById(R.id.cat_head);
        heart = (HeartMagicLayout) findViewById(R.id.heart);
    }

    public void clickHeart(View view) {
        heart.meLike();
    }

    public void b1(View view) {
        heartSecumCounter.explode();
//        secumCounter.initialize();
//        heart.switchState(PairLikeImageView.LikeState.ME_LIKE);
//        secumCounter.initialize();
//        catHead.startPulse();
//        dialingReceivingWaitingLayout.switchUIState(SecumChatActivity.State.DIALING);
//        secumCounter.meAdd();
    }

    public void b2(View view) {
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
}
