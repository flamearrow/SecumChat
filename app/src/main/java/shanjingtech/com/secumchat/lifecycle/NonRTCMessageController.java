package shanjingtech.com.secumchat.lifecycle;

import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

import shanjingtech.com.secumchat.util.Constants;

/**
 * Created by flamearrow on 2/26/17.
 */

public class NonRTCMessageController {
    private static final String TAG = "NonRTCMessageController";

    public interface NonRTCMessageControllerCallbacks {
        /**
         * When I successfully subscribed to standy channel - notify
         */
        void onStandbySuccess(String channel, Object message);

        /**
         * When I failed to subscribe to standy channel - notify
         */
        void onStandbyFail(String channel, PubnubError error);

        /**
         * When someone called my standby channel - show user someone is calling,
         * turn on the accept button
         */
        void onStandbyCalled(String channel, Object message);

        /**
         * When callee's standby channel is online
         */
        void onCalleeOnline(String calleeName);

        /**
         * When callee's standby channel is offline
         */
        void onCalleeOffline(String calleeName);

        /**
         * When I failed to call someone's standby channel - notify
         */
        void onCallingStandbyFailed(String channel, PubnubError error);

        /**
         * When callee's stand by channel receives my call - notify so that I know he sees accept
         * button
         */
        void onCalleeStandbyCalled(String channel, Object message);
    }

    private String username;


    private NonRTCMessageControllerCallbacks callbacks;

    private Pubnub pubnub;

    public NonRTCMessageController(String myName, Pubnub pubnub,
                                   NonRTCMessageControllerCallbacks lifeCycleCallbacks) {
        this.username = myName;
        this.callbacks = lifeCycleCallbacks;
        this.pubnub = pubnub;
    }

    /**
     * Subscribe to my standby by channel so that I'm online, others can call me
     */
    public void standBy() {
        String clientStdby = this.username + Constants.STDBY_SUFFIX;
        try {
            pubnub.subscribe(clientStdby, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    callbacks.onStandbyCalled(channel, message);
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    callbacks.onStandbySuccess(channel, message);
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    callbacks.onStandbyFail(channel, error);
                }
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unsubscirbe to my standby channel
     */
    public void unStandby() {
        pubnub.unsubscribeAll();
    }

    /**
     * Dial a callee, they'll be notified someone is calling
     *
     * @param calleeNumber
     */
    public void dial(final String calleeNumber) {
        // dial through their standy channel
        final String calleeStdBy = calleeNumber + Constants.STDBY_SUFFIX;
        // TODO: reject if null
        pubnub.hereNow(calleeStdBy, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                // this callback tells us if callee is online
                try {
                    int occupancy = ((JSONObject) message).getInt(Constants.JSON_OCCUPANCY);
                    // if callee is offline we just don't try to call him
                    if (occupancy == 0) {
                        callbacks.onCalleeOffline(calleeNumber);
                        return;
                    }
                    // otherwise go ahead call callee
                    else {
                        callbacks.onCalleeOnline(calleeNumber);
                    }

                    JSONObject jsonCall = new JSONObject();
                    jsonCall.put(Constants.JSON_CALL_USER, username);
                    jsonCall.put(Constants.JSON_CALL_TIME, System.currentTimeMillis());

                    pubnub.publish(calleeStdBy, jsonCall, new Callback() {
                        @Override
                        public void successCallback(String channel, Object message) {
                            callbacks.onCalleeStandbyCalled(channel, message);
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            super.errorCallback(channel, error);
                            callbacks.onCallingStandbyFailed(channel, error);
                        }
                    });
                } catch (JSONException e) {
                    Log.d(TAG, "Json error happened when trying to dial");
                }
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
                callbacks.onCallingStandbyFailed(channel, error);
            }
        });

    }
}