package com.shanjingtech.secumchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.shanjingtech.secumchat.model.AccessToken;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.PermissionRequester;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Legacy login activity, direct access to SecumChat and debug api
 */

public class LoginActivity extends SecumBaseActivity {

    private EditText mUsername;
    private PermissionRequester permissionRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.login_username);

        permissionRequester = new PermissionRequester(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        permissionRequester.requestPermissions();
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
//        secumAPI.registerUser(new UserRequest("phone+16503181659", "+16503181659"))
//                .enqueue(
//                        new Callback<User>() {
//                            @Override
//                            public void onResponse(Call<User> call, Response<User> response) {
//                                User u = response.body();
//                                String name = u.getUsername();
//                            }
//
//                            @Override
//                            public void onFailure(Call<User> call, Throwable t) {
//                                String s = call.getClass().toString();
//                            }
//                        });
        String formData = "grant_type=password&username=%2B16503181659&password=851927";
        secumAPI.getAccessToken("password", "+16503181659", "851927").enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                int i = 1;
                int j = 2;
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                int i = 1;
                int j = 2;
            }
        });
    }

    /**
     * Start secumChat activity
     *
     * @param view
     */
    public void secumChat(View view) {
        String username = mUsername.getText().toString();
        if (!validUsername(username))
            return;
        Intent intent = new Intent(this, SecumChatActivity.class);
        intent.putExtra(Constants.MY_NAME, username);
        startActivity(intent);
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
