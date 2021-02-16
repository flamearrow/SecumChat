package com.shanjingtech.secumchat.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

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
        if (currentUserProvider.getUser() != null) {
            secumNetDBSynchronizer.syncUnreadMessageForUser(currentUserProvider.getUser()
                    .getUsername());
        }
    }
}
