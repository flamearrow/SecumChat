package com.shanjingtech.secumchat.db;

import androidx.room.ColumnInfo;

public class BotConversationPreview {
    @ColumnInfo(name = "group_id")
    public String group_id;

    @ColumnInfo(name = "time")
    public long time;

    @ColumnInfo(name = "from_username")
    public String from_username;

    @ColumnInfo(name = "to_username")
    public String to_username;

    @ColumnInfo(name = "last_message")
    public String last_message;
}
