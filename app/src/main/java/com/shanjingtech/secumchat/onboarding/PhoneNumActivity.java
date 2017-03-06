package com.shanjingtech.secumchat.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserRequest;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Register flow:
 * a) phone number
 * *) access code from sms
 * b) confirm access code
 * c) name/age/gender
 * d) request access
 */

public class PhoneNumActivity extends Activity {
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_activity);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
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
        final String phoneNo = phoneNumber.getText().toString();
        if (validatePhone(phoneNo)) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SecumAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SecumAPI secumAPI = retrofit.create(SecumAPI.class);
            String userName = Constants.USER_NAME_PREVIX + phoneNo;
            UserRequest request = new UserRequest(userName, phoneNo);

            secumAPI.registerUser(request).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Intent intent = new Intent(PhoneNumActivity.this, AccessCodeActivity.class);
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
