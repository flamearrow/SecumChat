package com.meichinijiuchiquba.secumchat.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.meichinijiuchiquba.secumchat.R;

/**
 * An {@code ImageView} that's able to pulse
 */

public class PulseImageView extends androidx.appcompat.widget.AppCompatImageView {
    public static final String DECELERATE = "decelerate";
    public static final String ACCELERATE_DECELERATE = "accelerate_decelerate";
    public static final String OVERSHOOT = "overshoot";
    private AnimatorSet animatorSet;
    private ObjectAnimator scaleXAnimator;
    private ObjectAnimator scaleYAnimator;
    private float animationScaleStart;
    private float animationScaleEnd;
    private int animationDuration;
    private TimeInterpolator animationInterpolator;
    private boolean animationReverse;
    private boolean autoPulse;

    private static final float DEFAULT_SCALE_START = 0.9f;
    private static final float DEFAULT_SCALE_END = 1f;
    private static final int DEFAULT_SCALE_DURATION = 500;

    public PulseImageView(Context context) {
        super(context);
        initializeAnimatorSet();
    }

    public PulseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAnimationProperties(context, attrs);
        initializeAnimatorSet();
    }

    public PulseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAnimationProperties(context, attrs);
        initializeAnimatorSet();
    }

    private void initializeAnimationProperties(Context context, AttributeSet attributeSet) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.PulseImageView,
                0,
                0);
        animationScaleStart = a.getFloat(R.styleable.PulseImageView_animation_scale_start,
                DEFAULT_SCALE_START);
        animationScaleEnd = a.getFloat(R.styleable.PulseImageView_animation_scale_end,
                DEFAULT_SCALE_END);
        animationDuration = a.getInt(R.styleable.PulseImageView_animation_duration,
                DEFAULT_SCALE_DURATION);
        animationReverse = a.getBoolean(R.styleable.PulseImageView_animation_reverse, true);
        autoPulse = a.getBoolean(R.styleable.PulseImageView_animation_auto_pulse, true);
        String interpolator = a.getString(R.styleable.PulseImageView_animation_time_interpolator);
        if (interpolator == null) {
            animationInterpolator = new OvershootInterpolator();
        } else {
            switch (interpolator) {
                case DECELERATE:
                    animationInterpolator = new DecelerateInterpolator();
                    break;
                case ACCELERATE_DECELERATE:
                    animationInterpolator = new AccelerateDecelerateInterpolator();
                    break;
                case OVERSHOOT:
                default:
                    animationInterpolator = new OvershootInterpolator();
                    break;
            }
        }
        a.recycle();
    }

    private void initializeAnimatorSet() {
        animatorSet = new AnimatorSet();
        scaleYAnimator = ObjectAnimator.ofFloat(
                this,
                ImageView.SCALE_Y,
                animationScaleStart,
                animationScaleEnd);
        scaleYAnimator.setRepeatMode(
                animationReverse ? ValueAnimator.REVERSE : ValueAnimator.RESTART);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setDuration(animationDuration);
        scaleYAnimator.setInterpolator(animationInterpolator);


        scaleXAnimator = ObjectAnimator.ofFloat(
                this,
                ImageView.SCALE_X,
                animationScaleStart,
                animationScaleEnd);
        scaleXAnimator.setRepeatMode(
                animationReverse ? ValueAnimator.REVERSE : ValueAnimator.RESTART);
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.setDuration(animationDuration);
        scaleXAnimator.setInterpolator(animationInterpolator);

        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        if (autoPulse) {
            startPulse();
        }
    }

    public void startPulse() {
        animatorSet.start();
    }

    public void stopPulse() {
        animatorSet.cancel();
    }

}
