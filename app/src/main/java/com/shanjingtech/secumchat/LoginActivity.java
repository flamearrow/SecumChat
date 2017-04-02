package com.shanjingtech.secumchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.SecumDebug;

/**
 * Legacy login activity, direct access to SecumChat and debug api
 */

public class LoginActivity extends SecumBaseActivity {

    private EditText mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SecumDebug.enableDebugMode(sharedPreferences);
        mUsername = (EditText) findViewById(R.id.login_username);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void button1(View view) {
        if (requestSecumPermissions()) {
            // login as phone_11
            SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_11);
            User user = new User();
            user.setUsername("phone_11");
            // accesstoken: "vUXSNoPy3XLb3oh51zrhrYqQoDaVGd"
            Intent intent = new Intent(this, SecumChatActivity.class);
            intent.putExtra(Constants.CURRENT_USER, user);
            startActivity(intent);
        }

    }

    public void button2(View view) {
        if (requestSecumPermissions()) {
            // login as phone_22
            SecumDebug.setDebugUser(sharedPreferences, SecumDebug.USER_22);
            User user = new User();
            user.setUsername("phone_22");
            // accesstoken: "zhvG2zIf4xXFzzFfTJjnfOycXTjBZn"
            Intent intent = new Intent(this, SecumChatActivity.class);
            intent.putExtra(Constants.CURRENT_USER, user);
            startActivity(intent);
        }
    }

    /**
     * TODO udpate this
     *
     * @return
     */
    String getPhoneNumber() {
        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService
                (Context.TELEPHONY_SERVICE);
        String s = manager.getNetworkCountryIso();
        return manager.getLine1Number();
    }

    /**
     * Optional function to specify what a username in your chat app can look like.
     *
     * @param username The name entered by a user.
     * @return is username valid
     */
    private boolean validUsername(String username) {
        if (username.length() == 0) {
            mUsername.setError("Username cannot be empty.");
            return false;
        }
        if (username.length() > 16) {
            mUsername.setError("Username too long.");
            return false;
        }
        return true;
    }
}
