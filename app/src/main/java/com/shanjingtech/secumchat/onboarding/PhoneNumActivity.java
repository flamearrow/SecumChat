package com.shanjingtech.secumchat.onboarding;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.shanjingtech.countrycodepicker.CountryCodePicker;
import com.shanjingtech.secumchat.LoginActivity;
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
    private static final String TAG = "PhoneNumActivity";
    private EditText phoneNumber;
    private CheckBox debugCheckbox;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_activity);
        phoneNumber = (EditText) findViewById(R.id.phone_number);

        if (findViewById(R.id.debug_check_box) != null) {
            SecumDebug.enableDebugMode(sharedPreferences);
            debugCheckbox = (CheckBox) findViewById(R.id.debug_check_box);
        } else {
            SecumDebug.disableDebugMode(sharedPreferences);
        }
        ccp = (CountryCodePicker) findViewById(R.id.country_code);
        tryPrefillPhoneNumber();
    }

    public void debugCheckClicked(View view) {
        if (debugCheckbox.isChecked()) {
            SecumDebug.enableDebugMode(sharedPreferences);
        } else {
            SecumDebug.disableDebugMode(sharedPreferences);
        }
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

    private boolean validatePhone() {
        String number = phoneNumber.getText().toString();

        if (number == null || number.length() == 0) {
            phoneNumber.setError(getResources().getString(R.string.phone_number_cant_be_empty));
            return false;
        }
        if (number.length() > 16) {
            phoneNumber.setError(getResources().getString(R.string.phone_number_too_long));
            return false;
        }
        return true;
    }

    public void clickGo(View view) {
        if (SecumDebug.isDebugMode(sharedPreferences)) {
            if (debugCheckbox.isChecked()) {
                SecumDebug.enableDebugMode(sharedPreferences);
                Intent intent = new Intent(PhoneNumActivity.this,
                        AccessCodeActivity.class);
                intent.putExtra(Constants.PHONE_NUMBER, "12345");
                startActivity(intent);
            }
        } else {
            final String phoneNo = getFullPhoneNumber();
            if (validatePhone()) {
                secumAPI.registerUser(new UserRequest(phoneNo))
                        .enqueue(
                                new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User>
                                            response) {
                                        User user = response.body();
                                        currentUserProvider.setUser(user);
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

    public void privacy(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_URL));
        startActivity(browserIntent);
    }

    public void toDebug(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private String getFullPhoneNumber() {
        Log.d(TAG, "country code: " + ccp.getSelectedCountryCode());
        String countryCode = "+" + ccp.getSelectedCountryCode();
        String number = phoneNumber.getText().toString();
        return countryCode + number;
    }
}
