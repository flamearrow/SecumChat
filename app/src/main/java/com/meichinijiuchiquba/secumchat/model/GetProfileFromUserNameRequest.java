package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.meichinijiuchiquba.secumchat.net.SecumAPI#getProfileFromUserName(GetProfileFromUserNameRequest)}
 */

public class GetProfileFromUserNameRequest {
    public GetProfileFromUserNameRequest(String userName) {
        contact_username = userName;
    }

    @Expose
    @SerializedName("contact_username")
    String contact_username;
}
