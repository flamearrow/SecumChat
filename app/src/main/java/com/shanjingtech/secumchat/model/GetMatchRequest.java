package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flamearrow on 2/26/17.
 */

public class GetMatchRequest {
    @Expose
    @SerializedName("username")
    String username;

}
