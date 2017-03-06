package com.shanjingtech.secumchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.shanjingtech.secumchat.model.GetMatch;
import com.shanjingtech.secumchat.model.GetMatchRequest;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserRequest;
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
        String testName = "phone+16314560722";
        secumAPI.getMatch(new GetMatchRequest(testName)).enqueue(
                new Callback<GetMatch>() {
                    @Override
                    public void onResponse(Call<GetMatch> call, Response<GetMatch> response) {
                        GetMatch matchResult = response.body();
                        String s = matchResult.getMatchedUsername();
                        String status = matchResult.getStatus();
                    }

                    @Override
                    public void onFailure(Call<GetMatch> call, Throwable t) {

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
