package com.shanjingtech.secumchat.net;

import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * retrofit secum API defination
 */

public interface SecumAPI {
    //String BASE_URL = "https://www.yxg.me/api/";
    String BASE_URL = "http://59.110.93.125/";

    @Headers("Content-Type: application/json")
    @POST("/api/users/")
    Call<User> registerUser(@Body UserRequest request);

//    @Headers("Content-Type: application/json")
//    @FormUrlEncoded
//    @POST("/api/users/")
//    Call<User> registerUser(@Field("username") String username, @Field("phone") String phone);
}
