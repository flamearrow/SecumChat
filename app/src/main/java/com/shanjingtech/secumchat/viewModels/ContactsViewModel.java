package com.shanjingtech.secumchat.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.shanjingtech.secumchat.SecumApplication;
import com.shanjingtech.secumchat.db.UserDAO;
import com.shanjingtech.secumchat.db.UserPreview;

import java.util.List;

import javax.inject.Inject;

public class ContactsViewModel extends AndroidViewModel {
    @Inject
    UserDAO userDAO;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        ((SecumApplication) application).getNetComponet().inject(this);
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
