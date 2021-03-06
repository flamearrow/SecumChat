package com.shanjingtech.secumchat.message;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.db.Message;
import com.shanjingtech.secumchat.model.SendMessageRequest;
import com.shanjingtech.secumchat.model.SendMessageResponse;
import com.shanjingtech.secumchat.viewModels.ChatHistoryViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to display a 1 on 1 chat thread
 */

public class SecumMessageActivity extends SecumBaseActivity implements SwipeRefreshLayout
        .OnRefreshListener, MessageActionBoxView.MessageSendListener {
    private static final String TAG = "SecumMessageActivity";

    public static final String PEER_USER_NAME = "PEER_USER_NAME";
    // Unique id to identify this chat group
    public static final String GROUP_ID = "GROUP_ID";

    private String peerUserName;
    private String ownerName;
    private String groupId;
    private RecyclerView messageRecyclerView;
    private MessageActionBoxView messageAction;
    private ChatHistoryViewModel chatHistoryViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SecumMessageAdapter secumMessageAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        currentPeerNameProvider.setPeerUserName(peerUserName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPeerNameProvider.setPeerUserName(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: maybe create dagger scope to provide peer user
        peerUserName = getIntent().getStringExtra(PEER_USER_NAME);
        ownerName = currentUserProvider.getUser().getUsername();

        setContentView(R.layout.secum_message_activity);
        messageRecyclerView = findViewById(R.id.message_recycler);
        messageAction = findViewById(R.id.message_box);
        messageAction.setMessageSentListener(this);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        // groupId is null if it's started from a fresh chat
        groupId = getIntent().getStringExtra(GROUP_ID);

        initializeRecyclerView();

        markUnreadMessages();
        overridePendingTransition(R.anim.enter_from_right_full, R.anim.do_nothing);
    }

    private void initializeRecyclerView() {
        chatHistoryViewModel = ViewModelProviders.of(this).get(ChatHistoryViewModel.class);

        secumMessageAdapter = new SecumMessageAdapter(ownerName);
        if (groupId != null) {
            observeViewModel();
        }
        messageRecyclerView.setAdapter(secumMessageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void observeViewModel() {
        chatHistoryViewModel.getLiveHistoryWithGroupId(groupId).observe(this, items -> {
                    secumMessageAdapter.replaceItems(items);
                    messageRecyclerView.smoothScrollToPosition(secumMessageAdapter.getItemCount()
                            - 1);
                }
        );
    }

    private void markUnreadMessages() {
        new Thread() {
            @Override
            public void run() {
                // update messages with group id, mark the read field
                List<Message> messages = messageDAO.unreadHistoryWithGroupId(groupId);
                for (Message message : messages) {
                    message.setRead(true);
                }
                messageDAO.updateMessages(messages);
            }
        }.start();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "onRefresh", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMessageSent(String text) {
        messageAction.clearText();


        secumAPI.sendMessage(new SendMessageRequest(peerUserName, text)).enqueue(
                new Callback<SendMessageResponse>() {

                    @Override
                    public void onResponse(Call<SendMessageResponse> call,
                                           Response<SendMessageResponse> response) {
                        if (groupId == null) {
                            groupId = response.body().getGroupId();
                            // TODO: reattach ViewModel
                            observeViewModel();
                        }
                        Message message = new Message.Builder().setFrom(ownerName).setTo
                                (peerUserName)
                                .setOwnerName
                                        (ownerName).setTime(System.currentTimeMillis())
                                .setGroupId(groupId)
                                .setContent
                                        (text).build();

                        new Thread() {
                            @Override
                            public void run() {
                                messageDAO.insertMessage(message);
                            }
                        }.start();
                        Log.d(TAG, "sent success");
                    }

                    @Override
                    public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                        // show some warning
                        Log.d(TAG, "sent failure");
                    }
                });

    }
}
