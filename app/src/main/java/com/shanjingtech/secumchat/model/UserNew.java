package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserNew {
    @SerializedName("userInfo")
    @Expose
    public User userInfo;
}
