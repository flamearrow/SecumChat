package com.shanjingtech.secumchat.log;

import com.crashlytics.android.answers.CustomEvent;
import com.google.auto.factory.AutoFactory;

import static com.shanjingtech.secumchat.log.LogConstants.CHATTIME;
import static com.shanjingtech.secumchat.log.LogConstants.USERID;

/**
 * A chat is finished
 */
@AutoFactory
public class ChatFinish extends CustomEvent {
    private static final String TAG = "ChatFinish";

    public ChatFinish(String userId, int seconds) {
        super(TAG);
        putCustomAttribute(USERID, userId);
        putCustomAttribute(CHATTIME, seconds);
    }
}
