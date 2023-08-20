package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteContactRequest {
    public DeleteContactRequest(String contact_username) {
        this.contact_username = contact_username;
    }

    @Expose
    @SerializedName("contact_username")
    String contact_username;
}
