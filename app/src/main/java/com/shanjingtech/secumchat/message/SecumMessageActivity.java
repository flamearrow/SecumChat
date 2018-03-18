package com.shanjingtech.secumchat.message;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.shanjingtech.pnwebrtc.PnRTCListener;
import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.model.SendMessageRequest;
import com.shanjingtech.secumchat.viewModels.ChatHistoryViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to display a 1 on 1 chat thread
 */

public class SecumMessageActivity extends SecumBaseActivity implements SwipeRefreshLayout
        .OnRefreshListener, MessageActionBoxView.MessageSendListener {
    public static final String PEER_USER_NAME = "PEER_USER_NAME";
    // Unique id to identify this chat group
    public static final String GROUP_ID = "GROUP_ID";

    private String peerUserName;
    private RecyclerView messageRecyclerView;
    private MessageActionBoxView messageAction;
    private ChatHistoryViewModel chatHistoryViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: maybe create dagger scope to privde peer user
        peerUserName = getIntent().getStringExtra(PEER_USER_NAME);

        setContentView(R.layout.secum_message_activity);
        messageRecyclerView = findViewById(R.id.message_recycler);
        messageAction = findViewById(R.id.message_box);
        messageAction.setMessageSentListener(this);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        pnRTCClient.attachRTCListener(new PnRTCListener() {
            @Override
            public void onMessage(final String message, final long time) {
//                addNewMessage(new Message(message, SecumUtils.getCurrentTime
//                        (time), false));
            }
        });
        initializeRecyclerView();
    }

    private void initializePubnub() {
        pnRTCClient.getPubNub().unsubscribeAll();
        // subscribe to my regular channel
        pnRTCClient.subscribeToPubnubChannel(getMyName());
    }

    /**
     * TODO: move onResume and onPause to {@link SecumBaseActivity} without fucking
     * up {@link com.shanjingtech.secumchat.SecumChatActivity}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        initializePubnub();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pnRTCClient.getPubNub().unsubscribeAll();
    }

    private void initializeRecyclerView() {
        String groupId = getIntent().getStringExtra(GROUP_ID);
        chatHistoryViewModel = ViewModelProviders.of(this).get(ChatHistoryViewModel.class);

        SecumMessageAdapter messageAdapter = new SecumMessageAdapter(currentUserProvider.getUser
                ().getUsername());
        chatHistoryViewModel.getLiveHistoryWithGroupId(groupId).observe(this, items ->
                messageAdapter.replaceItems(items));
        messageRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "onRefresh", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMessageSent(String text) {
        messageAction.clearText();
//        addNewMessage(new Message(text, SecumUtils.getCurrentTime(), true));
        secumAPI.sendMessage(new SendMessageRequest(peerUserName, text)).enqueue(
                new Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse>
                            response) {
                        // dismiss the spinner
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        // show some warning
                    }
                });

    }

//    void addNewMessage(final Message message) {
//        messageRecyclerView.post(() -> {
//            messageAdapter.addMessage(message);
//            messageRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//        });
//    }

}
