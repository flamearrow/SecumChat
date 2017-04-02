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

import static com.shanjingtech.secumchat.SecumChatActivity.State;

/**
 * Layout for {@code SecumChatActivity.State.DIALING},
 * {@code SecumChatActivity.State.RECEIVING} and
 * {@code SecumChatActivity.State.WAITING} states.
 */

public class DialingReceivingWaitingLayout extends GridLayout {
    private ImageView acceptButton;
    private ImageView rejectButton;
    private TextView messageField;
    private Animation enterFromLeft;
    private Animation enterFromRight;
    private Animation exitToLeft;
    private Animation exitToRight;

    public DialingReceivingWaitingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dialing_receiving_waiting_layout, this, true);
        acceptButton = (ImageView) findViewById(R.id.accept_button);
        rejectButton = (ImageView) findViewById(R.id.reject_button);
        messageField = (TextView) findViewById(R.id.message_field);

        enterFromLeft = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_left);
        enterFromRight = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_right);
        exitToLeft = AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_left);
        exitToRight = AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_right);

    }

    public void setMessage(String message) {
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
                break;
            case WAITING:
                acceptButton.startAnimation(exitToRight);
                acceptButton.setVisibility(View.INVISIBLE);
                rejectButton.startAnimation(exitToLeft);
                rejectButton.setVisibility(View.INVISIBLE);
                messageField.setVisibility(View.VISIBLE);
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

                messageField.setVisibility(INVISIBLE);
        }
    }

}
