package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PullGroupMessagesRequest {

    public PullGroupMessagesRequest(int msg_grp_id) {
        this.msg_grp_id = msg_grp_id;
    }

    @Expose
    @SerializedName("msg_grp_id")
    int msg_grp_id;
}
