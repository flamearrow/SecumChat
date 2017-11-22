package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flamearrow on 11/21/17.
 */

public class SendMessageRequest {
    public SendMessageRequest(String receiver_username, String text) {
        this.receiver_username = receiver_username;
        this.text = text;
    }

    @Expose
    @SerializedName("receiver_username")
    String receiver_username;


    @Expose
    @SerializedName("text")
    String text;
}
