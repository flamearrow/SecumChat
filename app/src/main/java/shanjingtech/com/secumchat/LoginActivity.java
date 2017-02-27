package shanjingtech.com.secumchat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import shanjingtech.com.secumchat.util.Constants;

/**
 * Created by flamearrow on 2/26/17.
 */

public class LoginActivity extends Activity {

    private EditText mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.login_username);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String lastUsername = extras.getString("oldUsername", "");
            mUsername.setText(lastUsername);
        }
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
     *   Then, redirects to the MainActivity.
     * @param view Button clicked to trigger call to joinChat
     */
//    public void joinChat(View view){
//        String username = mUsername.getText().toString();
//        if (!validUsername(username))
//            return;
//
//        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putString(Constants.USER_NAME, username);
//        edit.apply();
//
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }

    /**
     * Start secumChat activity
     * @param view
     */
    public void secumChat(View view) {
        mUsername.setText("mlgb");
        String username = mUsername.getText().toString();
        if (!validUsername(username))
            return;
        Intent intent = new Intent(this, SecumChatActivity.class);
        intent.putExtra(Constants.USER_NAME, username);
        startActivity(intent);
    }

    /**
     * Optional function to specify what a username in your chat app can look like.
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
