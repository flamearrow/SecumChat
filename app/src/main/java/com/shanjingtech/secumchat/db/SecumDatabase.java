package com.shanjingtech.secumchat.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Message.class, UserDB.class}, version = 1, exportSchema = false)
public abstract class SecumDatabase extends RoomDatabase {
    public abstract MessageDAO messageDAO();

    public abstract UserDAO userDAO();
}
