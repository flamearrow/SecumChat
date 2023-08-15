package com.shanjingtech.secumchat.util;

import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.net.SecumAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtils {

    public interface APICallback {
        void onSuccess();

        void onFailure();
    }

    public static void loadBotChats(SecumAPI secumAPI, APICallback callback) {
        secumAPI.loadBotChats().enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
