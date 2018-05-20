package com.shanjingtech.secumchat;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanjingtech.secumchat.db.GroupId;
import com.shanjingtech.secumchat.message.SecumMessageActivity;
import com.shanjingtech.secumchat.model.AddContactRequest;
import com.shanjingtech.secumchat.model.BlockContactRequest;
import com.shanjingtech.secumchat.model.DeleteContactRequest;
import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.viewModels.ProfileViewModel;

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
    private boolean isStranger;

    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        avatar = findViewById(R.id.avatar);
        chat = findViewById(R.id.chat_button);
        video = findViewById(R.id.video_button);
        add = findViewById(R.id.add_button);
        overridePendingTransition(R.anim.enter_from_right_full, R.anim.do_nothing);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isStranger) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_profile, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_block:
                secumAPI.blockContact(new BlockContactRequest(profileUserName)).enqueue(
                        new Callback<List<GenericResponse>>() {
                            @Override
                            public void onResponse(Call<List<GenericResponse>> call,
                                                   Response<List<GenericResponse>> response) {
                                Toast.makeText(ProfileActivity.this, ProfileActivity.this
                                        .getResources()
                                        .getString(R.string.block_success), Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<List<GenericResponse>> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this, ProfileActivity.this
                                        .getResources()
                                        .getString(R.string.request_fail), Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            }
                        });
                break;
            case R.id.action_delete:
                secumAPI.deleteContact(new DeleteContactRequest(profileUserName)).enqueue(
                        new Callback<List<GenericResponse>>() {
                            @Override
                            public void onResponse(Call<List<GenericResponse>> call,
                                                   Response<List<GenericResponse>> response) {
                                Toast.makeText(ProfileActivity.this, ProfileActivity.this
                                        .getResources()
                                        .getString(R.string.delete_success), Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<List<GenericResponse>> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this, ProfileActivity.this
                                        .getResources()
                                        .getString(R.string.request_fail), Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            }
                        }
                );
                break;
        }
        return true;
    }

    private void handleIntent(Intent intent) {
        // started by search, is stranger
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            profileUserName = Constants.ACCOUNT_PREFIX + intent.getStringExtra(SearchManager.QUERY);
            add.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            isStranger = true;
        }
        // otherwise, from contacts/requested/blocked
        else {
            profileUserName = intent.getStringExtra(PROFILE_USER_NAME);
            add.setVisibility(View.GONE);
            chat.setVisibility(View.VISIBLE);
            video.setVisibility(View.VISIBLE);
            isStranger = false;
        }
        profileViewModel.getActiveContactsOwnedBy(getMyName(), profileUserName).observe(this,
                profilePreview -> {
                    if (profilePreview == null) {
                        showNotFoundDialog();
                    } else {
                        name.setText(profilePreview.getNickName());
                        age.setText(profilePreview.getAge());
                        if (profilePreview.getGender() != null) {
                            gender.setImageResource(profilePreview.getGender().equals(Constants
                                    .MALE) ? R
                                    .drawable.male : R.drawable.female);
                            gender.setVisibility(View.VISIBLE);
                        } else {
                            gender.setVisibility(View.GONE);
                        }
                        // also has email, phone, status etc
                    }
                });
        secumNetDBSynchronizer.syncUserDBFromUserName(getMyName(), profileUserName);
    }

//    private void pullUser() {
//
//        secumAPI.getProfileFromUserName(new GetProfileFromUserNameRequest(profileUserName))
//                .enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        User user = response.body();
//                        if (user == null) {
//                            showNotFoundDialog();
//                        } else {
//                            name.setText(user.getNickname());
//                            age.setText(user.getAge());
//                            if (user.getGender() != null) {
//                                gender.setImageResource(user.getGender().equals(Constants.MALE)
// ? R
//                                        .drawable.male : R.drawable.female);
//                                gender.setVisibility(View.VISIBLE);
//                            } else {
//                                gender.setVisibility(View.GONE);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) {
//                        Log.d(TAG, "Failure to access user.");
//                    }
//                });
//    }

    public void showNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources resources = this.getResources();
        builder.setMessage(resources.getString(R.string.no_user_found))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> finish())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void chat(View view) {
        new Thread() {
            public void run() {
                Intent intent = new Intent(
                        ProfileActivity.this,
                        SecumMessageActivity.class);
                intent.putExtra(SecumMessageActivity.PEER_USER_NAME, profileUserName);
                GroupId groupId = messageDAO.findChatWithUserOwnedBy(getMyName(), profileUserName);
                if (groupId != null) {
                    intent.putExtra(SecumMessageActivity.GROUP_ID, groupId.getGroupId());
                }
                startActivity(intent);
            }
        }.start();
    }

    public void video(View view) {
        // start SecumChatActivity with dialing mode
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
