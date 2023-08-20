package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class CreateGroupRequest {
    public CreateGroupRequest(int userId) {
        this.userIds = new LinkedList<>();
        this.userIds.add(userId);
    }

    @Expose
    @SerializedName("user_ids")
    List<Integer> userIds;
}
