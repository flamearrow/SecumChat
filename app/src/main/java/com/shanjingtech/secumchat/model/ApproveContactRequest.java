package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApproveContactRequest {
    public ApproveContactRequest(int contact_request_id) {
        this.contact_request_id = contact_request_id;
    }

    @Expose
    @SerializedName("contact_request_id")
    int contact_request_id;

    @Expose
    @SerializedName("contact_action")
    String contact_action = "CONTACT_ACTION_ENUM_APPROVE";
}
