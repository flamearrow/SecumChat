package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.shanjingtech.secumchat.net.SecumAPI#registerUser(UserRequest)}
 */
public class User {
    // phone+16503181659
    @SerializedName("username")
    @Expose
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

/* sample json response
    {
        "username":"phone+16503181659",
            "third_party_uid":null,
            "third_party_auth":null,
            "email":null,
            "phone":null,
            "profile_image_url":null,
            "lat":null,
            "lng":null,
            "nickname":null,
            "access_code":null,
            "num_comments":null,
            "num_posts":null,
            "num_following":null,
            "num_followed":null,
            "time_created":null,
            "time_updated":null
    }
 */
}
