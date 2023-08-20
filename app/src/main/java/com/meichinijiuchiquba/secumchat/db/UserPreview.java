package com.meichinijiuchiquba.secumchat.db;


import androidx.room.ColumnInfo;

/**
 * A simplified view for displaying users, used for displaying different types of contacts. See
 * {@link com.meichinijiuchiquba.secumchat.db.UserDB.ContactStatus} for all four types.
 */
public class UserPreview {
    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "nick_name")
    private String nickName;

    @ColumnInfo(name = "profile_image_url")
    private String profileImageUrl;

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
