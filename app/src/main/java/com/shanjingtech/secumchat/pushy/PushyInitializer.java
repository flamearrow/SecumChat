package com.shanjingtech.secumchat.pushy;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import me.pushy.sdk.Pushy;

public class PushyInitializer {
    private static final String TAG = PushyInitializer.class.getCanonicalName();
    private Context context;

    public PushyInitializer(Context context) {
        this.context = context;
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
        new RegisterForPushNotificationsAsync().execute();
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void,
            Exception> {

        protected Exception doInBackground(Void... params) {
            try {
                // Assign a unique token to this device
                String deviceToken = Pushy.register(context);

                // Log it for debugging purposes
//                Toast.makeText(context, "Pushy device token: " + deviceToken, Toast.LENGTH_SHORT)
//                        .show();


                int i = 23;
                int j = 24;
                // send it to backend
            } catch (Exception exc) {
                // Return exc to onPostExecute
                return exc;
            }

            // Success
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            if (pushyInitializedCallback == null) {
                return;
            }
            if (exc != null) {
                // Show error as toast message
                Log.d(TAG, exc.toString());
                pushyInitializedCallback.onPushyInitializeFailed();
            } else {
                pushyInitializedCallback.onPushyInitialized();
            }

        }
    }

}

