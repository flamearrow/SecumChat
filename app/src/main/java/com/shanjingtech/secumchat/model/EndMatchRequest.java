package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flamearrow on 3/5/17.
 */

public class EndMatchRequest {
    public EndMatchRequest(String username) {
        this.username = username;
    }

    @Expose
    @SerializedName("username")
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
