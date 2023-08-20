package com.meichinijiuchiquba.secumchat.log;

import com.crashlytics.android.answers.CustomEvent;
import com.google.auto.factory.AutoFactory;

import static com.meichinijiuchiquba.secumchat.log.LogConstants.USERID;

/**
 * A GetMatch request is sent
 */
@AutoFactory
public class GetMatchSent extends CustomEvent {
    private static final String TAG = "GetMatchSent";

    public GetMatchSent(String userId) {
        super(TAG);
        putCustomAttribute(USERID, userId);
    }

}
