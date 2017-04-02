package com.shanjingtech.secumchat.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.util.Constants;

/**
 * A counter with customized shape for back counting, stealing some ideas from
 * {@link android.widget.Chronometer}
 */
public class SecumCounter extends android.support.v7.widget.AppCompatTextView {

    private static final long MILLIS_IN_SECOND = 1000;

    private Animation shakeAnimation;

    private Animation bounceAnimation;


    public interface SecumCounterListener {
        void onCounterStart();

        void onCounterExpire();

        /**
         * When I want to add time
         */
        void onMeAdd();

        /**
         * When peer wants to add time
         */
        void onPeerAdd();

        /**
         * When both sides want to add time, increase timer
         *
         * @param secondsLeft how many seconds left
         */
        void onAddTimePaired(int secondsLeft);
    }

    private boolean running;
    private boolean meAdd;
    private boolean peerAdd;
    private int secondsLeft;
    private SecumCounterListener listener;

    public SecumCounter(Context context) {
        super(context);
        initializeUI();
    }

    public SecumCounter(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initializeUI();
    }

    public SecumCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeUI();
    }

    public void setSecumCounterListener(SecumCounterListener listener) {
        this.listener = listener;
    }

    private void initializeUI() {
        // set background to cat head
        // center text
        shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        bounceAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        setText("" + Constants.TORA);
        setGravity(Gravity.CENTER);
        setBackground(getResources().getDrawable(R.drawable.cat_timer));
        setTextSize(30);
    }

    /**
     * I clicked addTime
     */
    public void meAdd() {
        if (!meAdd) {
            if (listener != null) {
                listener.onMeAdd();
            }
            meAdd = true;
            checkAddTime();
        }
    }

    /**
     * Peer sent addTime
     */
    public void peerAdd() {
        if (!peerAdd) {
            if (listener != null) {
                listener.onPeerAdd();
            }
            peerAdd = true;
            checkAddTime();
        }
    }

    /**
     * Start shaking!
     */
    private void shake() {
        startAnimation(shakeAnimation);
    }

    /**
     * bounce once
     */
    private void bounce() {
        startAnimation(bounceAnimation);
    }

    /**
     * Freeze, don't move!
     */
    public void freeze() {
        clearAnimation();
    }

    private void checkAddTime() {
        if (meAdd && peerAdd) {
            secondsLeft += Constants.SECONDS_TO_ADD;
            if (listener != null) {
                post(clearAndBounceRunnable);
                listener.onAddTimePaired(secondsLeft);
            }
            meAdd = false;
            peerAdd = false;
        }
    }

    public void initialize() {
        stop();
        freeze();
        secondsLeft = Constants.TORA;
        start();
    }

    private void start() {
        running = true;
        if (listener != null) {
            listener.onCounterStart();
        }
        updateRunning();
    }

    public void stop() {
        running = false;
        freeze();
        updateRunning();
    }

    private void updateRunning() {
        if (running) {
            updateUI();
            postDelayed(tickRunnable, MILLIS_IN_SECOND);
        } else {
            removeCallbacks(tickRunnable);
        }
    }

    private void updateUI() {
        if (secondsLeft < 0) {
            if (running) {
                if (listener != null) {
                    listener.onCounterExpire();
                }
                stop();
            }
        } else {
            if (secondsLeft < Constants.HANG_UP_TIME) {
                shake();
            }
            setText("" + secondsLeft);
            secondsLeft--;
        }
    }

    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            if (running) {
                updateUI();
                postDelayed(tickRunnable, MILLIS_IN_SECOND);
            }
        }
    };

    private final Runnable clearAndBounceRunnable = new Runnable() {
        @Override
        public void run() {
            clearAnimation();
            bounce();
        }
    };

    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new
            DecelerateInterpolator();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).setInterpolator
                        (DECELERATE_INTERPOLATOR);
                break;
            case MotionEvent.ACTION_UP:
                animate().scaleX(1).scaleY(1).setInterpolator(DECELERATE_INTERPOLATOR);
                performClick();
                break;
        }
        return true;

    }
}
