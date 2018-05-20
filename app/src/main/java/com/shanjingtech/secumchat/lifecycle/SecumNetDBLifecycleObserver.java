package com.shanjingtech.secumchat.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.shanjingtech.secumchat.injection.CurrentUserProvider;
import com.shanjingtech.secumchat.net.SecumNetDBSynchronizer;

public class SecumNetDBLifecycleObserver implements LifecycleObserver {
    private SecumNetDBSynchronizer secumNetDBSynchronizer;
    private CurrentUserProvider currentUserProvider;

    public SecumNetDBLifecycleObserver(SecumNetDBSynchronizer secumNetDBSynchronizer,
                                       CurrentUserProvider currentUserProvider) {
        this.secumNetDBSynchronizer = secumNetDBSynchronizer;
        this.currentUserProvider = currentUserProvider;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void pullUnreadMessages() {
        secumNetDBSynchronizer.syncUnreadMessageForUser(currentUserProvider.getUser().getUsername());
    }
}
