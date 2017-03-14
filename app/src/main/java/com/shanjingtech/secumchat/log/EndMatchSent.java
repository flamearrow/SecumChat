package com.shanjingtech.secumchat.log;

import com.crashlytics.android.answers.CustomEvent;
import com.google.auto.factory.AutoFactory;

import static com.shanjingtech.secumchat.log.LogConstants.USERID;

/**
 * An EndMatch request is sent
 */
@AutoFactory
public class EndMatchSent extends CustomEvent {
    private static final String TAG = "EndMatchSent";

    public EndMatchSent(String userId) {
        super(TAG);
        putCustomAttribute(USERID, userId);
    }
}
