package com.shanjingtech.secumchat.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MessageDAO {

    /**
     * Returns a list of rowIds
     *
     * @param messages
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertMessages(List<Message> messages);

    /**
     * Returns rowId?
     *
     * @param message
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMessage(Message message);

    /**
     * Returns number of rows deleted
     */
    @Delete
    int deleteMessages(Message... messages);

    /**
     * Dummy load method
     */
    @Query("SELECT * FROM Message")
    Message[] loadAllMessages();

    /**
     * Load all messages owned by ownerName;
     */
    @Query("SELECT * FROM Message WHERE owner_name = :ownerName")
    List<Message> loadAllMessagesOwnedBy(String ownerName);

    /**
     * Load all messages owned by ownerName;
     */
    @Query("SELECT * FROM Message WHERE owner_name = :ownerName")
    List<Message> loadAllMessagesOwnedByOrder(String ownerName);

    /**
     * Load all message in a group id order by time, used in conversation history.
     */
    @Query("SELECT * FROM MESSAGE WHERE group_id = :groupId ORDER BY time")
    List<Message> historyWithGroupId(String groupId);


    /**
     * {@link LiveData} version of {@link #historyWithGroupId}
     */
    @Query("SELECT * FROM MESSAGE WHERE group_id = :groupId ORDER BY time")
    LiveData<List<Message>> liveHistoryWithGroupId(String groupId);


    /**
     * Load all unread messages in a group id.
     */
    @Query("SELECT * FROM MESSAGE WHERE group_id = :groupId AND read = 0")
    List<Message> unreadHistoryWithGroupId(String groupId);

    /**
     * Update the mawfakus.
     */
    @Update
    int updateMessages(List<Message> messages);

    /**
     * Group all unread messages, select peerName, group_id, last message and unread count.
     * Used in preview
     */
    @Query("SELECT from_username, group_id, content, COUNT(*) AS unread_count FROM MESSAGE WHERE " +
            "owner_name = :ownerName AND read = 0 GROUP BY group_id ORDER BY time")
    List<UnreadPreview> unreadPreviewOwnedBy(String ownerName);

    /**
     * Group all unread messages, find unread message count, read message count, last content, peer
     * user etc.
     */
    @Query("SELECT m.from_username, m.to_username, u.nick_name AS from_nickname, u2.nick_name AS " +
            "to_nickname, m.owner_name, m.group_id, m.content, b.unread_count, COUNT(*) AS " +
            "total_count, m.time FROM UserDB u, UserDB u2, MESSAGE m LEFT JOIN (SELECT group_id, " +
            "COUNT(*) as unread_count FROM MESSAGE WHERE owner_name = :ownerName AND read = 0 " +
            "GROUP BY group_id) b ON m.group_id = b.group_id WHERE m.owner_name = :ownerName AND " +
            "m.from_username = u.user_name AND u.owner_name = :ownerName AND m.to_username = " +
            "u2.user_name AND u2.owner_name = :ownerName GROUP BY m.group_id ORDER BY m.time")
    List<ConversationPreview> conversationPreviewOwnedBy(String ownerName);

    /**
     * {@link LiveData} version of {@link #conversationPreviewOwnedBy}
     */
    @Query("SELECT m.from_username, m.to_username, u.nick_name AS from_nickname, u2.nick_name AS " +
            "to_nickname, m.owner_name, m.group_id, m.content, b.unread_count, COUNT(*) AS " +
            "total_count, m.time FROM UserDB u, UserDB u2, MESSAGE m LEFT JOIN (SELECT group_id, " +
            "COUNT(*) as unread_count FROM MESSAGE WHERE owner_name = :ownerName AND read = 0 " +
            "GROUP BY group_id) b ON m.group_id = b.group_id WHERE m.owner_name = :ownerName AND " +
            "m.from_username = u.user_name AND u.owner_name = :ownerName AND m.to_username = " +
            "u2.user_name AND u2.owner_name = :ownerName GROUP BY m.group_id ORDER BY m.time")
    LiveData<List<ConversationPreview>> liveConversationPreviewOwnedBy(String ownerName);

    /**
     * Find if owner has ever chatted with peer, if so, return the unique groupId.
     */
    @Query("SELECT group_id FROM MESSage WHERE owner_name = :ownerName AND " +
            "(from_username=:peerName OR to_username=:peerName) GROUP BY group_id")
    GroupId findChatWithUserOwnedBy(String ownerName, String peerName);

}
