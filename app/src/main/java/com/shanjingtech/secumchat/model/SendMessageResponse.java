package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flamearrow on 4/1/18.
 */

public class SendMessageResponse {
    @Expose
    @SerializedName("message_group_id")
    String groupId;

    public String getGroupId() {
        return groupId;
    }
}
