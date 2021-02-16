package com.shanjingtech.secumchat.viewModels;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.shanjingtech.secumchat.db.UserPreview;

import java.util.List;

public class ContactsViewModel extends SecumDBViewModel {

    public ContactsViewModel(Application application) {
        super(application);
    }

    public LiveData<List<UserPreview>> getActiveContactsOwnedBy(String ownerId) {
        return userDAO.getLiveActiveContacts(ownerId);
    }

    public LiveData<List<UserPreview>> getRequestedContactsOwnedBy(String ownerId) {
        return userDAO.getLiveRequestedContacts(ownerId);
    }

    public LiveData<List<UserPreview>> getBlockedContactsOwnedBy(String ownerId) {
        return userDAO.getLiveBlockedContacts(ownerId);
    }

    public LiveData<List<UserPreview>> getPendingContactsOwnedBy(String ownerId) {
        return userDAO.getLivePendingContacts(ownerId);
    }
}
