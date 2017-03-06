package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.shanjingtech.secumchat.net.SecumAPI#endMatchRequest(EndMatchRequest)}
 */

public class EndMatch {
    @Expose
    @SerializedName("status")
    String status;
    @Expose
    @SerializedName("match_id")
    String matchId;
    @Expose
    @SerializedName("chat_uri")
    String chartURI;
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

    public String getChartURI() {
        return chartURI;
    }

    public void setChartURI(String chartURI) {
        this.chartURI = chartURI;
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
