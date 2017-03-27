package com.shanjingtech.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.SecumChatActivity;
import com.shanjingtech.secumchat.model.UpdateUserRequest;
import com.shanjingtech.secumchat.model.UpdateUserResponse;
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
    private HorizontalPicker agePicker;
    private boolean isMale;
    private String[] agesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_details_activity);
        name = (EditText) findViewById(R.id.my_name);
        agePicker = (HorizontalPicker) findViewById(R.id.my_age);
        agesArray = getResources().getStringArray(R.array.ages);
        isMale = false;
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

    private boolean validateInfo() {
        if (name.length() == 0) {
            name.setError("name cannot be empty.");
            return false;
        }
        return true;
    }

    public void clickGo(View view) {
        if (requestSecumPermissions()) {
            if (validateInfo()) {
//                Intent intent = new Intent(this, SecumChatActivity.class);
//                String age = agesArray[agePicker.getSelectedItem()];
//                intent.putExtra(Constants.MY_NAME, name.getText().toString());
//                intent.putExtra(Constants.MY_AGE, age);
//                intent.putExtra(Constants.ME_MALE, isMale);
//                startActivity(intent);
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
                            // TODO: pass in user
                            User user = response.body();
                            Intent intent = new Intent(MyDetailsActivity.this, SecumChatActivity
                                    .class);
                            intent.putExtra(Constants.MY_NAME, user.getUsername());
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        showToast(getResources().getString(R.string.general_error));
                    }
                });
            }
        }
    }
}
