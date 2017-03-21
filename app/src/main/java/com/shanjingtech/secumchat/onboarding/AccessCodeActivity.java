package com.shanjingtech.secumchat.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.model.AccessCode;
import com.shanjingtech.secumchat.model.AccessCodeRequest;
import com.shanjingtech.secumchat.ui.AccessCodeLayout;
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
public class AccessCodeActivity extends SecumBaseActivity
        implements AccessCodeLayout.OnTextChangedListener {
    public static final String TAG = "AccessCodeActivity";
    private AccessCodeLayout accessCode;
    private String phoneNo;
    private String correctAccessCode;
    private boolean isDebug;
    private Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_code_activity);
        accessCode = (AccessCodeLayout) findViewById(R.id.access_code);
        accessCode.setOnTextChangedListener(this);
        goButton = (Button) findViewById(R.id.go);
        // Passed from PhoneNumActivity
        phoneNo = getIntent().getStringExtra(Constants.PHONE_NUMBER);
        isDebug = SecumDebug.isDebugMode(this);
        if (!isDebug) {
            requestAccessCodeFromServer();
        }
    }

    public void resend(View view) {
        Log.d(TAG, "resend");
        if (!isDebug) {
            requestAccessCodeFromServer();
        }
    }

    private void requestAccessCodeFromServer() {
        secumAPI.getAccessCode(new AccessCodeRequest(phoneNo)).enqueue(
                new Callback<AccessCode>() {
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
        String code = accessCode.getAccessCode();
        if (isDebug || validateAccessCode(code)) {
            Intent intent = new Intent(this, MyDetailsActivty.class);
            String username = Constants.USER_NAME_PREVIX + phoneNo;
            intent.putExtra(Constants.MY_NAME, username);
            startActivity(intent);
        }
    }

    @Override
    public void onTextChanged(boolean valid) {
        Log.d(TAG, "onTextChanged, validation: " + valid);
        goButton.setEnabled(valid);
    }
}
