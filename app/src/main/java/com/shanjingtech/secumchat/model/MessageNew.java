package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageNew {
    @Expose
    @SerializedName("messageId")
    public String messageId;

    @Expose
    @SerializedName("messageGroupId")
    public int messageGroupId;

    @Expose
    @SerializedName("userInfo")
    public User userInfo;

    @Expose
    @SerializedName("text")
    public String text;

    @Expose
    @SerializedName("timestampCreated")
    public String timestampCreated;

    public String getFrom() {
        return userInfo.nickname;
    }

    public String getContent() {
        return text;
    }

    public Long getTime() {
        return Long.parseLong(timestampCreated);
    }



}
