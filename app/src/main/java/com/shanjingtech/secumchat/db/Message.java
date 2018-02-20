package com.shanjingtech.secumchat.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Message table to save meta data of all messages.
 */
@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long messageId;

    @ColumnInfo(name = "group_id")
    private String groupId;

    @ColumnInfo(name = "owner_name")
    private String ownerName;

    @ColumnInfo(name = "from_username")
    private String from;

    @ColumnInfo(name = "to_username")
    private String to;

    private String content;

    private long time;

    private boolean read;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

}
