package com.meichinijiuchiquba.secumchat.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingRequests {
    @SerializedName("incomingContactRequests")
    @Expose
    public List<ContactRequest> incomingContactRequests;

    @SerializedName("outgoingContactRequests")
    @Expose
    public List<ContactRequest> outgoingContactRequests;
}
