package com.shanjingtech.secumchat.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
        setContentView(R.layout.secum_message_activity);
        messageRecyclerView = (RecyclerView) findViewById(R.id.message_recycler);
        messageAction = (MessageActionBoxView) findViewById(R.id.message_box);
        messageAction.setMessageSentListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        messageAdapter = new SecumMessageAdapter();
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "onRefresh", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMessageSent(String text) {
        messageAction.clearText();
        messageAdapter.addMessage(new Message(text, SecumUtils.getCurrentTime(), true));
        // TODO: scroll to bottom
        swipeRefreshLayout.refreshDrawableState();
    }

}
