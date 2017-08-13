package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockContactRequest {
    public BlockContactRequest(String contact_username) {
        this.contact_username = contact_username;
    }

    @Expose
    @SerializedName("contact_username")
    String contact_username;
}
