package com.shanjingtech.secumchat.log;

import com.crashlytics.android.answers.CustomEvent;
import com.google.auto.factory.AutoFactory;

/**
 * Both sides clicked add time
 */
@AutoFactory
public class AddTimePaired extends CustomEvent {
    private static final String TAG = "AddTimePaired";

    public AddTimePaired() {
        super(TAG);
    }
}
