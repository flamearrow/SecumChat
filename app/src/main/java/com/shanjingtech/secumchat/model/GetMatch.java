package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shanjingtech.secumchat.net.SecumAPI;

/**
 * Created by flamearrow on 2/26/17.
 */

public class GetMatch {

    public boolean isSuccess() {
        return SecumAPI.MATCH_ACTIVE.equals(status);
    }

    /**
     * Should I call or receive
     */
    public boolean isCaller() {
        return isCaller;
    }

    public boolean isCallee() {
        return !isCaller;
    }

    public String getCaller() {
        return callerName;
    }

    public String getCallee() {
        return calleeName;
    }

    @Expose
    @SerializedName("status")
    String status;

    // TODO: make sure camel case works
    @Expose
    @SerializedName("match_id")
    int matchId;

    @Expose
    @SerializedName("chat_uri")
    String chatURI;

    @Expose
    @SerializedName("is_caller")
    boolean isCaller;

    @Expose
    @SerializedName("caller_username")
    String callerName;

    @Expose
    @SerializedName("callee_username")
    String calleeName;

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

    public int getMatchId() {
        return matchId;
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

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }
}
