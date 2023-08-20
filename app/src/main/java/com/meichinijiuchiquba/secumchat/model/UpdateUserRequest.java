package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.meichinijiuchiquba.secumchat.net.SecumAPI#updateUser(UpdateUserRequest)}
 * Note: only set the fields that need to be updated, unset fields are not touched.
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

    @Expose
    @SerializedName("profile_image_url")
    String profileImageUrl;

    public static class Builder {
        private String age;
        private String gender;
        private String lat;
        private String lng;
        private String nickname;
        private String profileImageUrl;


        public UpdateUserRequest build() {
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.age = age;
            updateUserRequest.gender = gender;
            updateUserRequest.lat = lat;
            updateUserRequest.lng = lng;
            updateUserRequest.nickname = nickname;
            updateUserRequest.profileImageUrl = profileImageUrl;
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

        public Builder setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
            return this;
        }
    }
}
