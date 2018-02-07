package com.shanjingtech.secumchat.net;

import com.shanjingtech.secumchat.model.AccessCode;
import com.shanjingtech.secumchat.model.AccessCodeRequest;
import com.shanjingtech.secumchat.model.AccessToken;
import com.shanjingtech.secumchat.model.AddContactRequest;
import com.shanjingtech.secumchat.model.ApproveContactRequest;
import com.shanjingtech.secumchat.model.BlockContactRequest;
import com.shanjingtech.secumchat.model.Contact;
import com.shanjingtech.secumchat.model.DeleteContactRequest;
import com.shanjingtech.secumchat.model.EndMatch;
import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.model.GetMatch;
import com.shanjingtech.secumchat.model.GetMatchRequest;
import com.shanjingtech.secumchat.model.GetProfileFromUserNameRequest;
import com.shanjingtech.secumchat.model.GroupMessages;
import com.shanjingtech.secumchat.model.ListContactsRequest;
import com.shanjingtech.secumchat.model.PingResponse;
import com.shanjingtech.secumchat.model.RegisterNotificationTokenRequest;
import com.shanjingtech.secumchat.model.ReportUserRequest;
import com.shanjingtech.secumchat.model.ReportUserResponse;
import com.shanjingtech.secumchat.model.SendMessageRequest;
import com.shanjingtech.secumchat.model.UpdateUserRequest;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserRequest;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Debug API to fake responses.
 */

public class SecumDebugAPI implements SecumAPI {
    @Override
    public Call<User> registerUser(UserRequest request) {
        return null;
    }

    @Override
    public Call<AccessCode> getAccessCode(AccessCodeRequest request) {
        return null;
    }

    @Override
    public Call<AccessToken> getAccessToken(String grantType, String userName, String password) {
        return null;
    }

    @Override
    public Call<GetMatch> getMatch(GetMatchRequest request) {
        return null;
    }

    @Override
    public Call<EndMatch> endMatch() {
        return null;
    }

    @Override
    public Call<PingResponse> ping() {
        return null;
    }

    @Override
    public Call<User> getProfile() {
        return null;
    }

    @Override
    public Call<User> updateUser(UpdateUserRequest request) {
        return null;
    }

    @Override
    public Call<ReportUserResponse> reportUser(ReportUserRequest request) {
        return null;
    }

    @Override
    public Call<List<Contact>> listContacts(ListContactsRequest request) {
        return null;
    }

    @Override
    public Call<User> getProfileFromUserName(GetProfileFromUserNameRequest request) {
        return null;
    }

    @Override
    public Call<List<GenericResponse>> addContact(AddContactRequest request) {
        return null;
    }

    @Override
    public Call<List<GenericResponse>> approveContact(ApproveContactRequest request) {
        return null;
    }

    @Override
    public Call<List<GenericResponse>> deleteContact(DeleteContactRequest request) {
        return null;
    }

    @Override
    public Call<List<GenericResponse>> blockContact(BlockContactRequest request) {
        return null;
    }

    @Override
    public Call<List<GenericResponse>> registerNotificationToken(RegisterNotificationTokenRequest
                                                                         request) {
        return null;
    }

    @Override
    public Call<GenericResponse> sendMessage(SendMessageRequest request) {
        return null;
    }

    public void fakePullMessage(boolean pullMessageSuccess) {
        this.pullMessageSuccess = pullMessageSuccess;

    }

    private boolean pullMessageSuccess;

    @Override
    public Call<GroupMessages> pullMessage() {
        return new DebugCall(pullMessageSuccess);
    }


    private class DebugCall implements Call {
        private boolean success;

        public DebugCall(boolean success) {
            this.success = success;
        }

        @Override
        public Response execute() throws IOException {
            return null;
        }

        @Override
        public void enqueue(Callback callback) {
            if (success) {
                callback.onResponse(null, null);
            } else {
                callback.onFailure(null, null);
            }
        }

        @Override
        public boolean isExecuted() {
            return false;
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Call clone() {
            return null;
        }

        @Override
        public Request request() {
            return null;
        }
    }
}
