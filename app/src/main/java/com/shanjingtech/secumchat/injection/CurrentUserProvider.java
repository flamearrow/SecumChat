package com.shanjingtech.secumchat.injection;

import com.shanjingtech.secumchat.model.User;

/**
 * Provides current logged in user.
 */

public class CurrentUserProvider {
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;
}
