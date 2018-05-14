package com.shanjingtech.secumchat.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDAO {
    /**
     * Insert a user, if there is already same user abort
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(UserDB user);

    /**
     * Update a user, if there's no user just insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(UserDB user);

    @Query("SELECT user_name, nick_name, profile_image_url FROM UserDB WHERE owner_name = " +
            ":ownerName AND status = 0")
    List<UserPreview> getActiveContacts(String ownerName);

    @Query("SELECT user_name, nick_name, profile_image_url FROM UserDB WHERE owner_name = " +
            ":ownerName AND status = 1")
    List<UserPreview> getRequestedContacts(String ownerName);

    @Query("SELECT user_name, nick_name, profile_image_url FROM UserDB WHERE owner_name = " +
            ":ownerName AND status = 2")
    List<UserPreview> getBlockedContacts(String ownerName);

    @Query("SELECT user_name, nick_name, profile_image_url FROM UserDB WHERE owner_name = " +
            ":ownerName AND status = 3")
    List<UserPreview> getPendingContacts(String ownerName);
}
