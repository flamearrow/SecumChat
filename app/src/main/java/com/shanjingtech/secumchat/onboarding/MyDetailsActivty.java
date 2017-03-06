package com.shanjingtech.secumchat.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.SecumChatActivity;

/**
 * Input you name, age and gender
 */

public class MyDetailsActivty extends Activity {
    private EditText name;
    private EditText age;
    private boolean isMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_details_activity);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
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
        if (age.length() == 0) {
            age.setError("age cannot be empty.");
            return false;
        }
        return true;
    }

    public void clickGo(View view) {
        // TODO: need another api call to link userinfo to phone number
        // pass name, age and gender
        validateInfo();
        // populate name here
        Intent intent = new Intent(this, SecumChatActivity.class);
        startActivity(intent);

    }
}
