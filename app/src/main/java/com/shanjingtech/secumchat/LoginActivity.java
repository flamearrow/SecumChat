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

import com.shanjingtech.secumchat.model.AccessToken;
import com.shanjingtech.secumchat.model.PingRequest;
import com.shanjingtech.secumchat.model.PingResponse;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.util.Constants;

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

    /**
     * Takes the username from the EditText, check its validity and saves it if valid.
     * Then, redirects to the MainActivity.
     *
     * @param view Button clicked to trigger call to joinChat
     */
    public void testButton(View view) {
        // use basic credential
//        secumAPI.getAccessToken("password", "+16503181659", "8048").enqueue(
//                new Callback<AccessToken>() {
//                    @Override
//                    public void
//                    onResponse(Call<AccessToken> call, Response<AccessToken> response) {
//                        int i = 1;
//                        int j = 2;
//                    }
//
//                    @Override
//                    public void
//                    onFailure(Call<AccessToken> call, Throwable t) {
//                        int i = 1;
//                        int j = 2;
//                    }
//                });
        secumAPI.getProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int i = 1;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                int j = 2;
            }
        });

        // use oauth token
//        secumAPI.ping(new PingRequest()).enqueue(new Callback<PingResponse>() {
//            @Override
//            public void onResponse(Call<PingResponse> call, Response<PingResponse> response) {
//                if (response.isSuccessful()) {
//                    Log.d("MLGB", "success");
//                } else {
//                    Log.d("MLGB", "fail");
//                }
//                int i = 1;
//            }
//
//            @Override
//            public void onFailure(Call<PingResponse> call, Throwable t) {
//                Log.d("MLGB", "fail");
//                int j = 1;
//            }
//        });
//        Log.d("MLGB", getPhoneNumber());

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
     * Start secumChat activity
     *
     * @param view
     */
    public void secumChat(View view) {
        if (requestSecumPermissions()) {
            String username = mUsername.getText().toString();
            if (!validUsername(username))
                return;
            Intent intent = new Intent(this, SecumChatActivity.class);
            intent.putExtra(Constants.MY_NAME, username);
            startActivity(intent);
        }
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
