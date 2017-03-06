package com.shanjingtech.secumchat.net;

import com.shanjingtech.secumchat.model.AccessCode;
import com.shanjingtech.secumchat.model.AccessCodeRequest;
import com.shanjingtech.secumchat.model.AccessToken;
import com.shanjingtech.secumchat.model.AccessTokenRequest;
import com.shanjingtech.secumchat.model.EndMatch;
import com.shanjingtech.secumchat.model.EndMatchRequest;
import com.shanjingtech.secumchat.model.GetMatchRequest;
import com.shanjingtech.secumchat.model.GetMatch;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * retrofit secum API defination
 */

public interface SecumAPI {
    //String BASE_URL = "https://www.yxg.me/api/";
    String BASE_URL = "http://59.110.93.125/";

    /**
     * Register a user through phone number and username
     *
     * @param request
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("/api/users/")
    Call<User> registerUser(@Body UserRequest request);

    /**
     * Send access code to user's phone through sms, confirm ownership
     */
    @Headers("Content-Type: application/json")
    @POST("api/users/get_access_code/")
    Call<AccessCode> getAccessCode(@Body AccessCodeRequest request);


    //TODO: figure out how to send with raw data(non json)
//    curl -X POST  -d "grant_type=password&username=1595005019&password=966468"
// -u"A7H5tpb2JQ7H66gPxbs6AAAVZgwtU12VPGoZpYUB
// :uVVN0JPW5piZf7nZglh92gleVJWnfAZguILU1Z25UR52yZa2lLFPjOtYJj42PiNF0GTQ32OdP8bQyHAbaHwkRArvhzDZDNVuBZqheflBVACDBjHrbedilUfon5JPIm6R" https://www.yxg.me/api/o/token/
    @POST("api/o/token/")
    Call<AccessToken> getAccessToken(@Body AccessTokenRequest request);

    /**
     * Request a match and make me happy
     */
    @Headers({
            "Content-Type: application/json",
            // TODO: pass in this programmatically
            "Authorization: Bearer RWmsVIZr46RFQ1j3mDDPa9DMS7YiLs"
    })
    @POST("api/matches/get_match/")
    Call<GetMatch> getMatch(@Body GetMatchRequest request);

    /**
     * Inform server a match is end
     */
    @Headers({
            "Content-Type: application/json",
            // TODO: pass in this programmatically
            "Authorization: Bearer RWmsVIZr46RFQ1j3mDDPa9DMS7YiLs"
    })
    @POST("api/matches/end_match/")
    Call<EndMatch> endMatch(@Body EndMatchRequest request);

}
