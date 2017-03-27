package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * OAuth2 token
 */
public class AccessToken {

    @SerializedName("access_token")
    @Expose
    String access_token;

    public String getAccess_token() {
        return access_token;
    }
}
