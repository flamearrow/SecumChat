package com.shanjingtech.secumchat.log;

import com.crashlytics.android.answers.CustomEvent;
import com.google.auto.factory.AutoFactory;

import static com.shanjingtech.secumchat.log.LogConstants.PEER1;
import static com.shanjingtech.secumchat.log.LogConstants.PEER2;

/**
 * Both sides clicked add time
 */
@AutoFactory
public class AddTimePaired extends CustomEvent {
    private static final String TAG = "AddTimePaired";

    public AddTimePaired(String peer1, String peer2) {
        super(TAG);
        putCustomAttribute(PEER1, peer1);
        putCustomAttribute(PEER2, peer2);
    }
}
