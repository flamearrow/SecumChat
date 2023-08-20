package com.meichinijiuchiquba.pnwebrtc;

import org.webrtc.MediaStream;

import java.util.List;

/**
 * Created by flamearrow on 2/26/17.
 */

public abstract class PnRTCListener {
    /**
     * Called in {@link com.pubnub.api.PubNub} object's subscribe connected callback.
     * Means that you are ready to receive calls.
     *
     * @param channels The channels you are subscribed to, the userId you may be called on.
     */
    public void onChannelSubscribed(List<String> channels) {
    }

    /**
     * Peer status changed. {@link PnPeer} status changed, can be
     * CONNECTING, CONNECTED, or DISCONNECTED.
     *
     * @param peer The peer object, can use to check peer.getStatus()
     */
    public void onPeerStatusChanged(PnPeer peer) {
    }

    /**
     * TODO: Is this different than onPeerStatusChanged == DISCONNECTED?
     * Called when a hangup occurs.
     *
     * @param peer The peer who was hung up on, or who hung up on you
     */
    public void onPeerConnectionClosed(PnPeer peer) {
    }

    /**
     * Called in {@link PnPeerConnectionClient} when setLocalStream
     * is invoked.
     *
     * @param localStream The users local stream from Android's front or back camera.
     */
    public void onLocalStream(MediaStream localStream) {
    }

    /**
     * Called when a remote stream is added in the {@link org.webrtc.PeerConnection.Observer}
     * in {@link PnPeer}.
     *
     * @param remoteStream The remote stream that was added
     * @param peer         The peer that added the remote stream
     *                     Todo: Maybe not the right peer?
     */
    public void onAddRemoteStream(MediaStream remoteStream, PnPeer peer) {
    }

    /**
     * Called in the {@link org.webrtc.PeerConnection.Observer} implemented
     * by {@link PnPeer}.
     *
     * @param remoteStream The stream that was removed by your peer
     * @param peer         The peer that removed the stream.
     */
    public void onRemoveRemoteStream(MediaStream remoteStream, PnPeer peer) {
    }

    /**
     * Called when a user message is send via {@link com.pubnub.api.PubNub} object.
     *
     */
    public void onMessage(String message, String from, String groupId, long time) {
    }

    /**
     * Called when a addtime message is sent via {@link com.pubnub.api.PubNub} object.
     *
     * @param peer The peer who sent the message
     */
    public void onAddTime(PnPeer peer) {
    }


    /**
     * Called when a dial message is sent, tell me who dialed
     *
     * @param senderId       Id of the caller/dialer
     * @param senderNickName NickName of caller/dialer
     * @param senderGender   Gender the caller/dialer
     */
    public void onDialed(String senderId, String senderNickName, String senderGender) {

    }

    /**
     * A helpful debugging callback for testing and developing your app.
     *
     * @param message The {@link PnRTCMessage} debug message.
     */
    public void onDebug(PnRTCMessage message) {
    }
}