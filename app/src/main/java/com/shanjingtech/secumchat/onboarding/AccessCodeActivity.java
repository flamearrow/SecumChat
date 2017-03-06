package com.shanjingtech.secumchat.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.model.AccessCode;
import com.shanjingtech.secumchat.model.AccessCodeRequest;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.util.Constants;
import com.shanjingtech.secumchat.util.PermissionRequester;

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
public class AccessCodeActivity extends Activity {
    private TextView accesCode;
    private String phoneNo;
    private String correctAccessCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_code_activity);
        accesCode = (TextView) findViewById(R.id.access_code);
        // Passed from PhoneNumActivity
        phoneNo = getIntent().getStringExtra(Constants.PHONE_NUMBER);
        requestAccessCodeFromServer();
    }

    private void requestAccessCodeFromServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SecumAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SecumAPI secumAPI = retrofit.create(SecumAPI.class);

        secumAPI.getAccessCode(new AccessCodeRequest(phoneNo)).enqueue(new Callback<AccessCode>() {
            @Override
            public void onResponse(Call<AccessCode> call, Response<AccessCode> response) {
                AccessCode code = response.body();
                correctAccessCode = code.getAccess_code();
            }

            @Override
            public void onFailure(Call<AccessCode> call, Throwable t) {

            }
        });
    }

    private boolean validateAccessCode(String code) {
        return code != null && code.equals(correctAccessCode);
    }

    public void clickGo(View view) {
        String code = accesCode.getText().toString();
        if (validateAccessCode(code)) {
            Intent intent = null;
            if (PermissionRequester.needToRequestPermission()) {
                intent = new Intent(this, PermissionRequestActivity.class);
            } else {
                String username = Constants.USER_NAME_PREVIX + phoneNo;
                intent.putExtra(Constants.MY_NAME, username);
                intent = new Intent(this, MyDetailsActivty.class);
            }
            startActivity(intent);
        } else {
            accesCode.setError("Incorrect access code");
        }
    }

}
