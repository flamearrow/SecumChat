package com.shanjingtech.secumchat.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.shanjingtech.pnwebrtc.PnRTCClient;
import com.shanjingtech.secumchat.injection.CurrentUserProvider;

/**
 * Observes lifecycle events for pubnub to sub/unsub.
 */
public class PnRTCClientLifecycleObserver implements LifecycleObserver {
    private static final String TAG = "MLGB";

    private PnRTCClient pnRTCClient;
    private CurrentUserProvider currentUserProvider;


    public PnRTCClientLifecycleObserver(
            PnRTCClient pnRTCClient,
            CurrentUserProvider currentUserProvider) {
        this.pnRTCClient = pnRTCClient;
        this.currentUserProvider = currentUserProvider;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void connectPubnub() {
        Log.d(TAG, "initializePubnub");
        pnRTCClient.pubnubUnsubscribeAll();
        pnRTCClient.subscribeToPubnubChannel(currentUserProvider.getUser().getUsername());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectPubnub() {
        Log.d(TAG, "disconnectPubnub");
        pnRTCClient.pubnubUnsubscribeAll();
    }
}
