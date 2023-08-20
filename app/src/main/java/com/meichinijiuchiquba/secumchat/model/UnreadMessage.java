package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flamearrow on 11/21/17.
 */

public class UnreadMessage {
    public String getSender_username() {
        return sender_username;
    }

    public String getText() {
        return text;
    }

    public String getTime_updated() {
        return time_updated;
    }

    public String getMessage_group_id() {
        return message_group_id;
    }

    @Expose
    @SerializedName("sender_username")
    String sender_username;
    @Expose
    @SerializedName("text")
    String text;
    @Expose
    @SerializedName("time_updated")
    String time_updated;
    @Expose
    @SerializedName("message_group_id")
    String message_group_id;
}
