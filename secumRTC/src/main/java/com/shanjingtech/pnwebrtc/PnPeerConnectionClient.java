package com.shanjingtech.pnwebrtc;

import android.util.Log;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.shanjingtech.pnwebrtc.utils.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.shanjingtech.pnwebrtc.PnRTCMessage.JSON_MESSAGE_GROUP_ID;
import static com.shanjingtech.pnwebrtc.PnRTCMessage.JSON_SENDER_GENDER;
import static com.shanjingtech.pnwebrtc.PnRTCMessage.JSON_SENDER_NICK_NAME;
import static com.shanjingtech.pnwebrtc.PnRTCMessage.META_MSG;

/**
 * Created by flamearrow on 2/26/17.
 */

public class PnPeerConnectionClient {
    private SessionDescription localSdp = null; // either offer or answer SDP
    private MediaStream localMediaStream = null;
    PeerConnectionFactory pcFactory;
    Set<PnRTCListener> mRtcListeners;
    PnSignalingParams signalingParams;
    int MAX_CONNECTIONS = Integer.MAX_VALUE;

    private PubNub mPubNub;
    private Map<String, PnAction> actionMap;
    private Map<String, PnPeer> peers;
    private String id;

    public PnPeerConnectionClient(PubNub pubnub, PnSignalingParams signalingParams) {
        this.mPubNub = pubnub;
        this.signalingParams = signalingParams;
        this.pcFactory = new PeerConnectionFactory(); // TODO: Check it allowed, else extra param
        this.peers = new HashMap<>();
        mRtcListeners = Collections.newSetFromMap(new ConcurrentHashMap<PnRTCListener, Boolean>());
        init();
    }

    private void init() {
        this.actionMap = new HashMap<>();
        this.actionMap.put(CreateOfferAction.TRIGGER, new CreateOfferAction());
        this.actionMap.put(CreateAnswerAction.TRIGGER, new CreateAnswerAction());
        this.actionMap.put(SetRemoteSDPAction.TRIGGER, new SetRemoteSDPAction());
        this.actionMap.put(AddIceCandidateAction.TRIGGER, new AddIceCandidateAction());
        this.actionMap.put(PnUserHangupAction.TRIGGER, new PnUserHangupAction());
        this.actionMap.put(PnUserMessageAction.TRIGGER, new PnUserMessageAction());
        this.actionMap.put(PnUserAddtimeAction.TRIGGER, new PnUserAddtimeAction());
        this.actionMap.put(PnUserDialAction.TRIGGER, new PnUserDialAction());

        mPubNub.addListener(new PnRTCReceiver());
    }

    // if force, enable it to listen on different channels
    boolean listenOn(String myId, boolean force) {  // Todo: return success?
        if (localMediaStream == null) {       // Not true for streaming?
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onDebug(new PnRTCMessage("Need to add media stream before you can " +
                        "connect."));
            }
            return false;
        }
        if (!force && this.id != null) {  // Prevent listening on multiple channels.
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onDebug(new PnRTCMessage("Already listening on " + this.id + ". " +
                        "Cannot have multiple connections."));
            }
            return false;
        }

        this.id = myId;
        subscribe(myId);
        return true;
    }

    /**
     * TODO: Add a max user threshold.
     * Connect with another user by their ID.
     *
     * @param userId The user to establish a WebRTC connection with
     * @return boolean value of success
     */
    boolean connect(String userId) {
//        if (peers.containsKey(userId)) {
//            removePeer(userId);
//        }
        if (!peers.containsKey(userId)) { // Prevents duplicate dials.
            if (peers.size() < MAX_CONNECTIONS) {
                PnPeer peer = addPeer(userId);
                peer.pc.addStream(this.localMediaStream);
                try {
                    actionMap.get(CreateOfferAction.TRIGGER).execute(userId, new JSONObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        for (PnRTCListener pnRTCListener : mRtcListeners) {
            pnRTCListener.onDebug(new PnRTCMessage("CONNECT FAILED. Duplicate dial or max peer " +
                    "connections exceeded. Max: " + MAX_CONNECTIONS + " Current: " + this.peers
                    .size()));
        }
        return false;
    }

    public void addPnRTCListener(PnRTCListener listener) {
        this.mRtcListeners.add(listener);
    }

    public void removePnRTCListener(PnRTCListener listener) {
        this.mRtcListeners.remove(listener);
    }

    private void subscribe(String channel) {

        mPubNub.subscribe().channels(Arrays.asList(channel)).execute();
    }

    public void setLocalMediaStream(MediaStream localStream) {
        this.localMediaStream = localStream;
        for (PnRTCListener pnRTCListener : mRtcListeners) {
            pnRTCListener.onLocalStream(localStream);
        }
    }

    public MediaStream getLocalMediaStream() {
        return this.localMediaStream;
    }

    private PnPeer addPeer(String id) {
        PnPeer peer = new PnPeer(id, this);
        peers.put(id, peer);
        return peer;
    }

    PnPeer removePeer(String id) {
        PnPeer peer = peers.get(id);
        peer.pc.close();
        return peers.remove(peer.id);
    }

    List<PnPeer> getPeers() {
        return new ArrayList<>(this.peers.values());
    }

    /**
     * Close connection (hangup) no a certain peer.
     *
     * @param id PnPeer id to close connection with
     */
    public void closeConnection(String id) {
        JSONObject packet = new JSONObject();
        try {
            if (!this.peers.containsKey(id)) return;
            PnPeer peer = this.peers.get(id);
            peer.hangup();
            packet.put(PnRTCMessage.JSON_TYPE, PnRTCMessage.JSON_HANGUP);
            transmitMessage(id, packet);
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onPeerConnectionClosed(peer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close connections (hangup) on all open connections.
     */
    public void closeAllConnections() {
        Set<String> peerIds = new HashSet<>((this.peers.keySet()));
        for (String peerId : peerIds) {
            closeConnection(peerId);
        }
    }

    /**
     * Send SDP Offers/Answers nd ICE candidates to peers.
     *
     * @param toID   The id or "number" that you wish to transmit a message to.
     * @param packet The JSON data to be transmitted
     */
    void transmitMessage(String toID, JSONObject packet) {
        if (this.id == null) { // Not logged in. Put an error in the debug cb.
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onDebug(new PnRTCMessage("Cannot transmit before calling Client" +
                        ".connect"));
            }
        }
        try {
            JSONObject message = new JSONObject();
            message.put(PnRTCMessage.JSON_PACKET, packet);
            message.put(PnRTCMessage.JSON_ID, ""); //Todo: session id, unused in js SDK?
            message.put(PnRTCMessage.JSON_SENDER_ID, this.id);
            this.mPubNub.publish().channel(toID).message(message).async(new PNCallback<PNPublishResult>() {

                @Override
                public void onResponse(PNPublishResult result, PNStatus status) {
                    if (status.isError()) {
                        for (PnRTCListener pnRTCListener : mRtcListeners) {
                            pnRTCListener.onDebug(new PnRTCMessage(status.toString()));
                        }
                    } else {
                        for (PnRTCListener pnRTCListener : mRtcListeners) {
                            pnRTCListener.onDebug(new PnRTCMessage(status.toString() + " : " +
                                    result.toString()));
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private interface PnAction {
        void execute(String peerId, JSONObject payload) throws JSONException;
    }

    private static final String PNACTION = "PnAction";

    private class CreateOfferAction implements PnAction {
        public static final String TRIGGER = "init";

        public void execute(String peerId, JSONObject payload) throws JSONException {
            Log.d(PNACTION, "CreateOfferAction");
            PnPeer peer = peers.get(peerId);
            peer.setDialed(true);
            peer.setType(PnPeer.TYPE_ANSWER);
            peer.pc.createOffer(peer, signalingParams.pcConstraints);
        }
    }

    private class CreateAnswerAction implements PnAction {
        public static final String TRIGGER = "offer";

        public void execute(String peerId, JSONObject payload) throws JSONException {
            Log.d(PNACTION, "CreateAnswerAction");
            PnPeer peer = peers.get(peerId);
            peer.setType(PnPeer.TYPE_OFFER);
            peer.setStatus(PnPeer.STATUS_CONNECTED);
            SessionDescription sdp = new SessionDescription(
                    SessionDescription.Type.fromCanonicalForm(payload.getString("type")),
                    payload.getString("sdp")
            );
            peer.pc.setRemoteDescription(peer, sdp);
            peer.pc.createAnswer(peer, signalingParams.pcConstraints);
        }
    }

    private class SetRemoteSDPAction implements PnAction {
        public static final String TRIGGER = "answer";

        public void execute(String peerId, JSONObject payload) throws JSONException {
            Log.d(PNACTION, "SetRemoteSDPAction");
            PnPeer peer = peers.get(peerId);
            SessionDescription sdp = new SessionDescription(
                    SessionDescription.Type.fromCanonicalForm(payload.getString("type")),
                    payload.getString("sdp")
            );
            peer.pc.setRemoteDescription(peer, sdp);
        }
    }

    private class AddIceCandidateAction implements PnAction {
        public static final String TRIGGER = "candidate";

        public void execute(String peerId, JSONObject payload) throws JSONException {
            Log.d(PNACTION, "AddIceCandidateAction");
            PeerConnection pc = peers.get(peerId).pc;
            if (pc.getRemoteDescription() != null) {
                IceCandidate candidate = new IceCandidate(
                        payload.getString("sdpMid"),
                        payload.getInt("sdpMLineIndex"),
                        payload.getString("candidate")
                );
                pc.addIceCandidate(candidate);
            }
        }
    }

    private class PnUserHangupAction implements PnAction {
        public static final String TRIGGER = PnRTCMessage.JSON_HANGUP;

        public void execute(String peerId, JSONObject payload) throws JSONException {
            Log.d(PNACTION, "PnUserHangupAction");
            PnPeer peer = peers.get(peerId);
            peer.hangup();
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onPeerConnectionClosed(peer);
            }
            // Todo: Consider Callback?
        }
    }

    private class PnUserMessageAction implements PnAction {
        public static final String TRIGGER = PnRTCMessage.JSON_USERMSG;

        public void execute(String peerId, JSONObject payload) throws JSONException {
            Log.d(PNACTION, "PnUserMessageAction");
            String message = payload.getString(PnRTCMessage.JSON_MESSAGE);
            String from = payload.getString(PnRTCMessage.JSON_SENDER_ID);
            String groupId = payload.getString(JSON_MESSAGE_GROUP_ID);
            long time = payload.getLong(PnRTCMessage.JSON_TIME);
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onMessage(message, from, groupId, time);
            }
        }
    }

    private class PnUserAddtimeAction implements PnAction {
        public static final String TRIGGER = PnRTCMessage.JSON_ADDTIME;

        public void execute(String peerId, JSONObject payload) throws JSONException {
            Log.d(PNACTION, "PnUserAddtimeAction");
            PnPeer peer = peers.get(peerId);
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onAddTime(peer);
            }
        }
    }

    private class PnUserDialAction implements PnAction {
        public static final String TRIGGER = PnRTCMessage.JSON_DIAL;

        @Override
        public void execute(String callerId, JSONObject payload) {
            Log.d(PNACTION, "PnUserDialAction");
            String senderNickName = null;
            String senderGender = null;
            try {
                senderNickName = payload.getString(JSON_SENDER_NICK_NAME);
                senderGender = payload.getString(JSON_SENDER_GENDER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (senderNickName == null) {
                senderNickName = callerId;
            }
            if (senderGender == null) {
                senderGender = "female";
            }
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onDialed(callerId, senderNickName, senderGender);
            }
        }
    }

    /**
     * @param userId Your id. Used to tag the message before publishing it to another user.
     * @return
     */
    public static JSONObject generateHangupPacket(String userId) {
        JSONObject json = new JSONObject();
        try {
            JSONObject packet = new JSONObject();
            packet.put(PnRTCMessage.JSON_TYPE, PnRTCMessage.JSON_HANGUP);
            json.put(PnRTCMessage.JSON_PACKET, packet);
            json.put(PnRTCMessage.JSON_ID, ""); //Todo: session id, unused in js SDK?
            json.put(PnRTCMessage.JSON_SENDER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * @param userId Your id. Used to tag the message before publishing it to another user.
     * @return
     */
    public static JSONObject generateAddtimePacket(String userId) {
        JSONObject json = new JSONObject();
        try {
            JSONObject packet = new JSONObject();
            packet.put(PnRTCMessage.JSON_TYPE, PnRTCMessage.JSON_ADDTIME);
            json.put(PnRTCMessage.JSON_PACKET, packet);
            json.put(PnRTCMessage.JSON_ID, ""); //Todo: session id, unused in js SDK?
            json.put(PnRTCMessage.JSON_SENDER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * @param senderId Your id. Used to tag the message before publishing it to another user.
     * @return
     */
    public static JSONObject generateDialPacket(String senderId, String senderNickName, String
            senderGender) {
        JSONObject json = new JSONObject();
        try {
            JSONObject packet = new JSONObject();
            packet.put(PnRTCMessage.JSON_TYPE, PnRTCMessage.JSON_DIAL);
            packet.put(JSON_SENDER_NICK_NAME, senderNickName);
            packet.put(PnRTCMessage.JSON_SENDER_GENDER, senderGender);
            json.put(PnRTCMessage.JSON_PACKET, packet);
            json.put(PnRTCMessage.JSON_ID, ""); //Todo: session id, unused in js SDK?
            json.put(PnRTCMessage.JSON_SENDER_ID, senderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Static method to generate the proper JSON for a user message. Use this when you don't have
     * a {@link com.shanjingtech.pnwebrtc.PnRTCClient} instantiated. Simply send a publish with the
     * returned JSONObject to the ID that a user is subscribed to.
     *
     * @param userId  Your UserID, needed to tag the message
     * @param message The message you with to send some other user
     * @return JSONObject properly formatted for the PubNub WebRTC API
     */
    public static JSONObject generateUserMessage(String userId, JSONObject message) {
        JSONObject json = new JSONObject();
        try {
            JSONObject packet = new JSONObject();
            packet.put(PnRTCMessage.JSON_USERMSG, message);
            json.put(PnRTCMessage.JSON_PACKET, packet);
            json.put(PnRTCMessage.JSON_ID, ""); //Todo: session id, unused in js SDK?
            json.put(PnRTCMessage.JSON_SENDER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    // listens on messages sent to own channel
    private class PnRTCReceiver extends SubscribeCallback {

        @Override
        public void status(PubNub pubnub, PNStatus status) {
            // these are all logging infos
            if (status.isError()) {
                for (PnRTCListener pnRTCListener : mRtcListeners) {
                    pnRTCListener.onDebug(new PnRTCMessage(new PnRTCMessage(status.toString())));
                }
            } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                for (PnRTCListener pnRTCListener : mRtcListeners) {
                    pnRTCListener.onDebug(new PnRTCMessage(status.toString()));
                    pnRTCListener.onChannelSubscribed(pubnub.getSubscribedChannels());
                }
            }
        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {
            Log.d("BGLM", "got some message!");
            JSONObject jsonMessage = JSONUtils.convertFrom(message.getMessage());
            for (PnRTCListener pnRTCListener : mRtcListeners) {
                pnRTCListener.onDebug(new PnRTCMessage(jsonMessage));
            }
            if (jsonMessage == null) {
                Log.d(JSONUtils.JSON_TAG, "empty json or json doesn't have call user");
            }
            try {
                String peerId = jsonMessage.getString(PnRTCMessage.JSON_SENDER_ID);
                JSONObject packet = jsonMessage.getJSONObject(PnRTCMessage.JSON_PACKET);
                String type = packet.getString(PnRTCMessage.JSON_TYPE);
                // Initial dial message, we don't need to register a peer at this time
                if (!PnRTCMessage.JSON_ICE.equals(type)) {
                    Log.d(META_MSG, "received from " + peerId + ", message type: " +
                            type);
                    Log.d(META_MSG, "" + jsonMessage);

                }
                if (PnRTCMessage.JSON_DIAL.equals(type)) {
                    actionMap.get(PnUserDialAction.TRIGGER).execute(peerId, packet);
                    return;
                }

                // User message has nothing to do with rtc
                if (PnRTCMessage.JSON_USERMSG.equals(type)) {
                    actionMap.get(PnUserMessageAction.TRIGGER).execute(peerId, packet);
                    return;
                }

                PnPeer peer;
                if (!peers.containsKey(peerId)) {
                    peer = addPeer(peerId);
                    peer.pc.addStream(localMediaStream);
                    // received a non existant hangup message, ignore it
//                    if (PnRTCMessage.JSON_HANGUP.equals(type)) {
//                        return;
//                    } else {
//                        // Possibly threshold number of allowed users
//                        peer = addPeer(peerId);
//                        peer.pc.addStream(localMediaStream);
//                    }
                } else {
                    peer = peers.get(peerId);
                }
                if (peer.getStatus().equals(PnPeer.STATUS_DISCONNECTED))
                    return; // Do nothing if disconnected.
                if (PnRTCMessage.JSON_HANGUP.equals(type)) {
                    actionMap.get(PnUserHangupAction.TRIGGER).execute(peerId, packet);
                    return;
                }
                if (PnRTCMessage.JSON_ADDTIME.equals(type)) {
                    actionMap.get(PnUserAddtimeAction.TRIGGER).execute(peerId, packet);
                    return;
                }
                if (packet.has(PnRTCMessage.JSON_SDP)) {
                    if (!peer.received) {
                        peer.setReceived(true);
                        for (PnRTCListener pnRTCListener : mRtcListeners) {
                            pnRTCListener.onDebug(new PnRTCMessage("SDP - " + peer.toString()));
                        }
                        // Todo: reveivercb(peer);
                    }
                    actionMap.get(type).execute(peerId, packet);
                    return;
                }
                if (PnRTCMessage.JSON_ICE.equals(type)) {
                    actionMap.get(AddIceCandidateAction.TRIGGER).execute(peerId, packet);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        }
    }

}