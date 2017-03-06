package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Access code for user phone registration
 * {@link com.shanjingtech.secumchat.net.SecumAPI#getAccessCode(AccessCodeRequest)}
 */

public class AccessCode {
    @SerializedName("access_code")
    @Expose
    String access_code;

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }
}
