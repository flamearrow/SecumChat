package com.shanjingtech.secumchat.net;


import androidx.annotation.Nullable;
import android.util.Log;

import com.shanjingtech.secumchat.db.Message;
import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.db.TimestampConverter;
import com.shanjingtech.secumchat.db.UserDAO;
import com.shanjingtech.secumchat.db.UserDB;
import com.shanjingtech.secumchat.injection.CurrentUserProvider;
import com.shanjingtech.secumchat.model.Contact;
import com.shanjingtech.secumchat.model.GetProfileFromUserNameRequest;
import com.shanjingtech.secumchat.model.GroupMessages;
import com.shanjingtech.secumchat.model.ListContactsRequest;
import com.shanjingtech.secumchat.model.UnreadMessage;
import com.shanjingtech.secumchat.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Firing API calls and insert into Database.
 * Note most SecumAPI calls should be initialized here to ensure synchronization of DB and memory
 * data.
 */
public class SecumNetDBSynchronizer {
    public interface SecumNetDBSynchronizerListener {
        void onDBOperationSuccess();

        void onDBOperationFailure();
    }

    private static final String TAG = SecumNetDBSynchronizer.class.getCanonicalName();
    private SecumAPI secumAPI;
    private UserDAO userDAO;
    private MessageDAO messageDAO;
    private CurrentUserProvider currentUserProvider;

    public SecumNetDBSynchronizer(SecumAPI secumAPI, UserDAO userDAO, MessageDAO messageDAO,
                                  CurrentUserProvider currentUserProvider) {
        this.secumAPI = secumAPI;
        this.userDAO = userDAO;
        this.messageDAO = messageDAO;
        this.currentUserProvider = currentUserProvider;
    }

    public void syncUserDBbyPreview(String ownerName) {
        syncUserDBbyPreview(ownerName, null);
    }

    /**
     * Invoke {@link SecumAPI#listContacts(ListContactsRequest)} with all types and insert into
     * {@link UserDB} table
     * <p>
     */
    public void syncUserDBbyPreview(String ownerName, @Nullable SecumNetDBSynchronizerListener
            listener) {
//        secumAPI.listContacts(new ListContactsRequest()).enqueue(new Callback<List<Contact>>() {
//            @Override
//            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
//                if (response.isSuccessful()) {
//
//                    new Thread(() -> {
//                        for (Contact contact : response.body()) {
//                            userDAO.insertUser(
//                                    new UserDB.Builder()
//                                            .setUserName(contact.getContact_username())
//                                            .setNickName(contact.getContact_nickname())
//                                            .setStatus(contact.getContactStatus())
//                                            .setOwnerName(ownerName).build());
//                        }
//                        if (listener != null) {
//                            listener.onDBOperationSuccess();
//                        }
//                    }).start();
//                } else {
//                    if (listener != null) {
//                        listener.onDBOperationFailure();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Contact>> call, Throwable t) {
//                Log.d(TAG, "failed in SecumNetDBSynchronizer#syncUserDBbyPreview");
//                if (listener != null) {
//                    listener.onDBOperationFailure();
//                }
//            }
//        });

    }

    public void syncUserDBFromUserName(String ownerName, String userName) {
        syncUserDBFromUserName(ownerName, userName, null);
    }

    /**
     * Invoke {@link SecumAPI#getProfileFromUserName(GetProfileFromUserNameRequest)} and insert
     * the returned user into {@link UserDB} table
     *
     * @param ownerName
     * @param userName
     */
    public void syncUserDBFromUserName(String ownerName, String userName, @Nullable
            SecumNetDBSynchronizerListener listener) {
        secumAPI.getProfileFromUserName(new GetProfileFromUserNameRequest(userName)).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            new Thread(() -> {
                                User user = response.body();
                                if (user != null) {
                                    // TODO: let getProfile API return status
                                    int oldStatus = userDAO.getUserStatus(ownerName, userName);
                                    userDAO.insertUser(new UserDB.Builder().setUser(user)
                                            .setOwnerName(ownerName).setStatus(oldStatus).build());
                                    if (listener != null) {
                                        listener.onDBOperationSuccess();
                                    }
                                } else {
                                    Log.d(TAG, "no user found for " + userName);
                                }
                            }).start();
                        } else {
                            if (listener != null) {
                                listener.onDBOperationFailure();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(TAG, "failed in SecumNetDBSynchronizer#syncUserDBFromUserName");
                        if (listener != null) {
                            listener.onDBOperationFailure();
                        }
                    }
                });
    }

    public void syncUserDBFromCurrentToken() {
        syncUserDBFromCurrentToken(null);
    }


    /**
     * Invoke {@link SecumAPI#getProfile()} from current token, update
     * {@link CurrentUserProvider} and database upon success.
     *
     * @param listener
     */
    public void syncUserDBFromCurrentToken(@Nullable SecumNetDBSynchronizerListener listener) {
        secumAPI.getProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        User user = response.body();
                        if (user != null) {
                            currentUserProvider.setUser(user);
                            userDAO.updateUser(new UserDB.Builder().setUser(user)
                                    .setOwnerName(user.getUsername()).build());
                            if (listener != null) {
                                listener.onDBOperationSuccess();
                            }
                        } else {
                            Log.d(TAG, "no user returned for current token");
                        }
                    }).start();
                } else {
                    if (listener != null) {
                        listener.onDBOperationFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "failed in SecumNetDBSynchronizer#syncUserDBFromCurrentToken");
                if (listener != null) {
                    listener.onDBOperationFailure();
                }
            }
        });
    }

    public void syncUnreadMessageForUser(String ownerName) {
        syncUnreadMessageForUser(ownerName, null);
    }

    /**
     * Pull unread messages for owner
     *
     * @param ownerName
     */
    public void syncUnreadMessageForUser(String ownerName, @Nullable SecumNetDBSynchronizerListener
            listener) {
        secumAPI.pullMessage().enqueue(new Callback<GroupMessages>() {
            @Override
            public void onResponse(Call<GroupMessages> call, Response<GroupMessages> response) {
//                if (response.isSuccessful()) {
//                    new Thread(() -> {
//                        for (UnreadMessage unreadMessage : response.body().getGroupMessages()) {
//                            messageDAO.insertMessage(
//                                    new Message.Builder().setOwnerName(ownerName).setGroupId
//                                            (unreadMessage.getMessage_group_id()).setContent
//                                            (unreadMessage.getText()).setFrom(unreadMessage
//                                            .getSender_username()).setTo(ownerName).setTime
//                                            (TimestampConverter.fromString(unreadMessage
//                                                    .getTime_updated())).setRead(false).build());
//                        }
//                        if (listener != null) {
//                            listener.onDBOperationSuccess();
//                        }
//                    }
//                    ).start();
//                } else {
//                    if (listener != null) {
//                        listener.onDBOperationFailure();
//                    }
//                }
            }

            @Override
            public void onFailure(Call<GroupMessages> call, Throwable t) {
                Log.d(TAG, "failed in SecumNetDBSynchronizer#synUnreadMessageForUser");
                if (listener != null) {
                    listener.onDBOperationFailure();
                }
            }
        });
    }

}
