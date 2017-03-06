package com.shanjingtech.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.SecumChatActivity;

/**
 * Prompt for requests for access if required
 */

public class PermissionRequestActivity extends SecumBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_request_activity);
    }

    public void clickGo(View view) {
        Intent intent = new Intent(this, SecumChatActivity.class);
        startActivity(intent);
    }
}
