package com.shanjingtech.secumchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.shanjingtech.secumchat.model.EndMatch;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.SecumDebug;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
