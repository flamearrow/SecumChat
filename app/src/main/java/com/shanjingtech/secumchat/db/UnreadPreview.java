package com.shanjingtech.secumchat.db;

import androidx.room.ColumnInfo;

/**
 * Used for preview unread messages
 */
public class UnreadPreview {
    private String group_id;

    @ColumnInfo(name = "unread_count")
    private int unreadCount;

    @ColumnInfo(name = "content")
    private String lastUnreadContent;

    @ColumnInfo(name = "from_username")
    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastUnreadContent() {
        return lastUnreadContent;
    }

    public void setLastUnreadContent(String lastUnreadContent) {
        this.lastUnreadContent = lastUnreadContent;
    }
}
