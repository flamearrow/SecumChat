package com.shanjingtech.secumchat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanjingtech.secumchat.model.GetProfileFromUserNameRequest;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shanjingtech.secumchat.util.Constants.CURRENT_USER_NAME;

public class ProfileActivity extends SecumBaseActivity {
    private static final String TAG = "ProfileActivity";
    private String currentUserName;
    private TextView name;
    private TextView age;
    private ImageView avatar;
    private ImageView gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        currentUserName = getIntent().getStringExtra(CURRENT_USER_NAME);
        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        gender = (ImageView) findViewById(R.id.gender);
        avatar = (ImageView) findViewById(R.id.avatar);
        pullUser();
    }

    private void pullUser() {
        secumAPI.getProfileFromUserName(new GetProfileFromUserNameRequest(currentUserName))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        name.setText(user.getNickname());
                        age.setText(user.getAge());
                        gender.setImageResource(user.getGender().equals(Constants.MALE) ? R
                                .drawable.male : R.drawable.female);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(TAG, "Failure to access user.");
                    }
                });
    }

}
