package com.meichinijiuchiquba.secumchat.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.meichinijiuchiquba.secumchat.R;


public class HeartSecumCounter extends FrameLayout implements Animation.AnimationListener,
        SecumCounter.BothAddedListener {
    private SecumCounter secumCounter;
    private ImageView heartView;
    private Animation counterShrink;
    private Animation counterExpand;
    private Animation heartExpand;

    public HeartSecumCounter(@NonNull Context context) {
        super(context);
        initialize();
    }

    public HeartSecumCounter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public HeartSecumCounter(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeartSecumCounter(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.heart_secum_counter, this, true);
        heartView = (ImageView) findViewById(R.id.heart_view);
        secumCounter = (SecumCounter) findViewById(R.id.secum_counter);
        secumCounter.setBothAddedListener(this);
        initializeAnimators();
    }

    private void initializeAnimators() {
        counterShrink = AnimationUtils.loadAnimation(getContext(), R.anim.counter_shrink);
        counterShrink.setAnimationListener(this);
        counterExpand = AnimationUtils.loadAnimation(getContext(), R.anim.counter_expand);
        counterExpand.setAnimationListener(this);
        heartExpand = AnimationUtils.loadAnimation(getContext(), R.anim.heart_expand);
        heartExpand.setAnimationListener(this);
    }

    public void explode() {
        post(new Runnable() {
            @Override
            public void run() {
                heartView.setVisibility(VISIBLE);
                secumCounter.startAnimation(counterShrink);
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == counterShrink) {
            heartView.startAnimation(heartExpand);
        } else if (animation == heartExpand) {
            secumCounter.startAnimation(counterExpand);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public SecumCounter getSecumCounter() {
        return secumCounter;
    }

    public void setSecumCounterListener(SecumCounter.SecumCounterListener listener) {
        secumCounter.setSecumCounterListener(listener);
    }

    public void meAdd() {
        secumCounter.meAdd();
    }

    public void peerAdd() {
        secumCounter.peerAdd();
    }

    public void restart() {
        secumCounter.initialize();
    }

    public void stop() {
        secumCounter.stop();
    }

    @Override
    public void onBothSidesAdded() {
        explode();
    }
}
