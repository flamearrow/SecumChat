package com.shanjingtech.secumchat.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.shanjingtech.secumchat.R;

/**
 * An {@code TextView} that enables itself after a certain period.
 */

public class AutoEnableTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final int DEFAULT_COUNT = 60;
    private int count;
    private int residue;
    private String originalText;
    private Runnable enableSelfRunnable = new Runnable() {
        @Override
        public void run() {
            if (residue <= 0) {
                setText(originalText);
                AutoEnableTextView.this.setEnabled(true);
            } else {
                residue--;
                setText(originalText + "(" + residue + ")");
                // Running on UI thread
                postDelayed(this, 1000);
            }
        }
    };

    public AutoEnableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEnabled(false);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AutoEnableTextView,
                0,
                0);

        count = a.getInt(R.styleable.AutoEnableTextView_timeout_in_sec, DEFAULT_COUNT);
        a.recycle();
    }

    /**
     * Start count, after calling this method, no more {@link #setText(CharSequence)} should be
     * called
     */
    public void startCount() {
        residue = count;
        originalText = getText().toString();
        setText(originalText + "(" + residue + ")");
        postDelayed(enableSelfRunnable, 1000);
    }

}
