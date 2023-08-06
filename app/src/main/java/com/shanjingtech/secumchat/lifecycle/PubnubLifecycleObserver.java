package com.shanjingtech.secumchat.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.pubnub.api.PubNub;
import com.shanjingtech.secumchat.injection.CurrentUserProvider;

import java.util.Collections;

public class PubnubLifecycleObserver implements LifecycleObserver {
    private CurrentUserProvider currentUserProvider;
    private PubNub pubNub;

    public PubnubLifecycleObserver(CurrentUserProvider currentUserProvider, PubNub pubNub) {
        this.currentUserProvider = currentUserProvider;
        this.pubNub = pubNub;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void connectPubnub() {
        String currentUserChannel = currentUserProvider.getUser().userId;
        Log.d("BGLM", "connecting pn: " + currentUserChannel);
        pubNub.unsubscribeAll();
        pubNub.subscribe().channels(Collections.singletonList(currentUserChannel)).execute();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectPubnub() {
        Log.d("BGLM", "ON_PAUSE");
        pubNub.unsubscribeAll();
    }
}
