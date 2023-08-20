package com.meichinijiuchiquba.secumchat.net;

import com.meichinijiuchiquba.secumchat.model.AccessCode;
import com.meichinijiuchiquba.secumchat.model.AccessCodeRequest;
import com.meichinijiuchiquba.secumchat.model.AccessToken;
import com.meichinijiuchiquba.secumchat.model.AddContactRequest;
import com.meichinijiuchiquba.secumchat.model.ApproveContactRequest;
import com.meichinijiuchiquba.secumchat.model.BlockContactRequest;
import com.meichinijiuchiquba.secumchat.model.ContactInfos;
import com.meichinijiuchiquba.secumchat.model.CreateGroupRequest;
import com.meichinijiuchiquba.secumchat.model.DeleteContactRequest;
import com.meichinijiuchiquba.secumchat.model.EndMatch;
import com.meichinijiuchiquba.secumchat.model.GenericResponse;
import com.meichinijiuchiquba.secumchat.model.GetInfoRequest;
import com.meichinijiuchiquba.secumchat.model.GetMatch;
import com.meichinijiuchiquba.secumchat.model.GetMatchRequest;
import com.meichinijiuchiquba.secumchat.model.GetProfileFromUserNameRequest;
import com.meichinijiuchiquba.secumchat.model.GroupMessages;
import com.meichinijiuchiquba.secumchat.model.MessageGroup;
import com.meichinijiuchiquba.secumchat.model.NewUser;
import com.meichinijiuchiquba.secumchat.model.PendingRequests;
import com.meichinijiuchiquba.secumchat.model.PingResponse;
import com.meichinijiuchiquba.secumchat.model.PullGroupMessagesRequest;
import com.meichinijiuchiquba.secumchat.model.RegisterNotificationTokenRequest;
import com.meichinijiuchiquba.secumchat.model.ReportUserRequest;
import com.meichinijiuchiquba.secumchat.model.ReportUserResponse;
import com.meichinijiuchiquba.secumchat.model.SendMessageRequest;
import com.meichinijiuchiquba.secumchat.model.SendMessageResponse;
import com.meichinijiuchiquba.secumchat.model.UpdateUserRequest;
import com.meichinijiuchiquba.secumchat.model.User;
import com.meichinijiuchiquba.secumchat.model.UserNew;
import com.meichinijiuchiquba.secumchat.model.UserPublicInfo;
import com.meichinijiuchiquba.secumchat.model.UserRequest;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okio.Timeout;
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
    public Call<NewUser> registerNewUser(UserRequest request) {
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
    public Call<UserNew> getProfile() {
        return null;
    }

    @Override
    public Call<UserPublicInfo> getInfo(GetInfoRequest request) {
        return null;
    }

    @Override
    public Call<UserNew> updateUser(UpdateUserRequest request) {
        return null;
    }

    @Override
    public Call<ReportUserResponse> reportUser(ReportUserRequest request) {
        return null;
    }

    @Override
    public Call<ContactInfos> listContacts() {
        return null;
    }

    @Override
    public Call<PendingRequests> listPendingRequests() {
        return null;
    }

    @Override
    public Call<User> getProfileFromUserName(GetProfileFromUserNameRequest request) {
        return null;
    }

    @Override
    public Call<GenericResponse> addContact(AddContactRequest request) {
        return null;
    }

    @Override
    public Call<GenericResponse> loadBotChats(ApproveContactRequest request) {
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
    public Call<SendMessageResponse> sendMessage(SendMessageRequest request) {
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

    @Override
    public Call<GroupMessages> pullGroupMessages(PullGroupMessagesRequest request) {
        return null;
    }

    @Override
    public Call<MessageGroup> createGroup(CreateGroupRequest request) {
        return null;
    }

    @Override
    public Call<GenericResponse> loadBotChats() {
        return null;
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

        @Override
        public Timeout timeout() {
            return null;
        }
    }
}
