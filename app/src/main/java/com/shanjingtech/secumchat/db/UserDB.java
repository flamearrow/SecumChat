package com.shanjingtech.secumchat.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.shanjingtech.secumchat.model.User;

/**
 * Model used for database
 */
@Entity
public class UserDB {
    public static class Builder {
        private String ownerName;

        private String userName;

        private String nickName;

        private String age;

        private String gender;

        private String email;

        private String phone;

        private int status;

        private String profileImageUrl;

        public Builder setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
            return this;
        }

        public Builder setOwnerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setNickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder setAge(String age) {
            this.age = age;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public UserDB build() {
            UserDB userDB = new UserDB();
            userDB.setAge(age);
            userDB.setEmail(email);
            userDB.setOwnerName(ownerName);
            userDB.setPhone(phone);
            userDB.setStatus(status);
            userDB.setUserName(userName);
            userDB.setNickName(nickName);
            userDB.setGender(gender);
            userDB.setProfileImageUrl(profileImageUrl);
            return userDB;
        }

        public Builder setUser(User user) {
            return setAge(user.getAge()).setEmail(user.getEmail()).setPhone(user.getPhone())
                    .setUserName(user.getUsername()).setNickName(user.getNickname()).setGender
                            (user.getGender()).setProfileImageUrl(user.getProfile_image_url());
        }
    }

    @ColumnInfo(name = "owner_name")
    private String ownerName;

    @PrimaryKey
    @ColumnInfo(name = "user_name")
    private @NonNull
    String userName;

    @ColumnInfo(name = "nick_name")
    private String nickName;

    @ColumnInfo(name = "profile_image_url")
    private String profileImageUrl;

    @ColumnInfo(name = "age")
    private String age;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "status")
    private @ContactStatus
    int status;

    @IntDef({ContactStatus.CONTACT_STATUS_ACTIVE, ContactStatus.CONTACT_STATUS_REQUESTED,
            ContactStatus.CONTACT_STATUS_BLOCKED, ContactStatus.CONTACT_STATUS_PENDING,
            ContactStatus.CONTACT_STATUS_UNKNOWN})
    public @interface ContactStatus {
        int CONTACT_STATUS_ACTIVE = 0;
        int CONTACT_STATUS_REQUESTED = 1;
        int CONTACT_STATUS_BLOCKED = 2;
        int CONTACT_STATUS_PENDING = 3;
        int CONTACT_STATUS_UNKNOWN = 4;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
