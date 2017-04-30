package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shanjingtech.secumchat.net.SecumAPI;

/**
 * {@link com.shanjingtech.secumchat.net.SecumAPI#getMatch(GetMatchRequest)}
 */
public class GetMatch {
    public boolean isValid() {
        return callerName != null && calleeName != null;
    }

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

    @Expose
    @SerializedName("match_id")
    int matchId;

    @Expose
    @SerializedName("chat_uri")
    String chatURI;

    public void setCaller(boolean caller) {
        isCaller = caller;
    }

    @Expose
    @SerializedName("is_caller")
    boolean isCaller;

    @Expose
    @SerializedName("caller_username")
    String callerName;


    @Expose
    @SerializedName("caller_nickname")
    String callerNickName;

    @Expose
    @SerializedName("callee_nickname")
    String calleeNickName;

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

    @Expose
    @SerializedName("caller_gender")
    String callerGender;

    @Expose
    @SerializedName("callee_gender")
    String calleeGender;

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

    public String getCallerGender() {
        return callerGender;
    }

    public void setCallerGender(String callerGender) {
        this.callerGender = callerGender;
    }

    public String getCalleeGender() {
        return calleeGender;
    }

    public void setCalleeGender(String calleeGender) {
        this.calleeGender = calleeGender;
    }

    public String getCallerNickName() {
        return callerNickName;
    }

    public String getCalleeNickName() {
        return calleeNickName;
    }
}
