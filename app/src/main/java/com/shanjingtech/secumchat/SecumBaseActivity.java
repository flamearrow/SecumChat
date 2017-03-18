package com.shanjingtech.secumchat;

import android.app.Activity;
import android.os.Bundle;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.shanjingtech.secumchat.log.AddTimePairedFactory;
import com.shanjingtech.secumchat.net.SecumAPI;

import javax.inject.Inject;

/**
 * Supers all Secum activities, handles dagger injection
 */

public class SecumBaseActivity extends Activity {
    @Inject
    protected SecumAPI secumAPI;
    @Inject
    protected Answers answers;
    @Inject
    AddTimePairedFactory addTimePairedFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SecumApplication) getApplication()).getNetComponet().inject(this);
    }
}
