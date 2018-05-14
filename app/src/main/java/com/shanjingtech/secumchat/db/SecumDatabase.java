package com.shanjingtech.secumchat.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Message.class, UserDB.class}, version = 1, exportSchema = false)
public abstract class SecumDatabase extends RoomDatabase {
    public abstract MessageDAO messageDAO();

    public abstract UserDAO userDAO();
}
