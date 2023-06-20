package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flamearrow on 11/21/17.
 */

public class SendMessageRequest {
    public SendMessageRequest(String groupId, String text) {
        this.msg_grp_id = Integer.parseInt(groupId);
        this.text = text;
    }

    @Expose
    @SerializedName("msg_grp_id")
    int msg_grp_id;


    @Expose
    @SerializedName("text")
    String text;
}
