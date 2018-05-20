package com.shanjingtech.secumchat.net;


import android.util.Log;

import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.db.UserDAO;
import com.shanjingtech.secumchat.db.UserDB;
import com.shanjingtech.secumchat.model.Contact;
import com.shanjingtech.secumchat.model.GetProfileFromUserNameRequest;
import com.shanjingtech.secumchat.model.ListContactsRequest;
import com.shanjingtech.secumchat.model.User;

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
     */
    public void syncUserDBbyPreview(String ownerName) {
        secumAPI.listContacts(new ListContactsRequest()).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                new Thread(() -> {
                    for (Contact contact : response.body()) {
                        userDAO.insertUser(
                                new UserDB.Builder()
                                        .setUserName(contact.getContact_username())
                                        .setNickName(contact.getContact_nickname())
                                        .setStatus(contact.getContactStatus())
                                        .setOwnerName(ownerName).build());
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.d(TAG, "failed in SecumNetDBSynchronizer#syncUserDBbyPreview");
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
        secumAPI.getProfileFromUserName(new GetProfileFromUserNameRequest(userName)).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        new Thread(() -> {
                            User user = response.body();
                            if (user != null) {
                                // TODO: let getProfile API return status
                                int oldStatus = userDAO.getUserStatus(ownerName, userName);
                                userDAO.insertUser(new UserDB.Builder().setUser(user).setOwnerName
                                        (ownerName).setStatus(oldStatus).build());
                            } else {
                                Log.d(TAG, "no user found for " + userName);
                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(TAG, "failed in SecumNetDBSynchronizer#syncUserDBFromUserName");
                    }
                });
    }

}
