package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactInfosValue {
    @SerializedName("userInfo")
    @Expose
    public User user;
}
