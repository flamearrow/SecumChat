package com.shanjingtech.secumchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shanjingtech.secumchat.db.Message;
import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.db.TimestampConverter;
import com.shanjingtech.secumchat.db.UserDAO;
import com.shanjingtech.secumchat.db.UserDB;
import com.shanjingtech.secumchat.injection.CurrentUserProvider;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.net.SecumNetDBSynchronizer;
import com.shanjingtech.secumchat.pushy.PushyInitializer;
import com.shanjingtech.secumchat.ui.DialingReceivingWaitingLayout;
import com.shanjingtech.secumchat.ui.HeartMagicLayout;
import com.shanjingtech.secumchat.ui.HeartSecumCounter;
import com.shanjingtech.secumchat.ui.SecumCounter;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.SecumDebug;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DebugActivity extends AppCompatActivity implements SecumCounter
        .SecumCounterListener, PushyInitializer.PushyInitializedCallback {
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    SecumAPI secumAPI;

    @Inject
    CurrentUserProvider currentUserProvider;

    @Inject
    PushyInitializer pushyInitializer;


    @Inject
    MessageDAO messageDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    SecumNetDBSynchronizer secumNetDBSynchronizer;

    private final static String TAG = "DebugActivity";
    HeartSecumCounter heartSecumCounter;
    HeartMagicLayout heart;
    DialingReceivingWaitingLayout dialingReceivingWaitingLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SecumApplication) getApplication()).getNetComponet().inject(this);
        setContentView(R.layout.debug_activity);

        // always use phone11's token
//        logInAsUser11();


        dialingReceivingWaitingLayout = (DialingReceivingWaitingLayout) findViewById(R.id.drw);
        heartSecumCounter = (HeartSecumCounter) findViewById(R.id.chronometer);
//        catHead = (PulseImageView) findViewById(R.id.cat_head);
        heart = (HeartMagicLayout) findViewById(R.id.heart);

        // TODO: To be used in splash
        initializePushy();

    }

    private void logInAsUser22() {
        SecumDebug.enableDebugMode(sharedPreferences);
        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_22);
        secumAPI.getProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    currentUserProvider.setUser(user);

                    Intent intent = new Intent(DebugActivity.this, ConversationPreviewActivity
                            .class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                int i = 23;
                int j = i + 23;
            }
        });
    }

    private void logInAsUser11() {
        SecumDebug.enableDebugMode(sharedPreferences);
        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_11);
        secumAPI.getProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    currentUserProvider.setUser(user);

                    Intent intent = new Intent(DebugActivity.this, ConversationPreviewActivity
                            .class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                int i = 23;
                int j = i + 23;

            }
        });
    }

    private void logInAsUser11Profile() {
        SecumDebug.enableDebugMode(sharedPreferences);
        SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_11);

        secumNetDBSynchronizer.syncUserDBFromCurrentToken(new SecumNetDBSynchronizer
                .SecumNetDBSynchronizerListener() {

            @Override
            public void onDBOperationSuccess() {
                Intent intent = new Intent(DebugActivity.this, ProfileActivity
                        .class);
                intent.putExtra(Constants.PROFILE_USER_NAME, currentUserProvider.getUser()
                        .getUsername());
                startActivity(intent);
            }

            @Override
            public void onDBOperationFailure() {
                startProfileAcitivtyWithFakeUser();
            }
        });
    }

    private void startProfileAcitivtyWithFakeUser() {

        User newUser = new User();
        newUser.setUsername("mlgb");
        currentUserProvider.setUser(newUser);
        Intent intent = new Intent(DebugActivity.this, ProfileActivity
                .class);
        intent.putExtra(Constants.PROFILE_USER_NAME, "mlgb");
        startActivity(intent);
    }

    private void initializePushy() {
        pushyInitializer.setPushyInitializedCallback(this);
        pushyInitializer.initializePushy();
    }

    public void clickHeart(View view) {
        heart.meLike();
    }

    public void b1(View view) {
        logInAsUser11();
    }

    private void insertFakeUsersForPhone11() {
        userDAO.insertUser(createFakeUser("phone_11", "phone_11", "phone11Nick", "23",
                "male", "ganggang@gmao.com", "123456", UserDB.ContactStatus
                        .CONTACT_STATUS_ACTIVE));
        userDAO.insertUser(createFakeUser("phone_11", "phone_22", "phone22Nick", "23",
                "male", "ganggang@gmao.com", "123456", UserDB.ContactStatus
                        .CONTACT_STATUS_ACTIVE));
        userDAO.insertUser(createFakeUser("phone_11", "phone_33", "phone3Nick", "23",
                "male", "ganggang@gmao.com", "123456", UserDB.ContactStatus
                        .CONTACT_STATUS_ACTIVE));
        userDAO.insertUser(createFakeUser("phone_11", "ganggang1", "ganggang1Nick", "23",
                "male", "ganggang@gmao.com", "123456", UserDB.ContactStatus
                        .CONTACT_STATUS_ACTIVE));
        userDAO.insertUser(createFakeUser("phone_11", "ganggang2", "ganggang2Nick", "23",
                "male", "ganggang@gmao.com", "123456", UserDB.ContactStatus
                        .CONTACT_STATUS_ACTIVE));
        userDAO.insertUser(createFakeUser("phone_11", "ganggang3", "ganggang3Nick", "23",
                "male", "ganggang@gmao.com", "123456", UserDB.ContactStatus
                        .CONTACT_STATUS_BLOCKED));
        userDAO.insertUser(createFakeUser("phone_11", "ganggang4", "ganggang4Nick", "23",
                "male", "ganggang@gmao.com", "123456", UserDB.ContactStatus
                        .CONTACT_STATUS_REQUESTED));

        userDAO.insertUser(createFakeUser("phone_11", "ganggang5", "ganggang5Nick", "23",
                "male", "ganggang@gmao.com", "123456", UserDB.ContactStatus
                        .CONTACT_STATUS_PENDING));

    }

    private UserDB createFakeUser(String ownerName, String userName, String nickName, String age,
                                  String gender, String email, String phone, int status) {
        return new UserDB.Builder().setAge(age).setUserName(userName).setNickName(nickName)
                .setOwnerName(ownerName).setGender(gender).setEmail(email).setPhone(phone)
                .setStatus(status).build();
    }


    public void b2(View view) {
//        new Thread(
//                () -> {
//                    List<UserPreview> activePreview = userDAO.getActiveContacts("phone_11");
//                    List<UserPreview> pendingPreview = userDAO.getPendingContacts("phone_11");
//                    List<UserPreview> blockedPreview = userDAO.getBlockedContacts("phone_11");
//                    List<UserPreview> requestedPreview = userDAO.getRequestedContacts("phone_11");
//                    int i = 23;
//                }
//
//        ).start();
        logInAsUser22();

    }

    private List<Message> createTestMessages() {
        List<Message> messages = new LinkedList<>();
        messages.add(createMessage("1", "phone_11", "phone_11",
                "phone_22", "phone_11 " +
                        "talks to phone_22", "2017-11-29T03:29:13.249169Z", true));
        messages.add(createMessage("1", "phone_11", "phone_22",
                "phone_11", "phone_22 " +
                        "talks to phone11", "2017-11-29T03:39:13.249169Z", true));
        messages.add(createMessage("1", "phone_11", "phone_22",
                "phone_11", "phone22 " +
                        "talks to phone11 unread 1", "2017-11-29T04:19:13.249169Z", false));
        messages.add(createMessage("1", "phone_11", "phone_22",
                "phone_11", "phone_22 " +
                        "talks to phone11 unread 2", "2017-11-30T03:39:13.249169Z", false));


//        messages.add(new Message());

        return messages;
    }

    private List<Message> createOtherTestMessages() {
        List<Message> messages = new LinkedList<>();
        messages.add(createMessage("123456", "phone_11", "phone_11",
                "phone_33", "phone11 " +
                        "talks to phone33", "2017-11-29T03:29:13.249169Z", true));
        messages.add(createMessage("123456", "phone_11", "phone_33",
                "phone_11", "phone33 " +
                        "talks to phone11", "2017-11-29T03:39:13.249169Z", true));
        messages.add(createMessage("123456", "phone_11", "phone_33",
                "phone_11", "phone33 " +
                        "talks to phone11 unread 1", "2017-11-29T04:19:13.249169Z", false));
        messages.add(createMessage("123456", "phone_11", "phone_33",
                "phone_11", "phone33 " +
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
        logInAsUser11Profile();
//        new Thread(() -> {
//            List<Long> ids = messageDAO.insertMessages(createOtherTestMessages());
//            List<Long> ids2 = messageDAO.insertMessages(createTestMessages());
//            List<UnreadPreview> previews = messageDAO.unreadPreviewOwnedBy("phone_11");
//            List<ConversationPreview> conversationPreviews = messageDAO
//                    .conversationPreviewOwnedBy
//                            ("phone_11");
//            int i = 23;
//            int j = 23;
//        }).start();


//        heart.switchState(HeartMagicLayout.LikeState.NO_LIKE);
//        secumCounter.peerAdd();
//        secumCounter.freeze();
//        heart.meLike();
//        secumCounter.meAdd();
//        secumCounter.initialize();

    }

    public void b4(View view) {
//        String s = "2017-11-29T03:39:13.249169Z";
//        long l = TimestampConverter.fromString(s);
//        int i = 23;
//        String ss = TimestampConverter.fromLong(l);
//        int j = 24;
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
