package com.meichinijiuchiquba.secumchat.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.util.Log;

import com.meichinijiuchiquba.pnwebrtc.PnRTCClient;
import com.meichinijiuchiquba.secumchat.injection.CurrentUserProvider;

/**
 * Observes lifecycle events for pubnub to sub/unsub.
 */
public class PnRTCClientLifecycleObserver implements LifecycleObserver {
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
        Log.d("BGLM", "PnRTCClientLifecycleObserver connecting pn");
        pnRTCClient.pubnubUnsubscribeAll();
        pnRTCClient.subscribeToPubnubChannel(currentUserProvider.getUser().userId);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectPubnub() {
        Log.d("BGLM", "PnRTCClientLifecycleObserver disconnecting pn");
        pnRTCClient.pubnubUnsubscribeAll();
    }
}
