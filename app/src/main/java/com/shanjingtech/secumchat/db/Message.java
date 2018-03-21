package com.shanjingtech.secumchat.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Message table to save meta data of all messages.
 */
@Entity
public class Message {
    public static class Builder {
        private long messageId;

        private String groupId;

        private String ownerName;

        private String from;

        private String to;

        private String content;

        private long time;

        // Default read
        private boolean read = true;

        public Builder setMessageId(long messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder setOwnerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder setTo(String to) {
            this.to = to;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public Builder setRead(boolean read) {
            this.read = read;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.setMessageId(messageId);
            message.setGroupId(groupId);
            message.setOwnerName(ownerName);
            message.setFrom(from);
            message.setTo(to);
            message.setContent(content);
            message.setTime(time);
            message.setRead(read);
            return message;
        }
    }

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
