package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.shanjingtech.secumchat.net.SecumAPI#registerUser(UserRequest)}
 * { "username":"phone+16503181659", "phone": "+16503181659"}
 */

public class UserRequest {
    @Expose
    @SerializedName("username")
    String username;

    @Expose
    @SerializedName("phone")
    String phone;

    public UserRequest(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
