package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewUser implements Serializable {


    @SerializedName("userId")
    @Expose
    public String userId;

}