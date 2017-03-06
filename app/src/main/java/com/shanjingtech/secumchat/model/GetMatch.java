package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flamearrow on 2/26/17.
 */

public class GetMatch {
    public boolean success;
    public String caller;
    public String callee;

    public boolean shouldDial() {
        return true;
    }

    public boolean isSuccess() {
        return matchId != null;
    }

    /**
     * Should I call or receive
     */
    public boolean isCaller() {
        return matchedUsername.equals(getCallee());
    }

    public String getCaller() {
        return matchId.split("|")[0];
    }

    public String getCallee() {
        return matchId.split("|")[1];
    }

    @Expose
    @SerializedName("status")
    String status;

    // TODO: make sure camel case works
    @Expose
    @SerializedName("match_id")
    String matchId;

    @Expose
    @SerializedName("chat_uri")
    String chatURI;

    @Expose
    @SerializedName("text")
    String text;

    @Expose
    @SerializedName("logging")
    String logging;

    @Expose
    @SerializedName("matched_username")
    String matchedUsername;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getChatURI() {
        return chatURI;
    }

    public void setChatURI(String chatURI) {
        this.chatURI = chatURI;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLogging() {
        return logging;
    }

    public void setLogging(String logging) {
        this.logging = logging;
    }

    public String getMatchedUsername() {
        return matchedUsername;
    }

    public void setMatchedUsername(String matchedUsername) {
        this.matchedUsername = matchedUsername;
    }
}
