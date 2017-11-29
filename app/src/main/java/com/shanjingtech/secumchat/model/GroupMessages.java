package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by flamearrow on 11/28/17.
 */

public class GroupMessages {
    public List<UnreadMessage> getGroupMessages() {
        return groupMessages;
    }

    @Expose
    @SerializedName("group_messages")
    List<UnreadMessage> groupMessages;
}
