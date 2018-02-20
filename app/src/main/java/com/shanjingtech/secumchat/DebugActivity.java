package com.shanjingtech.secumchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shanjingtech.secumchat.db.ConversationPreview;
import com.shanjingtech.secumchat.db.Message;
import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.db.TimestampConverter;
import com.shanjingtech.secumchat.db.UnreadPreview;
import com.shanjingtech.secumchat.message.SecumMessageActivity;
import com.shanjingtech.secumchat.net.SecumDebugAPI;
import com.shanjingtech.secumchat.pushy.PushyInitializer;
import com.shanjingtech.secumchat.ui.DialingReceivingWaitingLayout;
import com.shanjingtech.secumchat.ui.HeartMagicLayout;
import com.shanjingtech.secumchat.ui.HeartSecumCounter;
import com.shanjingtech.secumchat.ui.SecumCounter;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class DebugActivity extends AppCompatActivity implements SecumCounter
        .SecumCounterListener, PushyInitializer.PushyInitializedCallback {
//    @Inject
//    SharedPreferences sharedPreferences;
//
//    @Inject
//    SecumAPI secumAPI;
//
//    @Inject
//    CurrentUserProvider currentUserProvider;
//
//    @Inject
//    PushyInitializer pushyInitializer;

//    @Inject
//    SecumDatabase secumDatabase;

//    @Inject
//    DBComponent.Builder dbComponentBuilder;

    @Inject
    MessageDAO messageDAO;

    private final static String TAG = "DebugActivity";
    HeartSecumCounter heartSecumCounter;
    HeartMagicLayout heart;
    DialingReceivingWaitingLayout dialingReceivingWaitingLayout;

    SecumDebugAPI secumDebugAPI = new SecumDebugAPI();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SecumApplication) getApplication()).getNetComponet().inject(this);
        setContentView(R.layout.debug_activity);

        // always use phone11's token
        logInAsUser11();


        dialingReceivingWaitingLayout = (DialingReceivingWaitingLayout) findViewById(R.id.drw);
        heartSecumCounter = (HeartSecumCounter) findViewById(R.id.chronometer);
//        catHead = (PulseImageView) findViewById(R.id.cat_head);
        heart = (HeartMagicLayout) findViewById(R.id.heart);

        // TODO: To be used in splash
        initializePushy();

    }

    private void logInAsUser22() {
//        SecumDebug.enableDebugMode(sharedPreferences);
//        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_22);
//        secumAPI.getProfile().enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    User user = response.body();
//                    currentUserProvider.setUser(user);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
    }

    private void logInAsUser11() {
//        SecumDebug.enableDebugMode(sharedPreferences);
//        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_11);
//        secumAPI.getProfile().enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    User user = response.body();
//                    currentUserProvider.setUser(user);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
    }

    private void initializePushy() {
//        pushyInitializer.setPushyInitializedCallback(this);
//        pushyInitializer.initializePushy();
    }

    public void clickHeart(View view) {
        heart.meLike();
    }

    public void b1(View view) {
//        secumAPI.pullMessage().enqueue(new Callback<GroupMessages>() {
//            @Override
//            public void onResponse(Call<GroupMessages> call, Response<GroupMessages> response) {
//                for (UnreadMessage unreadMessage : response.body().getGroupMessages()) {
//                    Log.d(TAG, "received message '" + unreadMessage.getText() + "' from " +
//                            unreadMessage.getSender_username());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GroupMessages> call, Throwable t) {
//
//            }
//        });
//
//        secumDebugAPI.fakePullMessage(true);
//        secumDebugAPI.pullMessage().enqueue(new Callback<GroupMessages>() {
//            @Override
//            public void onResponse(Call<GroupMessages> call, Response<GroupMessages> response) {
//                // create fake messages
//                GroupMessages groupMessages = new GroupMessages();
//
//            }
//
//            @Override
//            public void onFailure(Call<GroupMessages> call, Throwable t) {
//
//            }
//        });

//        pushyInitializer.initializePushy();
//        heartSecumCounter.explode();
//        secumCounter.initialize();
//        heart.switchState(PairLikeImageView.LikeState.ME_LIKE);
//        secumCounter.initialize();
//        catHead.startPulse();
//        dialingReceivingWaitingLayout.switchUIState(SecumChatActivity.State.DIALING);
//        secumCounter.meAdd();
    }

    public void b2(View view) {
        Intent intent = new Intent(this, SecumMessageActivity.class);
        intent.putExtra(SecumMessageActivity.PEER_USER_NAME, "phone_22");
        startActivity(intent);

//        heart.switchState(PairLikeImageView.LikeState.PEER_LIKE);
//        heart.peerLike();
//        secumCounter.peerAdd();
//        secumCounter.shake();
//        catHead.stopPulse();
//        secumCounter.zoom();
//        secumCounter.meAdd();
//        dialingReceivingWaitingLayout.switchUIState(SecumChatActivity.State.WAITING);
//        secumCounter.peerAdd();
    }

    private List<Message> createTestMessages() {
        List<Message> messages = new LinkedList<>();
        messages.add(createMessage("12345", "phone_11", "phone11",
                "phone22", "phone11 " +
                        "talks to phone22", "2017-11-29T03:29:13.249169Z", true));
        messages.add(createMessage("12345", "phone_11", "phone22",
                "phone11", "phone22 " +
                        "talks to phone11", "2017-11-29T03:39:13.249169Z", true));
        messages.add(createMessage("12345", "phone_11", "phone22",
                "phone11", "phone22 " +
                        "talks to phone11 unread 1", "2017-11-29T04:19:13.249169Z", false));
        messages.add(createMessage("12345", "phone_11", "phone22",
                "phone11", "phone22 " +
                        "talks to phone11 unread 2", "2017-11-30T03:39:13.249169Z", false));


//        messages.add(new Message());

        return messages;
    }

    private List<Message> createOtherTestMessages() {
        List<Message> messages = new LinkedList<>();
        messages.add(createMessage("123456", "phone_11", "phone11",
                "phone33", "phone11 " +
                        "talks to phone22", "2017-11-29T03:29:13.249169Z", true));
        messages.add(createMessage("123456", "phone_11", "phone33",
                "phone11", "phone33 " +
                        "talks to phone11", "2017-11-29T03:39:13.249169Z", true));
        messages.add(createMessage("123456", "phone_11", "phone33",
                "phone11", "phone33 " +
                        "talks to phone11 unread 1", "2017-11-29T04:19:13.249169Z", false));
        messages.add(createMessage("123456", "phone_11", "phone33",
                "phone11", "phone33 " +
                        "talks to phone11 unread 2", "2017-11-30T03:39:13.249169Z", false));


//        messages.add(new Message());

        return messages;
    }

    private Message createMessage(String groupId, String ownerName, String from, String to,
                                  String content, String time, boolean read) {
        Message m = new Message();
        m.setGroupId(groupId);
        m.setOwnerName(ownerName);
        m.setFrom(from);
        m.setTo(to);
        m.setContent(content);
        m.setTime(TimestampConverter.fromString(time));
        m.setRead(read);
        return m;
    }


    public void b3(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                List<Long> ids = messageDAO.insertMessages(createOtherTestMessages());
//                List<Long> ids2 = messageDAO.insertMessages(createTestMessages());
//                List<Message> history = messageDAO.historyWithGroupId("12345");
                List<UnreadPreview> previews = messageDAO.unreadPreviewOwnedBy("phone_11");
                List<ConversationPreview> conversationPreviews = messageDAO.conversationPreviewOwnedBy
                        ("phone_11");
                int i = 23;
                int j = 23;
            }
        }).start();



//        heart.switchState(HeartMagicLayout.LikeState.NO_LIKE);
//        secumCounter.peerAdd();
//        secumCounter.freeze();
//        heart.meLike();
//        secumCounter.meAdd();
//        secumCounter.initialize();
    }

    public void b4(View view) {
        String s = "2017-11-29T03:39:13.249169Z";
        long l = TimestampConverter.fromString(s);
        int i = 23;
        String ss = TimestampConverter.fromInt(l);
        int j = 24;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Message> messages = messageDAO.loadAllMessagesOwnedBy("phone_11");
//                int i = 23;
//            }
//        }).start();
//        heart.switchState(HeartMagicLayout.LikeState.BOTH_LIKE);
//        secumCounter.bounce();
//        secumCounter.freeze();
//        secumCounter.explode();
    }

    @Override
    public void onCounterStart() {
        Log.d(TAG, "onCounterStart");

    }

    @Override
    public void onCounterStop() {
        Log.d(TAG, "onCounterStop");
    }

    @Override
    public void onCounterExpire() {
        Log.d(TAG, "onCounterExpire");

    }

    @Override
    public void onAddTimePaired(int secondsLeft) {
        Log.d(TAG, "onAddTimePaired " + secondsLeft);

    }

    @Override
    public void onMeAdd() {
        Log.d(TAG, "onMeAdd");

    }

    @Override
    public void onPeerAdd() {
        Log.d(TAG, "onPeerAdd");
    }

    public void acceptChat(View view) {
        Log.d(TAG, "acceptChat");
    }

    public void rejectChat(View view) {
        Log.d(TAG, "rejectChat");
    }

    @Override
    public void onPushyInitialized() {
        Toast.makeText(getApplicationContext(), "onPushyInitialized", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onPushyInitializeFailed() {
        Toast.makeText(getApplicationContext(), "onPushyInitializeFailed", Toast.LENGTH_SHORT)
                .show();
    }
}
