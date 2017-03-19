package com.shanjingtech.secumchat.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanjingtech.secumchat.R;

import static com.shanjingtech.secumchat.SecumChatActivity.State;

/**
 * Layout for {@code SecumChatActivity.State.DIALING},
 * {@code SecumChatActivity.State.RECEIVING} and
 * {@code SecumChatActivity.State.WAITING} states.
 */

public class DialingReceivingWaitingLayout extends LinearLayout {
    private Button acceptButton;
    private Button rejectButton;
    private TextView messageField;

    public DialingReceivingWaitingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dialing_receiving_waiting_layout, this, true);
        acceptButton = (Button) findViewById(R.id.accept_button);
        rejectButton = (Button) findViewById(R.id.reject_button);
        messageField = (TextView) findViewById(R.id.message_field);
    }

    public void setMessage(String message) {
        messageField.setText(message);
    }

    public void switchUIState(State state) {
        switch (state) {
            case DIALING:
            case RECEIVING:
                acceptButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
                messageField.setVisibility(View.VISIBLE);
                break;
            case WAITING:
                acceptButton.setVisibility(View.INVISIBLE);
                rejectButton.setVisibility(View.INVISIBLE);
                messageField.setVisibility(View.VISIBLE);
                break;
        }
    }

}
