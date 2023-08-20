package com.meichinijiuchiquba.secumchat.viewModels;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.meichinijiuchiquba.secumchat.db.ProfilePreview;

public class ProfileViewModel extends SecumDBViewModel {

    public ProfileViewModel(Application application) {
        super(application);
    }

    public LiveData<ProfilePreview> getActiveContactsOwnedBy(String ownerName, String userName) {
        return userDAO.getLiveUserProfile(ownerName, userName);
    }
}
