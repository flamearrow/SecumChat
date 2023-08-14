package com.shanjingtech.secumchat.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.shanjingtech.countrycodepicker.CountryCodePicker;
import com.shanjingtech.secumchat.ConversationPreviewActivity;
import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumBaseActivity;
import com.shanjingtech.secumchat.model.AccessCode;
import com.shanjingtech.secumchat.model.AccessCodeRequest;
import com.shanjingtech.secumchat.model.AccessToken;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.model.UserRequest;
import com.shanjingtech.secumchat.ui.AccessCodeLayout;
import com.shanjingtech.secumchat.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Register flow:
 * a) phone number
 * *) access code from sms
 * b) confirm access code
 * c) name/age/gender
 * d) register user
 */

public class PhoneNumActivity extends SecumBaseActivity {
    private static final String TAG = "PhoneNumActivity";
    private EditText phoneNumber;
    private CountryCodePicker ccp;

    private LinearLayout phoneNumberBar;

    private LinearLayout accessBar;

    private Button nextButton;

    // NOTE: This should probably be a member variable.
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    private AccessCodeLayout accessCode;
    private String correctAccessCode;
    private TextView didNotGetCode;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_activity_new);
        nextButton = findViewById(R.id.next_button);
        accessCode = findViewById(R.id.access_code);
        accessCode.setOnTextChangedListener(valid -> nextButton.setEnabled(valid));
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        progressBar = findViewById(R.id.progress_bar);
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (phoneNumberIsValid()) {
                    setButtonEnabled();
                } else {
                    setButtonDisabled();
                }
            }
        });

        phoneNumberBar = findViewById(R.id.phone_number_bar);
        accessBar = findViewById(R.id.access_bar);

        ccp = (CountryCodePicker) findViewById(R.id.country_code);

        didNotGetCode = findViewById(R.id.did_not_get_code_field);
        configureDidNotGetCode();
        // TODO: fix this
        // tryPrefillPhoneNumber();
    }

    private void configureDidNotGetCode() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                setButtonEnabled();
                requestAccessCode();
            }
        };

        // Create a SpannableStringBuilder and attach the ClickableSpan
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        String didNotGetCodeStr = getResources().getString(R.string.did_not_get_code);
        ssb.append(didNotGetCodeStr);
        ssb.append(" ");
        ssb.append(getResources().getString(R.string.resend_code));
        ssb.setSpan(clickableSpan, didNotGetCodeStr.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the text of the TextView with the AnnotatedString
        didNotGetCode.setText(ssb);
        didNotGetCode.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void tryPrefillPhoneNumber() {
        requestPhoneStatePermissions();
    }

    @Override
    protected void onPhoneStatePermissionGranted() {
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String countryCode = tm.getSimCountryIso();
            String number = tm.getLine1Number();
            if (!number.startsWith("+")) {
                number = "+" + number;
            }
            ccp.setCountryForNameCode(countryCode);
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(number, "");
            phoneNumber.setText("" + numberProto.getNationalNumber());
        } catch (Exception e) {
            Log.d(TAG, "Prefill phone number failed");
        }
    }

    private void setButtonLoading() {
        nextButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setButtonEnabled() {
        nextButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    private void setButtonDisabled() {
        nextButton.setEnabled(false);
        progressBar.setVisibility(View.GONE);
    }

    private boolean phoneNumberIsValid() {
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber.getText().toString(), ccp.getSelectedCountryNameCode());
            return phoneUtil.isValidNumber(numberProto);
        } catch (NumberParseException e) {
            // not parsed
        }
        return false;

    }

    public void clickGo(View view) {
        // inputting phone number
        if (phoneNumberBar.getVisibility() == View.VISIBLE) {
            setButtonLoading();
            registerUserAndRequestAccessCode();
        }
        // inputting access code
        else {
            String code = accessCode.getAccessCode();
            accessCode.setEnabled(false);
            if (code != null && code.equals(correctAccessCode)) {
                setButtonLoading();
                secumAPI.getAccessToken(Constants.PASSWORD, getFullPhoneNumber(), correctAccessCode).enqueue(
                        new Callback<AccessToken>() {
                            @Override
                            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                                accessCode.setEnabled(true);
                                // save access token
                                String accessToken = response.body().getAccess_token();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.SHARED_PREF_ACCESS_TOKEN, accessToken);
                                editor.commit();

                                Intent intent = new Intent(PhoneNumActivity.this, ConversationPreviewActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<AccessToken> call, Throwable t) {
                                accessCode.setEnabled(true);
                                setButtonEnabled();
                                showToast(getResources().getString(R.string.something_went_wrong));
                            }
                        });
            } else {
                showToast(getResources().getString(R.string.incorrect_code));
            }
        }
    }


    private void registerUserAndRequestAccessCode() {
        secumAPI.registerUser(new UserRequest(getFullPhoneNumber()))
                .enqueue(
                        new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User>
                                    response) {
                                User user = response.body();
                                currentUserProvider.setUser(user);
                                requestAccessCode();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                setButtonEnabled();
                                showToast(getResources().getString(R.string.something_went_wrong));
                            }

                        });
    }

    private void requestAccessCode() {
        secumAPI.getAccessCode(new AccessCodeRequest(getFullPhoneNumber())).enqueue(
                new Callback<AccessCode>() {
                    @Override
                    public void onResponse(Call<AccessCode> call, Response<AccessCode> response) {
                        correctAccessCode = response.body().getAccess_code();
                        phoneNumberBar.setVisibility(View.GONE);
                        accessBar.setVisibility(View.VISIBLE);
                        setButtonDisabled();
                    }

                    @Override
                    public void onFailure(Call<AccessCode> call, Throwable t) {
                        setButtonEnabled();
                        showToast(getResources().getString(R.string.fail_to_send_code));
                    }
                });
    }

    private String getFullPhoneNumber() {
        Log.d(TAG, "country code: " + ccp.getSelectedCountryCode());
        String countryCode = "+" + ccp.getSelectedCountryCode();
        String number = phoneNumber.getText().toString();
        return countryCode + number;
    }
}
