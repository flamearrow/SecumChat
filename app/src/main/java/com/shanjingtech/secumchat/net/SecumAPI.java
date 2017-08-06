package com.shanjingtech.secumchat.net;

import com.shanjingtech.secumchat.model.AccessCode;
import com.shanjingtech.secumchat.model.AccessCodeRequest;
import com.shanjingtech.secumchat.model.AccessToken;
import com.shanjingtech.secumchat.model.AddContactRequest;
import com.shanjingtech.secumchat.model.Contact;
import com.shanjingtech.secumchat.model.EndMatch;
import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.model.GetMatch;
import com.shanjingtech.secumchat.model.GetMatchRequest;
import com.shanjingtech.secumchat.model.GetProfileFromUserNameRequest;
import com.shanjingtech.secumchat.model.ListContactsRequest;
import com.shanjingtech.secumchat.model.PingResponse;
import com.shanjingtech.secumchat.model.ReportUserRequest;
import com.shanjingtech.secumchat.model.ReportUserResponse;
import com.shanjingtech.secumchat.model.UpdateUserRequest;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserRequest;
import com.shanjingtech.secumchat.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * retrofit secum API defination
 */

public interface SecumAPI {
    public static final String MATCH_ACTIVE = "MATCH_ACTIVE";
    public static final String MATCH_NOT_AVAILABLE = "MATCH_NOT_AVAILABLE";
    public static final String MATCH_INVALID = "MATCH_INVALID";

    public static final String TAG = "SecumAPI";

    String BASE_URL = "https://www.shanjingtech.com/";
    String USER_NAME = "A7H5tpb2JQ7H66gPxbs6AAAVZgwtU12VPGoZpYUB";
    String PASSWORD =
            "uVVN0JPW5piZf7nZglh92gleVJWnfAZguILU1Z25UR52yZa2lLFPjOtYJj42PiNF0GTQ32OdP8bQyHAbaHwkRArvhzDZDNVuBZqheflBVACDBjHrbedilUfon5JPIm6R";

    /**
     * Register a user through phone number and username
     *
     * @param request
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_REGISTER_USER)
    Call<User> registerUser(@Body UserRequest request);

    /**
     * Send access code to user's phone through sms, confirm ownership
     */
    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_GET_ACCESS_CODE)
    Call<AccessCode> getAccessCode(@Body AccessCodeRequest request);


    @POST(Constants.PATH_GET_ACCESS_TOKEN)
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(@Field("grant_type") String grantType, @Field("username")
            String userName, @Field("password") String password);

    /**
     * Request a match and make me happy
     */
    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_GET_MATCH)
    Call<GetMatch> getMatch(@Body GetMatchRequest request);

    /**
     * Inform server a match is end
     */
    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_END_MATCH)
    Call<EndMatch> endMatch();

    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_PING)
    Call<PingResponse> ping();


    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_GET_PROFILE)
    Call<User> getProfile();

    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_UPDATE_USER)
    Call<User> updateUser(@Body UpdateUserRequest request);

    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_REPORT)
    Call<ReportUserResponse> reportUser(@Body ReportUserRequest request);

    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_LIST_CONTACTS)
    Call<List<Contact>> listContacts(@Body ListContactsRequest request);

    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_PROFILE_FROM_USERNAME)
    Call<User> getProfileFromUserName(@Body GetProfileFromUserNameRequest request);

    @Headers("Content-Type: application/json")
    @POST(Constants.PATH_ADD_CONTACT)
    Call<List<GenericResponse>> addContact(@Body AddContactRequest request);
}
