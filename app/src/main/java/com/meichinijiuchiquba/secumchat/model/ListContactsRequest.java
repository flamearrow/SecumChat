package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.meichinijiuchiquba.secumchat.net.SecumAPI#listContacts(ListContactsRequest)}
 */

public class ListContactsRequest {
    public ListContactsRequest() {
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
