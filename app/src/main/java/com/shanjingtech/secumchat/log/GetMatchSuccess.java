package com.shanjingtech.secumchat.log;

import com.crashlytics.android.answers.CustomEvent;
import com.google.auto.factory.AutoFactory;

import static com.shanjingtech.secumchat.log.LogConstants.CALLEE;
import static com.shanjingtech.secumchat.log.LogConstants.CALLER;

/**
 * GetMatch finds a pair(pending user accept to start chatting
 */
@AutoFactory
public class GetMatchSuccess extends CustomEvent {
    private static final String TAG = "GetMatchSuccess";

    public GetMatchSuccess(String caller, String callee) {
        super(TAG);
        putCustomAttribute(CALLER, caller);
        putCustomAttribute(CALLEE, callee);
    }
}
