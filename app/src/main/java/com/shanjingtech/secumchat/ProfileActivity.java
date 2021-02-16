package com.shanjingtech.secumchat;

import android.app.SearchManager;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shanjingtech.secumchat.db.GroupId;
import com.shanjingtech.secumchat.message.SecumMessageActivity;
import com.shanjingtech.secumchat.model.AddContactRequest;
import com.shanjingtech.secumchat.model.BlockContactRequest;
import com.shanjingtech.secumchat.model.DeleteContactRequest;
import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.model.UpdateUserRequest;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.net.FirebaseImageUploader;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.viewModels.ProfileViewModel;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shanjingtech.secumchat.util.Constants.PROFILE_USER_NAME;

/**
 * ProfileActivity displays a user(myself, contacts or search results) from DB, note before
 * starting this activity, the user should be already inserted into DB.
 */
public class ProfileActivity extends SecumBaseActivity implements IPickResult {
    private static final String TAG = ProfileActivity.class.getCanonicalName();

    private static final int IS_MYSELF = 0;
    private static final int IS_CONTACT = 1;
    private static final int IS_STRANGER = 2;

    private String profileUserName;
    private TextView name;
    private TextView age;
    private ImageView avatar;
    private ImageView gender;
    private Button chat;
    private Button video;
    private Button add;
    private int owner;

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
        avatar.setOnClickListener(v -> {
                    if (owner == IS_MYSELF) {
                        PickImageDialog.build(new PickSetup()).setOnPickResult(this).show(this);
                    }
                }
        );
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (owner == IS_CONTACT) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_profile, menu);
        } else if (owner == IS_MYSELF) {
            // TODO: menu for myself
        } else if (owner == IS_STRANGER) {
            // TODO: menu for stranger
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
            owner = IS_STRANGER;
            // TODO: when is resulted from stranger, there's no database
        }
        // otherwise, is from contacts/requested/blocked or myself
        else {
            profileUserName = intent.getStringExtra(PROFILE_USER_NAME);
            add.setVisibility(View.GONE);
            chat.setVisibility(View.VISIBLE);
            video.setVisibility(View.VISIBLE);

            // myself
            if (profileUserName.equals(getMyName())) {
                owner = IS_MYSELF;
            } else {
                owner = IS_CONTACT;
            }

            profileViewModel.getActiveContactsOwnedBy(getMyName(), profileUserName).observe
                    (this,
                            profilePreview -> {
                                if (profilePreview == null) {
                                    showNotFoundDialog();
                                } else {
                                    name.setText(profilePreview.getNickName());
                                    age.setText(profilePreview.getAge());
                                    if (profilePreview.getGender() != null) {
                                        gender.setImageResource(profilePreview.getGender().equals
                                                (Constants
                                                        .MALE) ? R
                                                .drawable.male : R.drawable.female);
                                        gender.setVisibility(View.VISIBLE);
                                    } else {
                                        gender.setVisibility(View.GONE);
                                    }
                                    Glide.with(ProfileActivity.this).load(profilePreview
                                            .getProfileImageUrl()).into(avatar);
                                    // also has email, phone, status etc
                                }
                            });
        }

    }

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


    /**
     * Update the current user's avatar.
     * First upload the imageView's drawable to firebase, then update the url to secum backend
     *
     * @param imageView
     */
    public void updateUserAvatar(ImageView imageView) {

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        firebaseImageUploader.uploadImage(imageView.getDrawingCache(), getMyName(),
                new FirebaseImageUploader.ImageUploadListener() {

                    @Override
                    public void onLoadStarted() {
                        imageView.setDrawingCacheEnabled(false);
                        Log.d(TAG, "image load started: " + getMyName());
                    }

                    @Override
                    public void onLoadSuccess(String imageUrl) {
                        Log.d(TAG, "image load succeed: " + getMyName() + ", url: " + imageUrl);
                        secumAPI.updateUser(new UpdateUserRequest.Builder().setProfileImageUrl
                                (imageUrl).build()).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    currentUserProvider.setUser(response.body());
                                    Toast.makeText(ProfileActivity.this, getResources().getString(R
                                            .string
                                            .avatar_update_success), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfileActivity.this, getResources().getString
                                            (R.string
                                                    .avatar_update_failure), Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this, getResources().getString(R
                                        .string
                                        .avatar_update_failure), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onLoadFailure() {
                        Log.e(TAG, "image load failed" + getMyName());
                    }
                });

    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
            avatar.setImageBitmap(r.getBitmap());
            updateUserAvatar(avatar);
        } else {
            Log.e(TAG, "Failed to get image");
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
