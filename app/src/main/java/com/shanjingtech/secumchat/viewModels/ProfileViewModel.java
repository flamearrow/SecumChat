package com.shanjingtech.secumchat.viewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.shanjingtech.secumchat.db.ProfilePreview;

public class ProfileViewModel extends SecumDBViewModel {

    public ProfileViewModel(Application application) {
        super(application);
    }

    public LiveData<ProfilePreview> getActiveContactsOwnedBy(String ownerName, String userName) {
        return userDAO.getLiveUserProfile(ownerName, userName);
    }
}
