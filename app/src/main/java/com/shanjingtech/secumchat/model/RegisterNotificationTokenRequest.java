package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterNotificationTokenRequest {
    public RegisterNotificationTokenRequest(String token) {
        this.token = token;
    }

    @Expose
    @SerializedName("token")
    String token;
}
