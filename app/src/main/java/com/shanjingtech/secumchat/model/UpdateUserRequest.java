package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.shanjingtech.secumchat.net.SecumAPI#updateUser(UpdateUserRequest)}
 */
public class UpdateUserRequest {
    @Expose
    @SerializedName("age")
    String age;

    @Expose
    @SerializedName("gender")
    String gender;

    @Expose
    @SerializedName("lat")
    String lat;

    @Expose
    @SerializedName("lng")
    String lng;

    @Expose
    @SerializedName("nickname")
    String nickname;

    public static class Builder {
        private String age;
        private String gender;
        private String lat;
        private String lng;
        private String nickname;

        public UpdateUserRequest build() {
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.age = age;
            updateUserRequest.gender = gender;
            updateUserRequest.lat = lat;
            updateUserRequest.lng = lng;
            updateUserRequest.nickname = nickname;
            return updateUserRequest;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
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

        public Builder setLat(String lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLng(String lng) {
            this.lng = lng;
            return this;
        }
    }
}
