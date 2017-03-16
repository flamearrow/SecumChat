package com.shanjingtech.secumchat.lifecycle;

import android.util.Log;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.shanjingtech.secumchat.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by flamearrow on 2/26/17.
 */

public class NonRTCMessageController {
    private static final String TAG = "NonRTCMessageController";

    public interface NonRTCMessageControllerCallbacks {
        /**
         * When I successfully subscribed to standy channel - notify
         */
        void onStandbySuccess(List<String> channel, PNStatus message);

        /**
         * When I failed to subscribe to standy channel - notify
         */
        void onStandbyFail(List<String> channel, PNStatus error);

        /**
         * When someone called my standby channel - show user someone is calling,
         * turn on the accept button
         */
        void onStandbyCalled(String channel, PNMessageResult message);

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
        void onCallingStandbyFailed(String channel, PNStatus error);

        /**
         * When callee's stand by channel receives my call - notify so that I know he sees accept
         * button
         */
        void onCalleeStandbyCalled(String channel, PNStatus message);
    }

    private String username;


    private NonRTCMessageControllerCallbacks callbacks;

    private PubNub pubnub;

    public NonRTCMessageController(String myName, PubNub pubnub,
                                   NonRTCMessageControllerCallbacks lifeCycleCallbacks) {
        this.username = myName;
        this.callbacks = lifeCycleCallbacks;
        this.pubnub = pubnub;
        // listens to message to stand by channel
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                List<String> channelNames = pubnub.getSubscribedChannels();
                if (status.isError()) {
                    callbacks.onStandbyFail(channelNames, status);
                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    callbacks.onStandbySuccess(channelNames, status);
                } else if (status.getCategory() == PNStatusCategory.PNAcknowledgmentCategory) {
                    if (status.getOperation() == PNOperationType.PNUnsubscribeOperation) {
                        Log.d(TAG, "Unsubscribe success");
                    }
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String channel = message.getChannel();
                if (channel.endsWith(Constants.STDBY_SUFFIX)) {
                    callbacks.onStandbyCalled(message.getChannel(), message);
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                String channelName = presence.getChannel();
            }
        });
    }

    /**
     * Subscribe to my standby by channel so that I'm online, others can call me
     */
    public void standBy() {
        String clientStdby = this.username + Constants.STDBY_SUFFIX;
        pubnub.subscribe().channels(Arrays.asList(clientStdby)).execute();
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
        pubnub.hereNow().channels(Arrays.asList(calleeStdBy)).includeUUIDs(true).async(new PNCallback<PNHereNowResult>() {
            @Override
            public void onResponse(PNHereNowResult result, PNStatus status) {
                // this callback tells us if callee is online
                try {
//                    int occupancy = ((JSONObject) message).getInt(Constants.JSON_OCCUPANCY);
                    int occupancy = result.getTotalOccupancy();
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


                    pubnub.publish().channel(calleeStdBy).message(jsonCall).async(new PNCallback<PNPublishResult>() {

                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            if (status.isError()) {
                                callbacks.onCallingStandbyFailed(calleeStdBy, status);
                            } else {
                                callbacks.onCalleeStandbyCalled(calleeStdBy, status);
                            }
                        }
                    });
                } catch (JSONException e) {
                    Log.d(TAG, "Json error happened when trying to dial");
                }
            }
        });

    }
}