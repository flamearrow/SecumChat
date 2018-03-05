package com.shanjingtech.secumchat.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
    @Query("SELECT m.from_username, m.group_id, m.content, COUNT(*) AS unread_count, b" +
            ".total_count, m.time FROM MESSAGE m INNER JOIN (SELECT group_id, COUNT(*) as " +
            "total_count FROM MESSAGE WHERE owner_name = :ownerName GROUP BY group_id) b ON m" +
            ".group_id = b.group_id WHERE m.owner_name = :ownerName and m.read = 0 GROUP BY m" +
            ".group_id ORDER BY m.time")
    List<ConversationPreview> conversationPreviewOwnedBy(String ownerName);

    /**
     * {@link LiveData} version of {@link #conversationPreviewOwnedBy}
     */
    @Query("SELECT m.from_username, m.group_id, m.content, COUNT(*) AS unread_count, b" +
            ".total_count, m.time FROM MESSAGE m INNER JOIN (SELECT group_id, COUNT(*) as " +
            "total_count FROM MESSAGE WHERE owner_name = :ownerName GROUP BY group_id) b ON m" +
            ".group_id = b.group_id WHERE m.owner_name = :ownerName and m.read = 0 GROUP BY m" +
            ".group_id ORDER BY m.time")
    LiveData<List<ConversationPreview>> liveConversationPreviewOwnedBy(String ownerName);
}
