package com.shanjingtech.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserRequest;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.SecumDebug;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Register flow:
 * a) phone number
 * *) access code from sms
 * b) confirm access code
 * c) name/age/gender
 * d) request access
 */

public class PhoneNumActivity extends SecumBaseActivity {
    private EditText phoneNumber;
    private CheckBox debugCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_activity);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        debugCheckbox = (CheckBox) findViewById(R.id.debug);
    }

    // need to match +11234567890
    private boolean validatePhone(String username) {
        if (username.length() == 0) {
            phoneNumber.setError("Username cannot be empty.");
            return false;
        }
        if (username.length() > 16) {
            phoneNumber.setError("Username too long.");
            return false;
        }
        return true;
    }

    public void clickGo(View view) {
        if (debugCheckbox.isChecked()) {
            SecumDebug.enableDebugMode(this);
            Intent intent = new Intent(PhoneNumActivity.this,
                    AccessCodeActivity.class);
            intent.putExtra(Constants.PHONE_NUMBER, "12345");
            startActivity(intent);
        } else {
            final String phoneNo = phoneNumber.getText().toString();
            if (validatePhone(phoneNo)) {
                secumAPI.registerUser(new UserRequest(Constants.USER_NAME_PREVIX + phoneNo,
                        phoneNo))
                        .enqueue(
                                new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User>
                                            response) {
                                        Intent intent = new Intent(PhoneNumActivity.this,
                                                AccessCodeActivity.class);
                                        intent.putExtra(Constants.PHONE_NUMBER, phoneNo);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                    }

                                });

            }
        }
    }
}
