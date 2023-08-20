package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flamearrow on 4/16/17.
 */

public class ReportUserResponse {
    @Expose
    @SerializedName("report_id")
    String report_id;
    @Expose
    @SerializedName("reported_username")
    String reported_username;
    @Expose
    @SerializedName("username")
    String username;
    @Expose
    @SerializedName("reason")
    String reason;
    @Expose
    @SerializedName("other_info")
    String other_info;
}
