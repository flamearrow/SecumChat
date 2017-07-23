package com.shanjingtech.secumchat.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.model.AccessCode;
import com.shanjingtech.secumchat.model.AccessCodeRequest;
import com.shanjingtech.secumchat.model.AccessToken;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.ui.AccessCodeLayout;
import com.shanjingtech.secumchat.ui.AutoEnableTextView;
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
    private AutoEnableTextView autoEnableTextView;
    private String phoneNo;
    private boolean isDebug;
    private Button goButton;
    private String correctAccessCode;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_code_activity);
        accessCode = (AccessCodeLayout) findViewById(R.id.access_code);
        accessCode.setOnTextChangedListener(this);
        goButton = (Button) findViewById(R.id.go);
        autoEnableTextView = (AutoEnableTextView) findViewById(R.id.resend);
        // Passed from PhoneNumActivity
        phoneNo = getIntent().getStringExtra(Constants.PHONE_NUMBER);
        isDebug = SecumDebug.isDebugMode(sharedPreferences);
        if (!isDebug) {
            currentUser = currentUserProvider.getUser();
            correctAccessCode = currentUser.getAccess_code();
        }
        autoEnableTextView.startCount();
    }

    public void resend(View view) {
        autoEnableTextView.setEnabled(false);
        autoEnableTextView.startCount();
        if (!isDebug) {
            requestAccessCodeFromServer();
        }
    }

    private void requestAccessCodeFromServer() {
        secumAPI.getAccessCode(new AccessCodeRequest(phoneNo)).enqueue(
                new Callback<AccessCode>() {
                    @Override
                    public void onResponse(Call<AccessCode> call, Response<AccessCode> response) {
                        Log.d(TAG, "send code success");
                        // TODO: buffer it locally, vaidate before send getAccessToken
                        correctAccessCode = response.body().getAccess_code();
                    }

                    @Override
                    public void onFailure(Call<AccessCode> call, Throwable t) {
                        // failed to send code, show error and enable resend
                        showToast(getResources().getString(R.string.fail_to_send_code));
                        Log.d(TAG, "send code failed");
                        autoEnableTextView.setEnabled(true);
                    }
                });
    }

    private boolean validateAccessCode(String code) {
        return code != null && code.equals(correctAccessCode);
    }

    public void clickGo(View view) {
        String code = accessCode.getAccessCode();
        if (isDebug) {
            toDetailsActivity();
        } else {
            if (validateAccessCode(code)) {
                requestAccessTokenAndContinue(code);
            } else {
                showToast(getResources().getString(R.string.incorrect_code));
            }
        }
    }

    private void requestAccessTokenAndContinue(String accessCode) {
        secumAPI.getAccessToken(Constants.PASSWORD, phoneNo, accessCode).enqueue(
                new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        // save access token
                        String accessToken = response.body().getAccess_token();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.SHARED_PREF_ACCESS_TOKEN, accessToken);
                        editor.commit();
                        toDetailsActivity();
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        // incorrect access code, try again
                        showToast(getResources().getString(R.string.incorrect_code));
                    }
                });
    }

    private void toDetailsActivity() {
        Intent intent = new Intent(AccessCodeActivity.this, MyDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onTextChanged(boolean valid) {
        goButton.setEnabled(valid);
    }
}
