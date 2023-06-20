package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddContactRequest {
    public AddContactRequest(int contact_username) {
        this.contact_user_id = contact_user_id;
    }

    @Expose
    @SerializedName("contact_user_id")
    int contact_user_id;
}
