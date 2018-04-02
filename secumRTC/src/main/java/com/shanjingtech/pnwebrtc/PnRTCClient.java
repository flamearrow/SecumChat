package com.shanjingtech.pnwebrtc;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;

import java.util.Arrays;
import java.util.List;

/**
 * Created by flamearrow on 2/26/17.
 */

public class PnRTCClient {
    private PnSignalingParams pnSignalingParams;
    private PubNub mPubNub;
    private PnPeerConnectionClient pcClient;
    private String UUID;

    /**
     * pub, sub, sec key are all required.
     * Need to set secure to false.
     *
     * @param pubKey
     * @param subKey
     * @param secKey
     * @param UUID
     */
    public PnRTCClient(String pubKey, String subKey, String secKey, String UUID) {
        this(pubKey, subKey, secKey, UUID, PnSignalingParams.defaultInstance());
    }

    public PnRTCClient(String pubKey, String subKey, String secKey, String UUID,
                       PnSignalingParams signalingParams) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(subKey);
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSecretKey(secKey);
        pnConfiguration.setSecure(false);
        pnConfiguration.setUuid(UUID);
        this.mPubNub = new PubNub(pnConfiguration);
        this.pnSignalingParams = signalingParams;
        this.pcClient = new PnPeerConnectionClient(this.mPubNub, this.pnSignalingParams);
    }

    /**
     * Return the {@link com.shanjingtech.pnwebrtc.PnRTCClient} peer connection constraints.
     *
     * @return Peer Connection Constrains
     */
    public MediaConstraints pcConstraints() {
        return pnSignalingParams.pcConstraints;
    }

    /**
     * Return the {@link com.shanjingtech.pnwebrtc.PnRTCClient} video constraints.
     *
     * @return Video Constrains
     */
    public MediaConstraints videoConstraints() {
        return pnSignalingParams.videoConstraints;
    }

    /**
     * Return the {@link com.shanjingtech.pnwebrtc.PnRTCClient} audio constraints.
     *
     * @return Audio Constrains
     */
    public MediaConstraints audioConstraints() {
        return pnSignalingParams.audioConstraints;
    }

    /**
     * Return the {@link com.shanjingtech.pnwebrtc.PnRTCClient} Pubnub instance.
     *
     * @return The PnRTCClient's {@link PubNub} instance
     */
    public PubNub getPubNub() {
        return this.mPubNub;
    }

    /**
     * Return the UUID (username) of the {@link com.shanjingtech.pnwebrtc.PnRTCClient}. If not
     * provided by the constructor, a random phone number is generated and can be retrieived
     * with this method
     *
     * @return The UUID username of the client
     */
    public String getUUID() {
        return UUID;
    }


    /**
     * Need to attach mediaStream before you can connect.
     *
     * @param mediaStream Not null local media stream
     */
    public void attachLocalMediaStream(MediaStream mediaStream) {
        this.pcClient.setLocalMediaStream(mediaStream);
    }

    /**
     * Attach custom listener for callbacks!
     *
     * @param listener The listener which extends PnRTCListener to implement callbacks
     */
    public void attachRTCListener(PnRTCListener listener) {
        this.pcClient.addPnRTCListener(listener);
    }

    /**
     * Set the maximum simultaneous connections allowed
     *
     * @param max Max simultaneous connections
     */
    public void setMaxConnections(int max) {
        this.pcClient.MAX_CONNECTIONS = max;
    }

    /**
     * Subscribe to a channel using PubNub to listen for calls.
     *
     * @param channel The channel to listen on, your "phone number"
     */
    public void listenOn(String channel) {
        this.pcClient.listenOn(channel, false);
    }

    /**
     * Subscribe to a channel using PubNub to listen for calls, enable listen on different
     * channel
     *
     * @param channel The channel to listen on, your "phone number"
     */
    public void listenOnForce(String channel) {
        this.pcClient.listenOn(channel, true);
    }

    /**
     * Only subscribe to pubnub
     *
     * @param channel
     */
    public void subscribeToPubnubChannel(String channel) {
        mPubNub.subscribe().channels(Arrays.asList(channel)).execute();
    }

    /**
     * Connect with another user by their ID.
     *
     * @param userId The user to establish a WebRTC connection with
     */
    public void connect(String userId) {
        this.pcClient.connect(userId);
    }

    /**
     * Close a single peer connection. Send a PubNub hangup signal as well
     *
     * @param userId User to close a connection with
     */
    public void closeConnection(String userId) {
        this.pcClient.closeConnection(userId);
    }

    /**
     * Close all peer connections. Send a PubNub hangup signal as well.
     */
    public void closeAllConnections() {
        this.pcClient.closeAllConnections();
    }

    /**
     * Send a custom JSONObject user message to a single peer.
     *
     * @param userId  user to send a message to
     * @param message the JSON message to pass to a peer.
     */
    public void transmit(String userId, JSONObject message) {
        JSONObject usrMsgJson = new JSONObject();
        try {
            usrMsgJson.put(PnRTCMessage.JSON_USERMSG, message);
            this.pcClient.transmitMessage(userId, usrMsgJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a custom JSONObject user message to all peers.
     *
     * @param message the JSON message to pass to all peers.
     */
    public void transmitAll(JSONObject message) {
        List<PnPeer> peerList = this.pcClient.getPeers();
        for (PnPeer p : peerList) {
            transmit(p.getId(), message);
        }
    }

    private static String generateRandomNumber() {
        String areaCode = String.valueOf((int) (Math.random() * 1000));
        String digits = String.valueOf((int) (Math.random() * 10000));
        while (areaCode.length() < 3) areaCode += "0";
        while (digits.length() < 4) digits += "0";
        return areaCode + "-" + digits;
    }

    /**
     * Call this method in Activity.onDestroy() to close all open connections and clean up
     * instance for garbage collection.
     */
    public void onDestroy() {
        this.pcClient.closeAllConnections();
        this.mPubNub.unsubscribeAll();
    }

}
