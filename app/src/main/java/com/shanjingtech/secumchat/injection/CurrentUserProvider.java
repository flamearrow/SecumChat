package com.shanjingtech.secumchat.injection;

import com.shanjingtech.secumchat.model.NewUser;
import com.shanjingtech.secumchat.model.User;

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

    public void setNewUser(NewUser newUser) {
        this.user = new User(newUser);
    }

    private User user;
}
