package com.shanjingtech.secumchat.lifecycle;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoTrack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.shanjingtech.pnwebrtc.PnPeer;
import com.shanjingtech.pnwebrtc.PnRTCListener;
import com.shanjingtech.secumchat.SecumChatActivity;
import com.shanjingtech.secumchat.util.Constants;

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
        remoteCallbacks = VideoRendererGui.create(0, 0, 100, 100, VideoRendererGui.ScalingType
                .SCALE_ASPECT_FILL, false);
        localCallbacks = VideoRendererGui.create(0, 0, 100, 100, VideoRendererGui.ScalingType
                .SCALE_ASPECT_FILL, true);
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
                localStream.videoTracks.get(0).addRenderer(new VideoRenderer(localCallbacks));
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
                VideoRendererGui.update(localCallbacks, 0, 0, 100, 100, VideoRendererGui
                        .ScalingType.SCALE_ASPECT_FILL, false);
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
                Toast.makeText(secumChatActivity, "Connected to " + peer.getId(), Toast
                        .LENGTH_SHORT).show();
                try {
                    if (remoteStream.audioTracks.size() == 0 || remoteStream.videoTracks.size
                            () == 0) {
                        Log.d(TAG, "no video or audio tracks found on remoteStream");
                        return;
                    }
                    VideoTrack videoTrack = remoteStream.videoTracks.getFirst();
                    videoTrack.removeRenderer(remoteVideoRenderer);
                    videoTrack.addRenderer(remoteVideoRenderer);

                    VideoRendererGui.update(remoteCallbacks, 0, 0, 100, 100, VideoRendererGui
                            .ScalingType.SCALE_ASPECT_FILL, false);
                    VideoRendererGui.update(localCallbacks, 5, 5, 25, 25, VideoRendererGui
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
    public void onMessage(PnPeer peer, Object message) {
        super.onMessage(peer, message);  // Will log values
        if (!(message instanceof JSONObject)) return; //Ignore if not JSONObject
        JSONObject jsonMsg = (JSONObject) message;
        try {
            String uuid = jsonMsg.getString(Constants.JSON_MSG_UUID);
            String msg = jsonMsg.getString(Constants.JSON_MSG);
            long time = jsonMsg.getLong(Constants.JSON_TIME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddTime(PnPeer peer) {
        super.onAddTime(peer);
        secumChatActivity.peerAddTime();
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
