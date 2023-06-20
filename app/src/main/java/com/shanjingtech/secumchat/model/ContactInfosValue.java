package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactInfosValue {
    @SerializedName("userInfo")
    @Expose
    public User user;
}
