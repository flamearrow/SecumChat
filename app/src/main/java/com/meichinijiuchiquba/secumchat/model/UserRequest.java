package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.meichinijiuchiquba.secumchat.net.SecumAPI#registerUser(UserRequest)}
 * {"phone": "+16503181659"}
 */

public class UserRequest {

    @Expose
    @SerializedName("phone")
    String phone;

    public UserRequest(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
