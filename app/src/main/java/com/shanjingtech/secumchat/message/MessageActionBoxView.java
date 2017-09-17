package com.shanjingtech.secumchat.message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shanjingtech.secumchat.R;

public class MessageActionBoxView extends LinearLayout implements View.OnClickListener, View
        .OnKeyListener, TextView.OnEditorActionListener {

    public static final String TAG = MessageActionBoxView.class.getSimpleName();

    protected MessageBoxOptionsListener messageBoxOptionsListener;
    protected MessageSendListener messageSendListener;
    protected TextView btnSend;
    protected ImageButton btnOptions;
    protected EditText etMessage;
    protected PopupWindow optionPopup;


    public MessageActionBoxView(Context context) {
        super(context);
        init();
    }

    public MessageActionBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageActionBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.message_box_view, this);
    }

    protected void initViews() {
        btnSend = (TextView) findViewById(R.id.send_message);
        btnOptions = (ImageButton) findViewById(R.id.btn_options);
        etMessage = (EditText) findViewById(R.id.message_to_send);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();

        if (isInEditMode())
            return;

        btnSend.setOnClickListener(this);

        btnOptions.setOnClickListener(this);

        etMessage.setOnEditorActionListener(this);
        etMessage.setOnKeyListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.send_message) {
            if (messageSendListener != null)
                messageSendListener.onMessageSent(getMessageText());
        }
        // TODO(mlgb): implement image and location later
//        else if (id == R.id.chat_sdk_btn_options) {
//            boolean b = false;
//            if (messageBoxOptionsListener != null) {
//                b = messageBoxOptionsListener.onOptionButtonPressed();
//            }
//
//            if (!b)
//                showOptionPopup();
//        } else if (id == R.id.chat_sdk_btn_choose_picture) {
//            dismissOptionPopup();
//
//            if (messageBoxOptionsListener != null)
//                messageBoxOptionsListener.onPickImagePressed();
//        } else if (id == R.id.chat_sdk_btn_take_picture) {
//            if (!Utils.SystemChecks.checkCameraHardware(getContext())) {
//                Toast.makeText(getContext(), "This device does not have a camera.", Toast
//                        .LENGTH_SHORT).show();
//                return;
//            }
//
//            dismissOptionPopup();
//
//            if (messageBoxOptionsListener != null)
//                messageBoxOptionsListener.onTakePhotoPressed();
//        } else if (id == R.id.chat_sdk_btn_location) {
//            dismissOptionPopup();
//
//            if (messageBoxOptionsListener != null)
//                messageBoxOptionsListener.onLocationPressed();
//        }
    }

    /**
     * Send a text message when the done button is pressed on the keyboard.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND)
            if (messageSendListener != null)
                messageSendListener.onMessageSent(getMessageText());

        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // if enter is pressed start calculating
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//            int editTextLineCount = ((EditText) v).getLineCount();
//            if (editTextLineCount >= getResources().getInteger(R.integer
//                    .chat_sdk_max_message_lines))
//                return true;
//        }
        return false;
    }

    public void setMessageBoxOptionsListener(MessageBoxOptionsListener messageBoxOptionsListener) {
        this.messageBoxOptionsListener = messageBoxOptionsListener;
    }

    public void setMessageSentListener(MessageSendListener messageSendListener) {
        this.messageSendListener = messageSendListener;
    }

    public String getMessageText() {
        return etMessage.getText().toString();
    }

    public void clearText() {
        etMessage.getText().clear();
    }


    public interface MessageBoxOptionsListener {
        public void onLocationPressed();

        public void onTakePhotoPressed();

        public void onPickImagePressed();

        /**
         * Invoked when the option button pressed, If returned true the system wont show the
         * option popup.
         */
        public boolean onOptionButtonPressed();
    }

    public interface MessageSendListener {
        public void onMessageSent(String text);
    }
}
