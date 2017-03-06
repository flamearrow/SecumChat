package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 */

public class AccessToken {
    @SerializedName("access_token")
    @Expose
    String access_token;
}
