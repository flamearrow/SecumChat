package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactRequest {
    public ContactRequest(User user) {
        this.user = user;
    }
    @SerializedName("contactRequestId")
    @Expose
    public String contactRequestId;

    @SerializedName("userInfo")
    @Expose
    public User user;

    @SerializedName("status")
    @Expose
    public String status;
}
