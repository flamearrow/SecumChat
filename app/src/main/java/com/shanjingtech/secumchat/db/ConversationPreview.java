package com.shanjingtech.secumchat.db;

import android.arch.persistence.room.ColumnInfo;

/**
 * Used for conversation preview
 */
public class ConversationPreview {
    @ColumnInfo(name = "group_id")
    private String groupId;

    @ColumnInfo(name = "unread_count")
    private int unreadCount;

    @ColumnInfo(name = "total_count")
    private int totalCount;

    @ColumnInfo(name = "content")
    private String lastUnreadContent;

    @ColumnInfo(name = "from_username")
    private String from;

    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getLastUnreadContent() {
        return lastUnreadContent;
    }

    public void setLastUnreadContent(String lastUnreadContent) {
        this.lastUnreadContent = lastUnreadContent;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
