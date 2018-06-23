package com.shanjingtech.secumchat.pushy;


import android.content.Context;

import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.model.RegisterNotificationTokenRequest;
import com.shanjingtech.secumchat.net.SecumAPI;

import java.util.List;

import me.pushy.sdk.Pushy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushyInitializer {
    private static final String TAG = PushyInitializer.class.getCanonicalName();
    private Context context;
    private SecumAPI secumAPI;

    public PushyInitializer(Context context, SecumAPI secumAPI) {
        this.context = context;
        this.secumAPI = secumAPI;
    }

    public void setPushyInitializedCallback(PushyInitializedCallback pushyInitializedCallback) {
        this.pushyInitializedCallback = pushyInitializedCallback;
    }

    private PushyInitializedCallback pushyInitializedCallback;

    /**
     * Called when pushy returns current device token and token gets delivered to backend
     */
    public interface PushyInitializedCallback {
        void onPushyInitialized();

        void onPushyInitializeFailed();
    }

    /**
     * Start pushy long polling service, get current device token and send to backend.
     */
    public void initializePushy() {
        Pushy.listen(context);
        // TODO: request permission

        registerNotificationToken();
    }

    private void registerNotificationToken() {
        // Assign a unique token to this device
        new Thread(() -> {
            try {
                String deviceToken = Pushy.register(context);
                secumAPI.registerNotificationToken(new RegisterNotificationTokenRequest
                        (deviceToken)).enqueue(new Callback<List<GenericResponse>>() {
                    @Override
                    public void onResponse(Call<List<GenericResponse>> call,
                                           Response<List<GenericResponse>> response) {

                        if (pushyInitializedCallback == null) {
                            return;
                        }
                        pushyInitializedCallback.onPushyInitialized();
                    }

                    @Override
                    public void onFailure(Call<List<GenericResponse>> call, Throwable t) {
                        if (pushyInitializedCallback != null) {
                            pushyInitializedCallback.onPushyInitializeFailed();
                        }
                    }
                });
            } catch (Exception exc) {
                if (pushyInitializedCallback != null) {
                    pushyInitializedCallback.onPushyInitializeFailed();
                }
            }
        }).start();
    }

}
