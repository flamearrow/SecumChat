package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactInfos {
    @SerializedName("contactInfos")
    @Expose
    public List<ContactInfosValue> contactInfosValues;
}
