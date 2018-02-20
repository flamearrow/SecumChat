package com.shanjingtech.secumchat.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Message.class}, version = 1)
public abstract class SecumDatabase extends RoomDatabase {
    public abstract MessageDAO messageDAO();
}
