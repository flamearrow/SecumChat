package com.meichinijiuchiquba.secumchat.injection;

import com.meichinijiuchiquba.secumchat.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides current logged in user.
 */
@Singleton
public class CurrentUserProvider {
    @Inject
    public CurrentUserProvider() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;
}
