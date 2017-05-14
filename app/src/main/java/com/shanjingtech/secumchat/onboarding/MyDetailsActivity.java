package com.shanjingtech.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.SecumChatActivity;
import com.shanjingtech.secumchat.model.UpdateUserRequest;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.util.Constants;
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
        currentUser = (User) getIntent().getSerializableExtra(Constants.CURRENT_USER);
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
        inflater.inflate(R.menu.menu_profile, menu);
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
            secumAPI.updateUser(
                    new UpdateUserRequest.Builder()
                            .setNickname(nickname)
                            .setAge(age)
                            .setGender(gender)
                            .build()
            ).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();
                        Intent intent = new Intent(MyDetailsActivity.this, SecumChatActivity
                                .class);
                        intent.putExtra(Constants.CURRENT_USER, user);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
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
