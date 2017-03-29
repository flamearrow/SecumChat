package com.shanjingtech.secumchat;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.shanjingtech.pnwebrtc.PnRTCClient;
import com.shanjingtech.pnwebrtc.PnSignalingParams;
import com.shanjingtech.pnwebrtc.utils.JSONUtils;
import com.shanjingtech.secumchat.lifecycle.NonRTCMessageController;
import com.shanjingtech.secumchat.lifecycle.SecumRTCListener;
import com.shanjingtech.secumchat.model.GetMatch;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.net.SecumNetworkRequester;
import com.shanjingtech.secumchat.net.XirSysRequest;
import com.shanjingtech.secumchat.ui.DialingReceivingWaitingLayout;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.ui.SecumCounter;

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

import java.util.List;

/**
 * Activity to make you cum.
 */

public class SecumChatActivity extends SecumBaseActivity implements
        SecumRTCListener.RTCPeerListener,
        NonRTCMessageController.NonRTCMessageControllerCallbacks,
        SecumNetworkRequester.SecumNetworkRequesterCallbacks,
        SecumCounter.SecumCounterListener {

    private GLSurfaceView videoView;
    // my name, also used for regular channel name
    private String myName;
    private User currentUser;
    // my standby channel name
    private String myNameStdy;

    // webRTC components
    private MediaStream localStream;
    private VideoSource localVideoSource;
    private VideoTrack localVideoTrack;
    private AudioSource localAudioSource;
    private AudioTrack localAudioTrack;

    private SecumRTCListener secumRTCListener;

    // Pubhub components
    private PnRTCClient pnRTCClient;

    // UI
    // The button should be invisible until the user is being called
    private DialingReceivingWaitingLayout dialingReceivingWaitingView;
    private View matchingView;
    private Button addTimeButton;
    private SecumCounter secumCounter;

    // pubnub
    private NonRTCMessageController nonRTCMessageController;

    // state
    private State currentState;

    // network
    private GetMatch getMatch;
    private SecumNetworkRequester networkRequester;

    // timeout
    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * An {@code Runnable} to hangup and switch back to waiting state if the current state is
     * stateToError when fired.
     */
    class StateErrorRunnable implements Runnable {
        private State stateToError;

        StateErrorRunnable(State stateToError) {
            this.stateToError = stateToError;
        }

        @Override
        public void run() {
            if (currentState == stateToError) {
                showOtherSideRejected();
                hangUp();
            }
        }
    }

    /**
     * When get matched(caller switched to RECEIVING),
     * caller has 9 secs to click accept before it switch back to MATCHING
     */
    private StateErrorRunnable dialingErrorRunnable = new StateErrorRunnable(State.DIALING);
    /**
     * After caller clicked accept(caller switched to WAITING, caller has 9+2 seconds to wait for
     * caller's response before it switch back to MATCHING
     */
    private StateErrorRunnable waitingErrorRunnable = new StateErrorRunnable(State.WAITING);
    /**
     * When callee switched to RECEIVING, callee has 9 seconds to click accept before it switch
     * back to MATCHING
     */
    private StateErrorRunnable receivingErrorRunnable = new StateErrorRunnable(State.RECEIVING);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secum_chat_activity);
        this.currentUser = (User) getIntent().getSerializableExtra(Constants.CURRENT_USER);
        this.myName = currentUser.getUsername();
        myNameStdy = myName + Constants.STDBY_SUFFIX;
        setTitle(myName);

        initUI();
        initRTCComponents();
        initializeMediaStream();

        nonRTCMessageController = new NonRTCMessageController(myName, pnRTCClient.getPubNub(),
                this);
        networkRequester = new SecumNetworkRequester(this, myName, this);
    }

    private void initUI() {
        addTimeButton = (Button) findViewById(R.id.add_time_button);
        secumCounter = (SecumCounter) findViewById(R.id.secum_counter);
        secumCounter.setSecumCounterListener(this);
        dialingReceivingWaitingView = (DialingReceivingWaitingLayout) findViewById(R.id
                .dialing_receiving_waiting);
        matchingView = findViewById(R.id.matching_view);
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

        // init pubnubClient, order matters
        List<PeerConnection.IceServer> servers = XirSysRequest.getIceServers();
        if (!servers.isEmpty()) {
            pnRTCClient = new PnRTCClient(Constants.PUB_KEY, Constants.SUB_KEY, Constants.SEC_KEY,
                    myName, new
                    PnSignalingParams(servers));
        } else {
            // TODO: revert to use default signal params
            pnRTCClient = new PnRTCClient(Constants.PUB_KEY, Constants.SUB_KEY, Constants.SEC_KEY,
                    myName);
        }

        // Init camera views: this view is in charge of both bigger(bg) and small cameras views by
        // adding renderer
        videoView = (GLSurfaceView) findViewById(R.id.gl_surface);
        VideoRendererGui.setView(
                videoView,
                null);

        secumRTCListener = new SecumRTCListener(this);
        secumRTCListener.addRemoteStreamListener(this);
        pnRTCClient.attachRTCListener(secumRTCListener);

    }

    private void initializeMediaStream() {
        PeerConnectionFactory pcFactory = new PeerConnectionFactory();
        int camNumber = VideoCapturerAndroid.getDeviceCount();
        String frontFacingCam = VideoCapturerAndroid.getNameOfFrontFacingDevice();
        String backFacingCam = VideoCapturerAndroid.getNameOfBackFacingDevice();

        // Creates a VideoCapturerAndroid instance for the device name
        VideoCapturer capturer = VideoCapturerAndroid.create(frontFacingCam);

        // First create a Video Source, then we can make a Video Track
        localVideoSource = pcFactory.createVideoSource(capturer, this.pnRTCClient
                .videoConstraints());
        localVideoTrack = pcFactory.createVideoTrack(Constants.VIDEO_TRACK_ID,
                localVideoSource);

        // First we create an AudioSource then we can create our AudioTrack
        localAudioSource = pcFactory.createAudioSource(this.pnRTCClient
                .audioConstraints());
        localAudioTrack = pcFactory.createAudioTrack(Constants.AUDIO_TRACK_ID,
                localAudioSource);

        localStream = pcFactory.createLocalMediaStream(Constants.LOCAL_MEDIA_STREAM_ID);

        // Now we can add our tracks.
        localStream.addTrack(localVideoTrack);
        localStream.addTrack(localAudioTrack);

        pnRTCClient.attachLocalMediaStream(localStream);
    }

    /**
     * Send peer an hangup message, regardless of its result, switch back to matching state
     */
    private void hangUp() {
        String peerName = getPeerName();
        if (peerName != null) {
            nonRTCMessageController.hangUp(peerName);
        }
        switchState(State.MATCHING);
    }

    @Nullable
    private String getPeerName() {
        // getMatch could be nullified by peer hangup
        if (getMatch == null) {
            return null;
        } else {
            return getMatch.getMatchedUsername();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpChannels();
        localVideoSource.restart();
        switchState(State.WARMUP);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hangUp();
        tearDownChannels();
        // stop camera
        localVideoSource.stop();
    }

    private void setUpChannels() {
        // subscribe to standby channel and regular channel, hangup all possible RTC peers
        // note it's ok to subscribe to a pubnub channel multiple times

        // subscribe to my standy channel
        nonRTCMessageController.standBy();
        pnRTCClient.closeAllConnections();
        // subscribe to my regular channel
        pnRTCClient.listenOnForce(myName);
    }

    private void tearDownChannels() {
        // stop listening on standby channel
        nonRTCMessageController.unStandby();
        // stop all RTC connection
        pnRTCClient.closeAllConnections();
        // stop querying server
        networkRequester.cancellAll();
        networkRequester.endMatch();
    }

    private void switchState(State state) {
        if (currentState == state) {
            return;
        }
        currentState = state;
        Log.d(Constants.MLGB, "switched to State: " + state.toString());
        switch (state) {
            case WARMUP: {
                // wait for subscribe success signal to switch to matching state
                hideAllUI();
                return;
            }
            case MATCHING: {
                getMatch = null;
                pnRTCClient.closeAllConnections();
                showMatchingUI();
//                setUpChannels();
                networkRequester.startMatch();
                return;
            }
            case DIALING: {
                showDialingUI();
                // after get matched, caller has 9 seconds to click the accept button
                removeAllHandlerCallbacks();
                handler.postDelayed(dialingErrorRunnable, Constants.TORA * Constants.MILLIS_IN_SEC);
                return;
            }
            case RECEIVING: {
                removeAllHandlerCallbacks();
                handler.postDelayed(receivingErrorRunnable, Constants.TORA * Constants
                        .MILLIS_IN_SEC);
                showReceivingUI();
                return;
            }
            case CHATTING: {
                showChattingUI();
                return;
            }
            case WAITING: {
                showWaitingUI();
                // after caller accepts, wait 9 + 2 seconds before time out
                removeAllHandlerCallbacks();
                handler.postDelayed(waitingErrorRunnable, (Constants.TORA + Constants.GRACE) *
                        Constants.MILLIS_IN_SEC);
                return;
            }
            case ERROR: {
                showOtherSideRejected();
                switchState(State.MATCHING);
                return;
            }
        }

    }

    private void removeAllHandlerCallbacks() {
        handler.removeCallbacks(receivingErrorRunnable);
        handler.removeCallbacks(dialingErrorRunnable);
        handler.removeCallbacks(waitingErrorRunnable);
    }

    private void showMatchingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideAllUI();
                matchingView.setVisibility(View.VISIBLE);
                secumRTCListener.resetLocalStream();
            }
        });
    }

    private void showDialingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideAllUI();
                dialingReceivingWaitingView.setMessage("matching you with " + getMatch.getCallee());
                dialingReceivingWaitingView.setVisibility(View.VISIBLE);
                dialingReceivingWaitingView.switchUIState(State.DIALING);
            }
        });
    }

    private void showReceivingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideAllUI();
                dialingReceivingWaitingView.setMessage("matching you with " + getMatch.getCaller());
                dialingReceivingWaitingView.setVisibility(View.VISIBLE);
                dialingReceivingWaitingView.switchUIState(State.RECEIVING);
            }
        });
    }

    private void showChattingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideAllUI();
                secumCounter.setVisibility(View.VISIBLE);
                addTimeButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showWaitingUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideAllUI();
                dialingReceivingWaitingView.setMessage("matching you with " + getMatch.getCallee());
                dialingReceivingWaitingView.setVisibility(View.VISIBLE);
                dialingReceivingWaitingView.switchUIState(State.WAITING);
            }
        });
    }

    private void showOtherSideRejected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideAllUI();
                showToast("" + getPeerName() + " rejected");
            }
        });
    }

    private void hideAllUI() {
        dialingReceivingWaitingView.setVisibility(View.INVISIBLE);
        secumCounter.setVisibility(View.INVISIBLE);
        addTimeButton.setVisibility(View.INVISIBLE);
        matchingView.setVisibility(View.INVISIBLE);
    }

    public void toDial(View view) {
        // test, need to be switch from getMatch
        localVideoSource.stop();
    }

    public void toReceive(View view) {
        // test, need to be switch from getMatch
        localVideoSource.restart();
    }

    /**
     * When I clicked addTime
     *
     * @param view
     */
    public void addTime(View view) {
        secumCounter.meAdd();
        // send a addTime packet
        nonRTCMessageController.addTime(getPeerName());
    }

    /**
     * When peer clicked addTime
     */
    public void peerAddTime() {
        secumCounter.peerAdd();
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
            if (getMatch == null) {
                switchState(State.ERROR);
            } else {
                pnRTCClient.connect(getMatch.getCaller());
            }
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

    @UiThread
    @Override
    public void onRemoteStreamAdded() {
        if (currentState == State.CHATTING) {
            secumCounter.initialize();
        }
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
    public void onSubscribeSuccess(List<String> channels, PNStatus message) {
        Log.d(TAG, "channel standby: " + message.toString());
        if (channels.contains(myName) && channels.contains(myNameStdy)) {
            switchState(State.MATCHING);
        }
    }

    @Override
    public void onSubscribeFail(List<String> channels, PNStatus error) {
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
            String callerName = jsonMsg.getString(Constants.JSON_CALL_USER);


            // standby Channel called, verify if it's from the correct caller, if not ignore
            // it's possible callee hasn't receive getMatch yet
            if (getMatch != null && callerName.equals(getMatch.getCaller())) {
                switchState(State.RECEIVING);
            } else {
                // If callee hasn't receive getMatch yet, send a reject message to callerName
                Log.d(TAG, "standby channel called but getMatch null");
                nonRTCMessageController.hangUp(callerName);
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
    public void onGetMatchSucceed(final GetMatch getMatch, boolean isCaller) {
        this.getMatch = getMatch;
        if (isCaller) {
            switchState(State.DIALING);
        }
    }

    @Override
    public void onGetMatchFailed(final @Nullable GetMatch getMatch) {
        this.getMatch = getMatch;
    }

    @Override
    public void onEndMatchSucceed() {
    }

    @Override
    public void onEndMatchFailed() {

    }

    private static final String SECUMCOUNTER = "SecumCounter";

    @Override
    public void onCounterStart() {
        Log.d(SECUMCOUNTER, "onCounterStart");
    }

    @Override
    public void onCounterExpire() {
        hangUp();
        Log.d(SECUMCOUNTER, "onCounterExpire");
    }

    @Override
    public void onMeAdd() {
        Log.d(SECUMCOUNTER, "onMeAdd");
    }

    @Override
    public void onPeerAdd() {
        Log.d(SECUMCOUNTER, "onPeerAdd");
        showToast(getPeerName() + " wants to add time!");
    }

    @Override
    public void onAddTimePaired(int secondsLeft) {
        Log.d(SECUMCOUNTER, "onAddTimePaired: " + secondsLeft);
        answers.logCustom(addTimePairedFactory.create(getMatch.getCaller(), getMatch.getCallee()));
    }

    public enum State {
        WARMUP, // sent subscribe message to my channel and my standby channel, wait for reply
        MATCHING, // wait for server to give me a match
        DIALING, // I dial the other, call dial(callee)
        RECEIVING, // I'm about to receive the dial
        CHATTING, // chatting
        WAITING, // When one side accept, waiting for the other side to accept, time out after 9s
        ERROR //various, always show 'otherside rejected' and switch back to waiting
    }

}
