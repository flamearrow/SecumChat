package com.shanjingtech.secumchat;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.shanjingtech.pnwebrtc.PnPeerConnectionClient;
import com.shanjingtech.pnwebrtc.PnRTCClient;
import com.shanjingtech.pnwebrtc.PnSignalingParams;
import com.shanjingtech.pnwebrtc.utils.JSONUtils;
import com.shanjingtech.secumchat.lifecycle.NonRTCMessageController;
import com.shanjingtech.secumchat.lifecycle.SecumRTCListener;
import com.shanjingtech.secumchat.model.GetMatch;
import com.shanjingtech.secumchat.net.SecumNetworkRequester;
import com.shanjingtech.secumchat.servers.XirSysRequest;
import com.shanjingtech.secumchat.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Activity to make you cum.
 */

public class SecumChatActivity extends SecumBaseActivity implements
        SecumRTCListener.RTCPeerListener,
        NonRTCMessageController.NonRTCMessageControllerCallbacks,
        SecumNetworkRequester.SecumNetworkRequesterCallbacks {

    private GLSurfaceView videoView;

    // webRTC components
    private MediaStream localStream;
    SecumRTCListener secumRTCListener;

    // Pubhub components
    private PnRTCClient pnRTCClient;

    private String myName;
    private String incomingCallerName;

    // UI
    // The button should be invisible until the user is being called
    private Button acceptButton;
    private Button rejectButton;
    private TextView messageField;

    // debug field, to be removed
    private Button connectButton;
    private EditText calleeField;


    // pubnub
    private NonRTCMessageController nonRTCMessageController;

    // state
    private State currentState;

    // network
    private GetMatch getMatch;
    private SecumNetworkRequester networkRequester;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secum_chat_activity);
        this.myName = getIntent().getStringExtra(Constants.MY_NAME);
        setTitle(myName);

        initUI();
        initRTCComponents();

        nonRTCMessageController = new NonRTCMessageController(myName, pnRTCClient.getPubNub(),
                this);
        networkRequester = new SecumNetworkRequester(this, myName, this);

        switchState(State.MATCHING);
    }

    private void initUI() {
        acceptButton = (Button) findViewById(R.id.accept_button);
        rejectButton = (Button) findViewById(R.id.reject_button);
        messageField = (TextView) findViewById(R.id.message_field);

        // to be removed
        calleeField = (EditText) findViewById(R.id.callee_name);
        connectButton = (Button) findViewById(R.id.connect_button);
        calleeField.setVisibility(View.INVISIBLE);
        connectButton.setVisibility(View.INVISIBLE);
    }

    // turn on Accept button
    // should it's accepted, #acceptChat will be called
    private void dispatchIncomingCall(final String userId) {
        Log.d(Constants.MLGB, "incoming call from: " + userId);
        incomingCallerName = userId;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                acceptButton.setText("accept call from " + userId);
                acceptButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initRTCComponents() {
        // First, we initiate the PeerConnectionFactory with our application context and some
        // options.
        PeerConnectionFactory.initializeAndroidGlobals(
                this,  // Context
                true,  // Audio Enabled
                true,  // Video Enabled
                true,  // Hardware Acceleration Enabled
                null); // Render EGL Context
        PeerConnectionFactory pcFactory = new PeerConnectionFactory();

        // init pubnubClient, order matters
        List<PeerConnection.IceServer> servers = getXirSysIceServers();
        if (!servers.isEmpty()) {
            pnRTCClient = new PnRTCClient(Constants.PUB_KEY, Constants.SUB_KEY, Constants.SEC_KEY,
                    myName, new
                    PnSignalingParams(servers));
        } else {
            // TODO: revert to use default signal params
            pnRTCClient = new PnRTCClient(Constants.PUB_KEY, Constants.SUB_KEY, Constants.SEC_KEY,
                    myName);
        }


        int camNumber = VideoCapturerAndroid.getDeviceCount();
        String frontFacingCam = VideoCapturerAndroid.getNameOfFrontFacingDevice();
        String backFacingCam = VideoCapturerAndroid.getNameOfBackFacingDevice();

        // Creates a VideoCapturerAndroid instance for the device name
        VideoCapturer capturer = VideoCapturerAndroid.create(frontFacingCam);

        // First create a Video Source, then we can make a Video Track
        VideoSource localVideoSource = pcFactory.createVideoSource(capturer, this.pnRTCClient
                .videoConstraints());
        VideoTrack localVideoTrack = pcFactory.createVideoTrack(Constants.VIDEO_TRACK_ID,
                localVideoSource);

        // First we create an AudioSource then we can create our AudioTrack
        AudioSource audioSource = pcFactory.createAudioSource(this.pnRTCClient.audioConstraints());
        AudioTrack localAudioTrack = pcFactory.createAudioTrack(Constants.AUDIO_TRACK_ID,
                audioSource);

        // Init camera views: this view is in charge of both bigger(bg) and small cameras views by
        // adding renderer
        videoView = (GLSurfaceView) findViewById(R.id.gl_surface);
        VideoRendererGui.setView(
                videoView,
                null);

        localStream = pcFactory.createLocalMediaStream(Constants.LOCAL_MEDIA_STREAM_ID);

        // Now we can add our tracks.
        localStream.addTrack(localVideoTrack);
        localStream.addTrack(localAudioTrack);

        // trigger pubnub callback
        secumRTCListener = new SecumRTCListener(this);
        secumRTCListener.addRemoteStreamListener(this);
        pnRTCClient.attachRTCListener(secumRTCListener);
        pnRTCClient.attachLocalMediaStream(localStream);

        // listenOn myself, this is required for both caller and callee
        // TODO: mlgb do I need to do this when entering MATCHING?
//        pnRTCClient.listenOn(myName);
    }

    public List<PeerConnection.IceServer> getXirSysIceServers() {
        List<PeerConnection.IceServer> servers = new ArrayList<PeerConnection.IceServer>();
        try {
            servers = new XirSysRequest().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return servers;
    }

    // TODO: to remove
    public void connect(View view) {
        String calleeName = calleeField.getText().toString();
        if (calleeName.isEmpty() || calleeName.equals(this.myName)) {
            showToast("Enter a valid user ID to call.");
            return;
        }
        hideKeyboard();

        // TODO: mlgb: this should be triggered when we receive server getmatch response
        nonRTCMessageController.dial(calleeName);
    }

    /**
     * Send peer an hangup message, regardless of its result, switch back to matching state
     */
    void hangUp() {
        String peerName = getPeerName();
        JSONObject hangupMsg = PnPeerConnectionClient.generateHangupPacket(myName);
        pnRTCClient.getPubNub().publish().channel(peerName).message(hangupMsg).async(new PNCallback<PNPublishResult>() {


            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if (status.isError()) {
                    Log.d(Constants.MLGB, "hangUp failed!");
                } else {
                    Log.d(Constants.MLGB, "hangUp succeeded!");
                }
            }
        });

        switchState(State.MATCHING);
    }

    private String getPeerName() {
        return getMatch.getMatchedUsername();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeChannels();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop listening on standby channel
        nonRTCMessageController.unStandby();
        // stop all RTC connection
        pnRTCClient.closeAllConnections();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SecumChatActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeChannels() {
        // subscribe to standby channel and regular channel, hangup all possible RTC peers
        // note it's ok to subscribe to a pubnub channel multiple times
        nonRTCMessageController.standBy();
        pnRTCClient.closeAllConnections();
        pnRTCClient.listenOnForce(myName);
    }

    private void switchState(State state) {
        currentState = state;
        Log.d(Constants.MLGB, "switched to State: " + state.toString());
        switch (state) {
            case MATCHING: {
                showMatchingUI();
                initializeChannels();
                networkRequester.initializeMatch();
//                initializeMatch();
                return;
            }
            case DIALING: {
                showDialingUI();
                return;
            }
            case RECEIVING: {
                showReceivingUI();
                return;
            }
            case CHATTING: {
                showChattingUI();
                return;
            }
            case WAITING: {
                showWaitingUI();
                return;
            }
            case ERROR: {
                showErrorUI();
                switchState(State.MATCHING);
                return;
            }
        }

    }

    private void showMatchingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show buttons invisible, message field visible
                messageField.setVisibility(View.VISIBLE);
                messageField.setText("matching...");
                turnButtons(false);
                secumRTCListener.resetLocalStream();
            }
        });
    }

    private void showDialingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show matching you with callee name
                // accept/reject button
                messageField.setVisibility(View.VISIBLE);
                messageField.setText("matching you with " + getMatch.getCallee());
                turnButtons(true);
            }
        });
    }

    private void showReceivingUI() {
        // show matching with caller
//        messageField.setVisibility(View.VISIBLE);
//        messageField.setText("matching you with " + getMatch.caller);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageField.setVisibility(View.VISIBLE);
                messageField.setText("matching you with " + getMatch.getCaller());
                turnButtons(true);
            }
        });
    }

    private void showChattingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // hide message field,
                // TODO: mlgb need to addtime button
                messageField.setVisibility(View.INVISIBLE);
                turnButtons(false);
            }
        });
    }

    private void showWaitingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // hide buttons
                turnButtons(false);
                messageField.setVisibility(View.VISIBLE);
                messageField.setText("matching you with " + getMatch.getCallee());
            }
        });
    }

    private void showErrorUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: replace otherside with real name
                showToast("otherside rejected");
                messageField.setVisibility(View.INVISIBLE);
                turnButtons(false);
            }
        });
    }

    private void turnButtons(boolean on) {
        if (on) {
            acceptButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
        } else {
            acceptButton.setVisibility(View.INVISIBLE);
            rejectButton.setVisibility(View.INVISIBLE);
        }
    }

    public void toDial(View view) {
        // test, need to be switch from getMatch

//        fakeIncomingGetMatchResponse();
        switchState(State.DIALING);
    }

    public void toReceive(View view) {
        // test, need to be switch from getMatch
        // need to be called after peer switch to dial

//        fakeIncomingGetMatchResponse();
        switchState(State.RECEIVING);
    }

    // note this should come from network
    public void fakeIncomingGetMatchResponse() {
        getMatch = new GetMatch();
        getMatch.setCalleeName("mlgb2");
        getMatch.setCallerName("mlgb");
    }


    public void acceptChat(View view) {
        // I can't switch to CHATTING state yet as callee hasn't connect
        // me yet, switch to CHATTING when onAddRemoteStream() is called
        if (currentState == State.DIALING) {
            // I'm caller, I dial callee
            nonRTCMessageController.dial(getMatch.getCallee());
            // when callee accepted, onAddRemoteStream() will be called
            switchState(State.WAITING);
        } else if (currentState == State.RECEIVING) {
            // I'm callee, caller already dialed my standby channel,
            //  I connect to caller through RTC
            // This will trigger caller's onAddRemoteStream()
            pnRTCClient.connect(getMatch.getCaller());
        }
    }

    public void rejectChat(View view) {
        // TODO: mlgb notify server this pair needs to be recycled to matching pool?
        if (currentState == State.DIALING) {
            // dialer clicked reject
            // just don't dial and continue waiting
            switchState(State.MATCHING);
        } else if (currentState == State.RECEIVING) {
            hangUp();
        }
    }

    @Override
    public void onRTCPeerConnected() {
        // connection established, switch to chatting
        switchState(State.CHATTING);
    }

    @Override
    public void onRTCPeerDisconnected() {
        if (currentState == State.WAITING) {
            // callee rejected me by generating a hangup message and triggersPnPeer.hangup()
            showToast("" + getMatch.getCallee() + " rejected");
            switchState(State.MATCHING);
        } else if (currentState == State.RECEIVING) {
            // TODO: mlgb is it possible to reach here?
        } else if (currentState == State.CHATTING) {
            // timeout or hangup
            switchState(State.MATCHING);
        }
    }

    private final static String TAG = "SecumChatCallbacks";

    @Override
    public void onStandbySuccess(List<String> channels, PNStatus message) {
        Log.d(TAG, "channel standby: " + message.toString());
    }

    @Override
    public void onStandbyFail(List<String> channels, PNStatus error) {
        Log.d(TAG, "channel standby fail: " + error.toString());
    }

    @Override
    public void onStandbyCalled(String channel, PNMessageResult message) {
        Log.d(TAG, "standby channel called: " + message.toString());
        JSONObject jsonMsg = JSONUtils.convertFrom(message.getMessage());
        try {
            if (jsonMsg == null || !jsonMsg.has(Constants.JSON_CALL_USER)) {
                Log.d(JSONUtils.JSON_TAG, "empty json or json doesn't have call user");
                return;     //Ignore Signaling messages.
            }
            String user = jsonMsg.getString(Constants.JSON_CALL_USER);


            // standby Channel called, verify if it's from the correct caller, if not ignore
            // it's possible callee hasn't receive getMatch yet

            // TO REMOVE
//            fakeIncomingGetMatchResponse();
            if (user.equals(getMatch.getCaller())) {
                switchState(State.RECEIVING);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCalleeOnline(String calleeName) {
        Log.d(TAG, "callee " + calleeName + " is online");
    }

    @Override
    public void onCalleeOffline(String calleeName) {
        Log.d(TAG, "callee " + calleeName + " is offline");
        switchState(State.ERROR);
    }

    @Override
    public void onCallingStandbyFailed(String channel, PNStatus error) {
        Log.d(TAG, "Failed to call standby channel: " + channel);
    }

    @Override
    public void onCalleeStandbyCalled(String channel, PNStatus message) {
        // TODO: show a loading status icon
        Log.d(TAG, "callee's phone is ringing: " + message.toString());
    }

    @Override
    public void onBackPressed() {
        if (currentState == State.CHATTING) {
            hangUp();
        }
        // TODO: might need to delegate real backpress on other states too
        else if (currentState == State.MATCHING || currentState == State.WAITING) {
            super.onBackPressed();
        }
    }

    @Override
    public void onGetMatchSucceed(GetMatch getMatch, boolean isCaller) {
        this.getMatch = getMatch;
        if (isCaller) {
            switchState(State.DIALING);
        }
    }

    @Override
    public void onGetMatchFailed(@Nullable GetMatch getMatch) {
        this.getMatch = getMatch;
    }

    @Override
    public void onEndMatchSucceed() {

    }

    @Override
    public void onEndMatchFailed() {

    }

    enum State {
        MATCHING, // wait for server to give me a match
        DIALING, // I dial the other, call dial(callee)
        RECEIVING, // I'm about to receive the dial
        CHATTING, // chatting
        WAITING, // When one side accept, waiting for the other side to accept
        ERROR //various, always show 'otherside rejected' and switch back to waiting
    }

}
