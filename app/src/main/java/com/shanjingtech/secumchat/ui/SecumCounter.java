package com.shanjingtech.secumchat.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
public class SecumCounter extends androidx.appcompat.widget.AppCompatTextView {
    public static final String SECUMCOUNTER = "SecumCounter";

    private static final long MILLIS_IN_SECOND = 1000;

    private Animation shakeAnimation;

    private Animation bounceAnimation;

    interface BothAddedListener {
        void onBothSidesAdded();
    }

    public interface SecumCounterListener {
        void onCounterStart();

        void onCounterStop();

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

    private volatile boolean running;
    private boolean meAdd;
    private boolean peerAdd;
    private int secondsLeft;
    private SecumCounterListener secumCounterListener;
    private BothAddedListener bothAddedListener;
    private boolean bouncing;

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
        this.secumCounterListener = listener;
    }

    public void setBothAddedListener(BothAddedListener listener) {
        this.bothAddedListener = listener;
    }

    private void initializeUI() {
        // set background to cat head
        // center text
        shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        bounceAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (bouncing) {
                    startAnimation(bounceAnimation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
            if (secumCounterListener != null) {
                secumCounterListener.onMeAdd();
            }
            meAdd = true;
            checkAddTime();
        } else {
            Log.d(SECUMCOUNTER, "me already add!");
        }
    }

    /**
     * Peer sent addTime
     */
    public void peerAdd() {
        if (!peerAdd) {
            if (secumCounterListener != null) {
                secumCounterListener.onPeerAdd();
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
        bouncing = true;
        post(new Runnable() {
            @Override
            public void run() {
                startAnimation(bounceAnimation);
            }
        });
    }

    /**
     * Freeze, don't move!
     */
    private void freeze() {
        bouncing = false;
        clearAnimation();
    }

    private void checkAddTime() {
        if (meAdd && peerAdd) {
            secondsLeft += Constants.SECONDS_TO_ADD;
            if (secumCounterListener != null) {
                secumCounterListener.onAddTimePaired(secondsLeft);
            }
            if (bothAddedListener != null) {
                bothAddedListener.onBothSidesAdded();
            }
            meAdd = false;
            peerAdd = false;
        } else if (meAdd || peerAdd) {
            bounce();
        }
    }

    public void initialize() {
        stop();
        freeze();
        meAdd = false;
        peerAdd = false;
        secondsLeft = Constants.TORA;
        start();
    }

    private void start() {
        running = true;
        if (secumCounterListener != null) {
            secumCounterListener.onCounterStart();
        }
        updateRunning();
    }

    public void stop() {
        running = false;
        if (secumCounterListener != null) {
            secumCounterListener.onCounterStop();
        }
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
                if (secumCounterListener != null) {
                    secumCounterListener.onCounterExpire();
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
