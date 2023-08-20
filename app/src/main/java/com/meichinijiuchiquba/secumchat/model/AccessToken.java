package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * OAuth2 token
 * {@link com.meichinijiuchiquba.secumchat.net.SecumAPI#getAccessCode(AccessCodeRequest)}
 */
public class AccessToken {

    @SerializedName("access_token")
    @Expose
    String access_token;

    public String getAccess_token() {
        return access_token;
    }
}
