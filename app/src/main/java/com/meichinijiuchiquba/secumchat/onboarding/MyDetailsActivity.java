package com.meichinijiuchiquba.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.meichinijiuchiquba.secumchat.R;
import com.meichinijiuchiquba.secumchat.SecumBaseActivity;
import com.meichinijiuchiquba.secumchat.SecumChatActivity;
import com.meichinijiuchiquba.secumchat.model.UpdateUserRequest;
import com.meichinijiuchiquba.secumchat.model.User;
import com.meichinijiuchiquba.secumchat.model.UserNew;
import com.meichinijiuchiquba.secumchat.util.Constants;
import com.wefika.horizontalpicker.HorizontalPicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Input you name, age and gender
 */

public class MyDetailsActivity extends SecumBaseActivity {
    private EditText name;
    private User currentUser;
    private HorizontalPicker agePicker;
    private boolean isMale;
    private String[] agesArray;
    private RadioButton maleButton;
    private RadioButton femaleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_details_activity);
        name = (EditText) findViewById(R.id.my_name);
        agePicker = (HorizontalPicker) findViewById(R.id.my_age);
        agesArray = getResources().getStringArray(R.array.ages);
        maleButton = (RadioButton) findViewById(R.id.male);
        femaleButton = (RadioButton) findViewById(R.id.female);
        isMale = false;
//        currentUser = (User) getIntent().getSerializableExtra(Constants.CURRENT_USER);
        currentUser = currentUserProvider.getUser();
        if (currentUser != null) {
            prefill();
        }
    }

    private void prefill() {
        name.setText(currentUser.getNickname());
        int age = currentUser.getAge() == null ? 0 : Integer.parseInt(currentUser.getAge());
        if (age >= 13) {
            agePicker.setSelectedItem(age - 13);
        }
        String gender = currentUser.getGender();
        if (gender != null) {
            if (gender.equals(Constants.MALE)) {
                isMale = true;
                maleButton.setChecked(true);
                femaleButton.setChecked(false);

            } else if (gender.equals(Constants.FEMALE)) {
                isMale = false;
                maleButton.setChecked(false);
                femaleButton.setChecked(true);
            }
        }
    }

    public void clickGender(View view) {
        RadioButton button = (RadioButton) view;
        boolean checked = button.isChecked();
        switch (button.getId()) {
            case R.id.male:
                isMale = checked;
                break;
            case R.id.female:
                isMale = !checked;
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                finish();
                logOut();
                break;
        }
        return true;
    }

    private boolean validateInfo() {
        if (name.length() == 0) {
            name.setError(getResources().getString(R.string.name_error));
            return false;
        }
        return true;
    }

    public void clickGo(View view) {
        requestCameraAudioLocationPermissions();
    }

    private void startSecumChat() {
        if (validateInfo()) {
            final String nickname = name.getText().toString();
            String age = agesArray[agePicker.getSelectedItem()];
            String gender = isMale ? Constants.MALE : Constants.FEMALE;

//            User user = new User();
//            user.setUsername(nickname);
//            currentUserProvider.setUser(user);
//            Intent intent = new Intent(MyDetailsActivity.this, ConversationPreviewActivity.class);
//                        intent.putExtra(Constants.CURRENT_USER, user);
//            startActivity(intent);

            secumAPI.updateUser(
                    new UpdateUserRequest.Builder()
                            .setNickname(nickname)
                            .setAge(age)
                            .setGender(gender)
                            .build()
            ).enqueue(new Callback<UserNew>() {
                @Override
                public void onResponse(Call<UserNew> call, Response<UserNew> response) {

                    if (response.isSuccessful()) {
                        Log.d("BGLM", "Update user success");
                        User user = response.body().userInfo;
                        Log.d("BGLM", "setting user with nickame:" + user.getNickname() + " and userId: " + user.userId);
                        currentUserProvider.setUser(user);
                        Intent intent = new Intent(MyDetailsActivity.this, SecumChatActivity
                                .class);
                        startActivity(intent);
                    } else {
                        Log.d("BGLM", "Update user failure");
                    }
                }

                @Override
                public void onFailure(Call<UserNew> call, Throwable t) {
                    Log.d("BGLM", "Update user failure");
                    showToast(getResources().getString(R.string.general_error));
                }
            });
        }
    }

    @Override
    protected void onAudioCameraLocationPermissionGranted() {
        startSecumChat();
    }

    @Override
    public void onBackPressed() {
        // don't go back
    }
}
