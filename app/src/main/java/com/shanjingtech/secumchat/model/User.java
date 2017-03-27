package com.shanjingtech.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.shanjingtech.secumchat.net.SecumAPI#registerUser(UserRequest)}
 */
public class User {
    @SerializedName("username")
    @Expose
    String username;

    @SerializedName("third_party_uid")
    @Expose
    String third_party_uid;

    @SerializedName("third_party_auth")
    @Expose
    String third_party_auth;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("profile_image_url")
    @Expose
    String profile_image_url;

    @SerializedName("age")
    @Expose
    String age;

    @SerializedName("gender")
    @Expose
    String gender;

    @SerializedName("lat")
    @Expose
    String lat;

    @SerializedName("lng")
    @Expose
    String lng;

    @SerializedName("nickname")
    @Expose
    String nickname;

    @SerializedName("access_code")
    @Expose
    String access_code;

    @SerializedName("num_comments")
    @Expose
    String num_comments;

    @SerializedName("num_posts")
    @Expose
    String num_posts;

    @SerializedName("num_following")
    @Expose
    String num_following;

    @SerializedName("num_followed")
    @Expose
    String num_followed;

    @SerializedName("time_created")
    @Expose
    String time_created;

    @SerializedName("time_updated")
    @Expose
    String time_updated;

    public String getUsername() {
        return username;
    }

    public String getThird_party_uid() {
        return third_party_uid;
    }

    public String getThird_party_auth() {
        return third_party_auth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAccess_code() {
        return access_code;
    }

    public String getNum_comments() {
        return num_comments;
    }

    public String getNum_posts() {
        return num_posts;
    }

    public String getNum_following() {
        return num_following;
    }

    public String getNum_followed() {
        return num_followed;
    }

    public String getTime_created() {
        return time_created;
    }

    public String getTime_updated() {
        return time_updated;
    }
/* sample json response
    {
       "username":"phone_+16503181659",
       "third_party_uid":null,
       "third_party_auth":null,
       "email":"",
       "phone":"+16503181659",
       "profile_image_url":null,
       "age":null,
       "gender":null,
       "lat":null,
       "lng":null,
       "nickname":null,
       "access_code":null,
       "num_comments":0,
       "num_posts":19,
       "num_following":0,
       "num_followed":0,
       "time_created":"2017-03-22T07:07:58.750586Z",
       "time_updated":"2017-03-27T00:19:58.919426Z"
    }
 */
}
