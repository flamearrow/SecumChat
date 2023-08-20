package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Access code for user phone registration
 * {@link com.meichinijiuchiquba.secumchat.net.SecumAPI#getAccessCode(AccessCodeRequest)}
 */

public class AccessCode {
    @SerializedName("accessCode")
    @Expose
    String access_code;

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }
}
