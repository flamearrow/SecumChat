package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("contact_username")
    @Expose
    String contact_username;


    @SerializedName("contact_nickname")
    @Expose
    String contact_nickname;
}
