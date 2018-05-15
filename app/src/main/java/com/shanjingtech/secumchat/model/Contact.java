package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shanjingtech.secumchat.db.UserDB;

import static com.shanjingtech.secumchat.util.Constants.CONTACT_STATUS_ACTIVE;
import static com.shanjingtech.secumchat.util.Constants.CONTACT_STATUS_BLOCKED;
import static com.shanjingtech.secumchat.util.Constants.CONTACT_STATUS_PENDING;
import static com.shanjingtech.secumchat.util.Constants.CONTACT_STATUS_REQUESTED;

/**
 * Model for contact.
 * {@link com.shanjingtech.secumchat.net.SecumAPI#listContacts(ListContactsRequest)}}
 */

public class Contact {
    public String getContact_username() {
        return contact_username;
    }

    public String getContact_nickname() {
        return contact_nickname;
    }

    public String getContact_status() {
        return contact_status;
    }

    public @UserDB.ContactStatus
    int getContactStatus() {
        switch (contact_status) {
            case CONTACT_STATUS_ACTIVE:
                return UserDB.ContactStatus.CONTACT_STATUS_ACTIVE;
            case CONTACT_STATUS_BLOCKED:
                return UserDB.ContactStatus.CONTACT_STATUS_BLOCKED;
            case CONTACT_STATUS_PENDING:
                return UserDB.ContactStatus.CONTACT_STATUS_PENDING;
            case CONTACT_STATUS_REQUESTED:
                return UserDB.ContactStatus.CONTACT_STATUS_REQUESTED;
            default:
                return UserDB.ContactStatus.CONTACT_STATUS_UNKNOWN;
        }
    }

    @SerializedName("contact_username")
    @Expose
    String contact_username;


    @SerializedName("contact_nickname")
    @Expose
    String contact_nickname;

    @SerializedName("contact_status")
    @Expose
    String contact_status;
}
