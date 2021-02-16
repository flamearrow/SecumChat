package com.shanjingtech.secumchat.db;

import androidx.room.ColumnInfo;

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

    @ColumnInfo(name = "to_username")
    private String to;

    @ColumnInfo(name = "from_nickname")
    private String fromNickname;

    @ColumnInfo(name = "to_nickname")
    private String toNickname;

    @ColumnInfo(name = "owner_name")
    private String owner;

    private long time;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

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

    // return non ownername
    public String getPeerNickName() {
        return owner.equals(from) ? toNickname : fromNickname;
    }

    public String getPeerUserName() {
        return owner.equals(from) ? to : from;
    }

    public String getFromNickname() {
        return fromNickname;
    }

    public void setFromNickname(String fromNickname) {
        this.fromNickname = fromNickname;
    }

    public String getToNickname() {
        return toNickname;
    }

    public void setToNickname(String toNickname) {
        this.toNickname = toNickname;
    }
}
