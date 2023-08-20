package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetInfoRequest {

    public GetInfoRequest(int userId) {
        this.userId = userId;
    }
    @Expose
    @SerializedName("user_id")
    int userId;
}
