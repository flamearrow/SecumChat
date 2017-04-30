package com.shanjingtech.secumchat.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.model.GetMatch;

import static com.shanjingtech.secumchat.SecumChatActivity.State;
import static com.shanjingtech.secumchat.util.Constants.FEMALE;
import static com.shanjingtech.secumchat.util.Constants.MALE;

/**
 * Layout for {@code SecumChatActivity.State.DIALING},
 * {@code SecumChatActivity.State.RECEIVING} and
 * {@code SecumChatActivity.State.WAITING} states.
 */

public class DialingReceivingWaitingLayout extends GridLayout {
    private ImageView acceptButton;
    private ImageView rejectButton;
    private TextView messageField;
    private ImageView genderView;
    private Animation enterFromLeft;
    private Animation enterFromRight;
    private Animation exitToLeft;
    private Animation exitToRight;
    private boolean hasGender;

    public DialingReceivingWaitingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dialing_receiving_waiting_layout, this, true);
        acceptButton = (ImageView) findViewById(R.id.accept_button);
        rejectButton = (ImageView) findViewById(R.id.reject_button);
        messageField = (TextView) findViewById(R.id.message_field);
        genderView = (ImageView) findViewById(R.id.gender);

        enterFromLeft = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_left);
        enterFromRight = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_right);
        exitToLeft = AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_left);
        exitToRight = AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_right);

    }

    public void setCalleeMessage(GetMatch getMatch) {
        setMessage(getMatch.getCalleeNickName());
        setGender(getMatch.getCalleeGender());
    }

    public void setCallerMessage(GetMatch getMatch) {
        setMessage(getMatch.getCallerNickName());
        setGender(getMatch.getCallerGender());
    }

    private void setGender(String genderStr) {
        if (MALE.equals(genderStr)) {
            hasGender = true;
            genderView.setImageDrawable(getResources().getDrawable(R.drawable
                    .male_filled_yellow));
        } else if (FEMALE.equals(genderStr)) {
            hasGender = true;
            genderView.setImageDrawable(getResources().getDrawable(R.drawable
                    .female_filled_yellow));
        } else {
            hasGender = false;
        }
    }

    private void setMessage(String message) {
        messageField.setText(message);
    }

    public void switchUIState(State state) {
        switch (state) {
            case DIALING:
            case RECEIVING:
                acceptButton.setVisibility(View.VISIBLE);
                acceptButton.startAnimation(enterFromRight);
                rejectButton.setVisibility(View.VISIBLE);
                rejectButton.startAnimation(enterFromLeft);
                messageField.setVisibility(View.VISIBLE);
                genderView.setVisibility(hasGender ? VISIBLE : GONE);
                break;
            case WAITING:
                acceptButton.startAnimation(exitToRight);
                acceptButton.setVisibility(View.INVISIBLE);
                rejectButton.startAnimation(exitToLeft);
                rejectButton.setVisibility(View.INVISIBLE);
                messageField.setVisibility(View.VISIBLE);
                genderView.setVisibility(hasGender ? VISIBLE : GONE);
                break;
            default:
                if (acceptButton.getVisibility() == VISIBLE) {
                    acceptButton.startAnimation(exitToRight);
                    acceptButton.setVisibility(View.INVISIBLE);
                }
                if (rejectButton.getVisibility() == VISIBLE) {
                    rejectButton.startAnimation(exitToLeft);
                    rejectButton.setVisibility(View.INVISIBLE);
                }
                genderView.setVisibility(INVISIBLE);
                messageField.setVisibility(INVISIBLE);
        }
    }

}
