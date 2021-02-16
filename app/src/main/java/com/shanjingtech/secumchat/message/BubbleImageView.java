package com.shanjingtech.secumchat.message;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.shanjingtech.secumchat.R;

public class BubbleImageView extends AppCompatImageView {
    private boolean pressed = false;
    public static final int GRAVITY_LEFT = 0;
    public static final int GRAVITY_RIGHT = 1;
    private boolean showClickIndication = false;
    private int bubbleGravity = GRAVITY_LEFT, bubbleColor = Color.GREEN, pressedColor = Color.BLUE;

    public BubbleImageView(Context context) {
        super(context);
        updateDrawble();
    }

    public BubbleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        updateDrawble();
    }

    public BubbleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getAttrs(attrs);
        updateDrawble();
    }


    private void getAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.ChatBubbleImageView);
        try {
            // Gravity of the bubble. Left or Right.
            bubbleGravity = a.getInt(
                    R.styleable.ChatBubbleImageView_bubble_gravity,
                    GRAVITY_LEFT);

            // Bubble color. The color could be changed when loading the the image url.
            bubbleColor = a.getColor(
                    R.styleable.ChatBubbleImageView_bubble_color,
                    Color.GREEN);

            // The color of the bubble when pressed.
            pressedColor = a.getColor(
                    R.styleable.ChatBubbleImageView_bubble_pressed_color,
                    Color.BLUE);
        } finally {
            a.recycle();
        }
    }


    private void updateDrawble() {
        if (bubbleGravity == GRAVITY_RIGHT) {
            setBackgroundResource(R.drawable.bubble_right);
        } else {
            setBackgroundResource(R.drawable.bubble_left);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (!showClickIndication)
            return;

        if (!pressed && isPressed()) {
            pressed = true;
            invalidate();
        } else if (pressed && !isPressed()) {
            pressed = false;
            invalidate();
        }
    }


    public void setBubbleGravity(int bubbleGravity) {
        this.bubbleGravity = bubbleGravity;
    }

    public void setBubbleColor(int bubbleColor) {
        this.bubbleColor = bubbleColor;
    }

    public int getBubbleGravity() {
        return bubbleGravity;
    }

    public int getBubbleColor() {
        return bubbleColor;
    }

}