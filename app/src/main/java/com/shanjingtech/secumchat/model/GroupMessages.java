package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shanjingtech.secumchat.db.Message;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by flamearrow on 11/28/17.
 */

public class GroupMessages {

    @Expose
    @SerializedName("messages")
    public List<MessageNew> messages = new LinkedList<>();
}
