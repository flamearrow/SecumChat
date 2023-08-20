package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.meichinijiuchiquba.secumchat.net.SecumAPI#reportUser(ReportUserRequest)}
 */
public class ReportUserRequest {
    @Expose
    @SerializedName("reported_username")
    String reportedUserName;

    @Expose
    @SerializedName("reason")
    String reason;

    public void setReportedUserName(String reportedUserName) {
        this.reportedUserName = reportedUserName;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
