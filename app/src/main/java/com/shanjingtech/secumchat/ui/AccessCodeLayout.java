package com.shanjingtech.secumchat.ui;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.TextView;

import com.shanjingtech.secumchat.R;

/**
 * Layout to input access code, composed of 4 textViews
 */

public class AccessCodeLayout extends GridLayout implements
        TextWatcher {
    public interface OnTextChangedListener {
        void onTextChanged(boolean valid);
    }

    private OnTextChangedListener onTextChangedListener;
    private TextView tt1;
    private TextView tt2;
    private TextView tt3;
    private TextView tt4;

    public AccessCodeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.access_code_layout, this, true);
        tt1 = (TextView) findViewById(R.id.field1);
        tt2 = (TextView) findViewById(R.id.field2);
        tt3 = (TextView) findViewById(R.id.field3);
        tt4 = (TextView) findViewById(R.id.field4);
        tt1.addTextChangedListener(this);

        tt2.addTextChangedListener(this);

        tt3.addTextChangedListener(this);

        tt4.addTextChangedListener(this);
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        tt1.setEnabled(enabled);
        tt2.setEnabled(enabled);
        tt3.setEnabled(enabled);
        tt4.setEnabled(enabled);
    }

    public String getAccessCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(tt1.getText().toString())
                .append(tt2.getText().toString())
                .append(tt3.getText().toString())
                .append(tt4.getText().toString());
        return sb.toString();
    }

    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.onTextChangedListener = listener;
    }

    private boolean validate() {
        return tt1.getText().toString().length() == 1 &&
                tt2.getText().toString().length() == 1 &&
                tt3.getText().toString().length() == 1 &&
                tt4.getText().toString().length() == 1;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (onTextChangedListener != null) {
            onTextChangedListener.onTextChanged(validate());
        }
        updateFocus(s.length() == 1);
    }

    /**
     * If should move forward, move to next, otherwise move backward
     *
     * @param shouldMoveForward
     */
    private void updateFocus(boolean shouldMoveForward) {
        if (shouldMoveForward) {
            if (tt1.hasFocus()) {
                tt1.clearFocus();
                tt2.requestFocus();
            } else if (tt2.hasFocus()) {
                tt2.clearFocus();
                tt3.requestFocus();
            } else if (tt3.hasFocus()) {
                tt3.clearFocus();
                tt4.requestFocus();
            }
        } else {
            if (tt4.hasFocus()) {
                tt4.clearFocus();
                tt3.requestFocus();
            } else if (tt3.hasFocus()) {
                tt3.clearFocus();
                tt2.requestFocus();
            } else if (tt2.hasFocus()) {
                tt2.clearFocus();
                tt1.requestFocus();
            }
        }
    }
}
