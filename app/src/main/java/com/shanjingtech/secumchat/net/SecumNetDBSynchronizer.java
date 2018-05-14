package com.shanjingtech.secumchat.net;


import android.util.Log;

import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.db.UserDAO;
import com.shanjingtech.secumchat.db.UserDB;
import com.shanjingtech.secumchat.model.Contact;
import com.shanjingtech.secumchat.model.ListContactsRequest;
import com.shanjingtech.secumchat.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Firing API calls and insert into Database
 */
public class SecumNetDBSynchronizer {
    private static final String TAG = SecumNetDBSynchronizer.class.getCanonicalName();
    private SecumAPI secumAPI;
    private UserDAO userDAO;
    private MessageDAO messageDAO;

    public SecumNetDBSynchronizer(SecumAPI secumAPI, UserDAO userDAO, MessageDAO messageDAO) {
        this.secumAPI = secumAPI;
        this.userDAO = userDAO;
        this.messageDAO = messageDAO;
    }

    /**
     * Invoke {@link SecumAPI#listContacts(ListContactsRequest)} with all types and insert into
     * {@link UserDB} table
     * <p>
     * TODO: now send 4 requests in seq, replace with wildcard when server API is available
     */
    public void syncUserDBbyPreview(String ownerName) {
        // active
        secumAPI.listContacts(new ListContactsRequest()).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                new Thread(() -> {
                    for (Contact contact : response.body()) {
                        userDAO.insertUser(
                                new UserDB.Builder()
                                        .setUserName(contact.getContact_username())
                                        .setNickName(contact.getContact_nickname())
                                        .setStatus(UserDB.ContactStatus
                                                .CONTACT_STATUS_ACTIVE)
                                        .setOwnerName(ownerName).build());
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.d(TAG, "failed in SecumNetDBSynchronizer#syncUserDBbyPreview");
            }
        });

        // blocked
        secumAPI.listContacts(new ListContactsRequest(Constants.CONTACT_STATUS_BLOCKED))
                .enqueue(
                        new Callback<List<Contact>>() {
                            @Override
                            public void onResponse(Call<List<Contact>> call,
                                                   Response<List<Contact>> response) {
                                new Thread(() -> {
                                    for (Contact contact : response.body()) {
                                        userDAO.insertUser(
                                                new UserDB.Builder()
                                                        .setUserName(contact.getContact_username())
                                                        .setNickName(contact.getContact_nickname())
                                                        .setStatus(UserDB.ContactStatus
                                                                .CONTACT_STATUS_BLOCKED)
                                                        .setOwnerName(ownerName).build());
                                    }
                                }).start();

                            }

                            @Override
                            public void onFailure(Call<List<Contact>> call, Throwable t) {
                                Log.d(TAG, "failed in " +
                                        "SecumNetDBSynchronizer#syncUserDBbyPreview");
                            }
                        });

        // pending
        secumAPI.listContacts(new ListContactsRequest(Constants.CONTACT_STATUS_PENDING))
                .enqueue(
                        new Callback<List<Contact>>() {
                            @Override
                            public void onResponse(Call<List<Contact>> call,
                                                   Response<List<Contact>> response) {
                                new Thread(() -> {
                                    for (Contact contact : response.body()) {
                                        userDAO.insertUser(
                                                new UserDB.Builder()
                                                        .setUserName(contact.getContact_username())
                                                        .setNickName(contact.getContact_nickname())
                                                        .setStatus(UserDB.ContactStatus
                                                                .CONTACT_STATUS_PENDING)
                                                        .setOwnerName(ownerName).build());
                                    }
                                }).start();
                            }

                            @Override
                            public void onFailure(Call<List<Contact>> call, Throwable t) {
                                Log.d(TAG, "failed in " +
                                        "SecumNetDBSynchronizer#syncUserDBbyPreview");
                            }
                        });

        // requested
        secumAPI.listContacts(new ListContactsRequest(Constants.CONTACT_STATUS_REQUESTED))
                .enqueue(
                        new Callback<List<Contact>>() {
                            @Override
                            public void onResponse(Call<List<Contact>> call,
                                                   Response<List<Contact>> response) {
                                new Thread(() -> {
                                    for (Contact contact : response.body()) {
                                        userDAO.insertUser(
                                                new UserDB.Builder()
                                                        .setUserName(contact.getContact_username())
                                                        .setNickName(contact.getContact_nickname())
                                                        .setStatus(UserDB.ContactStatus
                                                                .CONTACT_STATUS_REQUESTED)
                                                        .setOwnerName(ownerName).build());
                                    }
                                }).start();
                            }

                            @Override
                            public void onFailure(Call<List<Contact>> call, Throwable t) {
                                Log.d(TAG, "failed in " +
                                        "SecumNetDBSynchronizer#syncUserDBbyPreview");
                            }
                        });

    }


    /**
     * Invoke {@link SecumAPI#getProfile()} and insert the returned user into {@link UserDB} table
     *
     * @param ownerName
     * @param userName
     */
    public void syncUserDBFromUserName(String ownerName, String userName) {

    }
}
