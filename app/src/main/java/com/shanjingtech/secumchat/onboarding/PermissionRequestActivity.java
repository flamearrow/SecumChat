package com.shanjingtech.secumchat.onboarding;

import android.app.Activity;
import android.os.Bundle;

import com.shanjingtech.secumchat.R;

/**
 * Prompt for requests for access if required
 */

public class PermissionRequestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_request_activity);
    }
}
