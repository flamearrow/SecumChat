package com.meichinijiuchiquba.secumchat.lifecycle;

import android.util.Log;

import com.meichinijiuchiquba.pnwebrtc.PnPeer;
import com.meichinijiuchiquba.pnwebrtc.PnRTCListener;
import com.meichinijiuchiquba.pnwebrtc.PnRTCMessage;
import com.meichinijiuchiquba.secumchat.SecumChatActivity;

import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoTrack;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by flamearrow on 2/26/17.
 */

public class SecumRTCListener extends PnRTCListener {
    public interface RTCPeerListener {
        void onRTCPeerConnected();

        void onRTCPeerDisconnected();

        void onRemoteStreamAdded();
    }

    private static final String TAG = "SecumRTCListener";
    private SecumChatActivity secumChatActivity;
    private VideoRenderer localVideoRenderer;
    private VideoRenderer remoteVideoRenderer;
    private VideoRenderer.Callbacks localCallbacks;
    private VideoRenderer.Callbacks remoteCallbacks;

    private Set<RTCPeerListener> rTCPeerListeners;

    public SecumRTCListener(SecumChatActivity secumChatActivity) {
        this.secumChatActivity = secumChatActivity;
        remoteCallbacks = VideoRendererGui.create(0, 0, 100, 100,
                RendererCommon.ScalingType.SCALE_ASPECT_FILL
                , false);
        localCallbacks = VideoRendererGui.create(0, 0, 100, 100,
                RendererCommon.ScalingType.SCALE_ASPECT_FILL, true);
        localVideoRenderer = new VideoRenderer(localCallbacks);
        remoteVideoRenderer = new VideoRenderer(remoteCallbacks);
        rTCPeerListeners = Collections.synchronizedSet(new HashSet<RTCPeerListener>());
    }

    public void addRemoteStreamListener(RTCPeerListener rTCPeerListener) {
        rTCPeerListeners.add(rTCPeerListener);
    }

    public void removeRemoteStreamListener(RTCPeerListener rTCPeerListener) {
        rTCPeerListeners.remove(rTCPeerListener);
    }

    @Override
    public void onLocalStream(final MediaStream localStream) {
        super.onLocalStream(localStream); // Will log values
        secumChatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (localStream.videoTracks.size() == 0) {
                    Log.d(TAG, "no video tracks found on localStream");
                    return;
                }
                localStream.videoTracks.get(0).addRenderer(localVideoRenderer);
            }
        });
    }

    /**
     * Reset local stream to full screen
     */
    public void resetLocalStream() {
        secumChatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                VideoRendererGui.update(localCallbacks, 0, 0, 100, 100, RendererCommon
                        .ScalingType.SCALE_ASPECT_FILL, true);
            }
        });
    }

    @Override
    public void onAddRemoteStream(final MediaStream remoteStream, final PnPeer peer) {
        super.onAddRemoteStream(remoteStream, peer); // Will log values
        for (RTCPeerListener listener : rTCPeerListeners) {
            listener.onRTCPeerConnected();
        }
        secumChatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (remoteStream.audioTracks.size() == 0 || remoteStream.videoTracks.size
                            () == 0) {
                        Log.d(TAG, "no video or audio tracks found on remoteStream");
                        return;
                    }
                    VideoTrack videoTrack = remoteStream.videoTracks.getFirst();
                    videoTrack.removeRenderer(remoteVideoRenderer);
                    videoTrack.addRenderer(remoteVideoRenderer);

                    VideoRendererGui.update(remoteCallbacks, 0, 0, 100, 100, RendererCommon
                            .ScalingType.SCALE_ASPECT_FILL, false);
                    VideoRendererGui.update(localCallbacks, 5, 5, 25, 25, RendererCommon
                            .ScalingType.SCALE_ASPECT_FIT, true);
                    for (RTCPeerListener listener : rTCPeerListeners) {
                        listener.onRemoteStreamAdded();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRemoveRemoteStream(MediaStream remoteStream, PnPeer peer) {
        super.onRemoveRemoteStream(remoteStream, peer);
        Log.d(TAG, "onRemoveRemoteStream");
    }

    @Override
    public void onAddTime(PnPeer peer) {
        super.onAddTime(peer);
        secumChatActivity.onPeerAddTime();
    }

    @Override
    public void onDialed(String senderId, String senderNickName, String senderGender) {
        secumChatActivity.onDialed(senderId, senderNickName, senderGender);
    }

    @Override
    public void onDebug(PnRTCMessage message) {
        Log.d(TAG, message.getMessage());
    }

    @Override
    public void onChannelSubscribed(List<String> channels) {
        secumChatActivity.onChannelSubscribed(channels);
    }

    @Override
    public void onPeerStatusChanged(PnPeer peer) {
        if (peer.getStatus().equals(PnPeer.STATUS_DISCONNECTED)) {
            Log.d(TAG, "disconnected!");
            for (RTCPeerListener listener : rTCPeerListeners) {
                listener.onRTCPeerDisconnected();
            }
            // network break or whatever, triggered from
            // PeerConnection.Observer#onIceConnectionChange to disconnected
        }
    }

    @Override
    public void onPeerConnectionClosed(PnPeer peer) {
        // this is followed by onPeerStatusChanged - DISCONNECTED
        // we already handle it there
        Log.d(TAG, "onPeerConnectionClosed");
    }
}
