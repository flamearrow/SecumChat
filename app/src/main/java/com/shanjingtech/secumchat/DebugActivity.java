package com.shanjingtech.secumchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.shanjingtech.secumchat.ui.HeartMagicLayout;
import com.shanjingtech.secumchat.ui.PulseImageView;
import com.shanjingtech.secumchat.ui.SecumCounter;

public class DebugActivity extends Activity implements SecumCounter.SecumCounterListener {
    private final static String TAG = "DebugActivity";
    SecumCounter secumCounter;
//    PulseImageView catHead;
    HeartMagicLayout heart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_activity);
        secumCounter = (SecumCounter) findViewById(R.id.chronometer);
        secumCounter.setSecumCounterListener(this);
//        catHead = (PulseImageView) findViewById(R.id.cat_head);
        heart = (HeartMagicLayout) findViewById(R.id.heart);
    }

    public void clickHeart(View view) {
        heart.meLike();
    }

    public void b1(View view) {
        secumCounter.initialize();
//        heart.switchState(PairLikeImageView.LikeState.ME_LIKE);
//        secumCounter.initialize();
//        catHead.startPulse();
    }

    public void b2(View view) {
//        heart.switchState(PairLikeImageView.LikeState.PEER_LIKE);
        heart.peerLike();
        secumCounter.peerAdd();
//        secumCounter.shake();
//        catHead.stopPulse();
//        secumCounter.zoom();
//        secumCounter.meAdd();
    }

    public void b3(View view) {
//        heart.switchState(HeartMagicLayout.LikeState.NO_LIKE);
//        secumCounter.peerAdd();
//        secumCounter.freeze();
        heart.meLike();
        secumCounter.meAdd();
    }

    public void b4(View view) {
//        heart.switchState(HeartMagicLayout.LikeState.BOTH_LIKE);
        secumCounter.bounce();
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
