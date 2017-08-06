package com.shanjingtech.secumchat;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanjingtech.secumchat.model.AddContactRequest;
import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.model.GetProfileFromUserNameRequest;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shanjingtech.secumchat.util.Constants.PROFILE_USER_NAME;

public class ProfileActivity extends SecumBaseActivity {
    private static final String TAG = "ProfileActivity";
    private String profileUserName;
    private TextView name;
    private TextView age;
    private ImageView avatar;
    private ImageView gender;
    private Button chat;
    private Button video;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        gender = (ImageView) findViewById(R.id.gender);
        avatar = (ImageView) findViewById(R.id.avatar);
        chat = (Button) findViewById(R.id.chat_button);
        video = (Button) findViewById(R.id.video_button);
        add = (Button) findViewById(R.id.add_button);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        // started by search
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            profileUserName = Constants.ACCOUNT_PREFIX + intent.getStringExtra(SearchManager.QUERY);
            add.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
        }
        // otherwise
        else {
            profileUserName = intent.getStringExtra(PROFILE_USER_NAME);
            add.setVisibility(View.GONE);
            chat.setVisibility(View.VISIBLE);
            video.setVisibility(View.VISIBLE);
        }
        pullUser();
    }

    private void pullUser() {
        secumAPI.getProfileFromUserName(new GetProfileFromUserNameRequest(profileUserName))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        if (user == null) {
                            showNotFoundDialog();
                        } else {
                            name.setText(user.getNickname());
                            age.setText(user.getAge());
                            if (user.getGender() != null) {
                                gender.setImageResource(user.getGender().equals(Constants.MALE) ? R
                                        .drawable.male : R.drawable.female);
                                gender.setVisibility(View.VISIBLE);
                            } else {
                                gender.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(TAG, "Failure to access user.");
                    }
                });
    }

    public void showNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources resources = this.getResources();
        builder.setMessage(resources.getString(R.string.no_user_found))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void chat(View view) {

    }

    public void video(View view) {

    }

    public void add(View view) {
        secumAPI.addContact(new AddContactRequest(profileUserName)).enqueue(new Callback<List<GenericResponse>>() {


            @Override
            public void onResponse(Call<List<GenericResponse>> call,
                                   Response<List<GenericResponse>> response) {
                Toast.makeText(ProfileActivity.this, ProfileActivity.this.getResources()
                        .getString(R.string.requested), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<List<GenericResponse>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, ProfileActivity.this.getResources()
                        .getString(R.string.request_fail), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
