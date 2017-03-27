package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.shanjingtech.secumchat.net.SecumAPI#getAccessCode(AccessCodeRequest)}
 */

public class AccessCodeRequest {
    @Expose
    @SerializedName("phone")
    String phone;

    public AccessCodeRequest(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
