package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shanjingtech.secumchat.util.Constants;

/**
 * {@link com.shanjingtech.secumchat.net.SecumAPI#listContacts(ListContactsRequest)}
 */

public class ListContactsRequest {
    public ListContactsRequest() {
        // default check active contacts
        this(Constants.CONTACT_STATUS_ACTIVE);
    }

    public ListContactsRequest(String contactsType) {
        setStatus(contactsType);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Expose
    @SerializedName("status")
    String status;
}
