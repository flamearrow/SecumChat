package com.shanjingtech.secumchat.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.shanjingtech.pnwebrtc.PnRTCListener;
import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.util.SecumUtils;

/**
 * Activity to display a 1 on 1 chat thread
 */

public class SecumMessageActivity extends SecumBaseActivity implements SwipeRefreshLayout
        .OnRefreshListener, MessageActionBoxView.MessageSendListener {
    private RecyclerView messageRecyclerView;
    private MessageActionBoxView messageAction;
    private SecumMessageAdapter messageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializePubnub();

        setContentView(R.layout.secum_message_activity);
        messageRecyclerView = (RecyclerView) findViewById(R.id.message_recycler);
        messageAction = (MessageActionBoxView) findViewById(R.id.message_box);
        messageAction.setMessageSentListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        initializeRecyclerView();
    }

    private void initializePubnub() {
        pnRTCClient.attachRTCListener(new PnRTCListener() {
            @Override
            public void onMessage(final String message, final long time) {
                addNewMessage(new Message(message, SecumUtils.getCurrentTime
                        (time), false));
            }
        });
        pnRTCClient.getPubNub().unsubscribeAll();
        // subscribe to my regular channel
        pnRTCClient.subscribeToPubnubChannel(getMyName());
    }

    private void initializeRecyclerView() {
        messageAdapter = new SecumMessageAdapter();
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
        addNewMessage(new Message(text, SecumUtils.getCurrentTime(), true));
    }

    void addNewMessage(final Message message) {
        messageRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                messageAdapter.addMessage(message);
                messageRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }

}
