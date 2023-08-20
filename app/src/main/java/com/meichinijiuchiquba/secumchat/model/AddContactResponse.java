package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddContactResponse {
    @Expose
    @SerializedName("contactRequestId")
    String contactRequestId;


    @Expose
    @SerializedName("status")
    String status;
}
