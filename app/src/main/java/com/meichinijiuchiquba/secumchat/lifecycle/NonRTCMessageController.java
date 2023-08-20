package com.meichinijiuchiquba.secumchat.lifecycle;

import android.util.Log;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.meichinijiuchiquba.pnwebrtc.PnPeerConnectionClient;
import com.meichinijiuchiquba.secumchat.model.User;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Handle Pubnub messages
 */

public class NonRTCMessageController {
    private static final String TAG = "NonRTCMessageController";

    public interface NonRTCMessageControllerCallbacks {
        /**
         * When callee's is online
         */
        void onCalleeOnline(String calleeName);

        /**
         * When callee's is offline
         */
        void onCalleeOffline(String calleeName);
    }

    private String username;
    private User currentUser;

    private NonRTCMessageControllerCallbacks callbacks;

    private PubNub pubnub;

    public NonRTCMessageController(User currentuser, PubNub pubnub,
                                   NonRTCMessageControllerCallbacks lifeCycleCallbacks) {
        this.currentUser = currentuser;
        this.username = currentuser.getUsername();
        this.callbacks = lifeCycleCallbacks;
        this.pubnub = pubnub;
    }

    /**
     * Send a hangup message to peer's own channel
     *
     * @param peerName
     */
    public void hangUp(final String peerName) {
        JSONObject hangupMsg = PnPeerConnectionClient.generateHangupPacket(username);
        pubnub.publish().channel(peerName).message(hangupMsg).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if (status.isError()) {
                    Log.d(TAG, "hangUp failed!");
                } else {
                    Log.d(TAG, "hangUp succeeded!");
                }
            }
        });
    }

    /**
     * Send a addtime message to peer's own channel, they'll be notified peerAdd
     *
     * @param peerName
     */
    public void addTime(final String peerName) {
        JSONObject addtimeMsg = PnPeerConnectionClient.generateAddtimePacket(username);
        pubnub.publish().channel(peerName).message(addtimeMsg).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if (status.isError()) {
                    Log.d(TAG, "addTime failed!");
                } else {
                    Log.d(TAG, "addTime succeeded!");
                }
            }
        });
    }

    /**
     * Dial peer's pubnub channel so that they know someone is calling
     *
     * @param peerName
     */
    public void dial(final String peerName) {
        pubnub.hereNow().channels(Arrays.asList(peerName)).includeUUIDs(true).async(
                new PNCallback<PNHereNowResult>() {
                    @Override
                    public void onResponse(PNHereNowResult result, PNStatus status) {
                        // this callback tells us if callee is online
                        int occupancy = result.getTotalOccupancy();
                        // if callee is offline we just don't try to call him
                        if (occupancy == 0) {
                            callbacks.onCalleeOffline(peerName);
                            return;
                        }
                        // otherwise go ahead call callee
                        else {
                            callbacks.onCalleeOnline(peerName);
                        }
                        JSONObject dialMsg = PnPeerConnectionClient.generateDialPacket
                                (currentUser.getUsername(), currentUser.getNickname(),
                                        currentUser.getGender());
                        pubnub.publish().channel(peerName).message(dialMsg).async(
                                new PNCallback<PNPublishResult>() {
                                    @Override
                                    public void onResponse(PNPublishResult result, PNStatus
                                            status) {
                                        if (status.isError()) {
                                            Log.d(TAG, "dial failed!");
                                        } else {
                                            Log.d(TAG, "dial succeeded!");
                                        }
                                    }
                                });
                    }
                });


    }
}