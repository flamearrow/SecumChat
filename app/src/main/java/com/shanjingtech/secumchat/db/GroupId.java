package com.shanjingtech.secumchat.db;

import androidx.room.ColumnInfo;

/**
 * Created by flamearrow on 4/1/18.
 */

public class GroupId {
    @ColumnInfo(name = "group_id")
    private String groupId;

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }
}
