package com.shanjingtech.secumchat.util;

import android.content.Context;
import android.util.AttributeSet;

/**
 * A counter with customized shape for back counting, stealing some ideas from
 * {@link android.widget.Chronometer}
 */
public class SecumCounter extends android.support.v7.widget.AppCompatTextView {
    private static final long MILLIS_IN_SECOND = 1000;

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
    }

    public SecumCounter(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SecumCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSecumCounterListener(SecumCounterListener listener) {
        this.listener = listener;
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


    private void checkAddTime() {
        if (meAdd && peerAdd) {
            secondsLeft += Constants.SECONDS_TO_ADD;
            if (listener != null) {
                listener.onAddTimePaired(secondsLeft);
            }
            meAdd = false;
            peerAdd = false;
        }
    }

    public void initialize() {
        stop();
        // (TODO)race condition
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

    private void stop() {
        running = false;
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

}
