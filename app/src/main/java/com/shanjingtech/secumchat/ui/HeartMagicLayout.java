package com.shanjingtech.secumchat.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shanjingtech.secumchat.R;

/**
 * When no one likes, display holo heart.
 * When either one likes, display left/right solid heart, start pulsing the heart.
 * When both sides like, explode a full heart, pulse for BACK_TO_NO_LIKE_DELAY millis,
 * then switch back to default state.
 */
public class HeartMagicLayout extends FrameLayout {
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new
            DecelerateInterpolator();
    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR =
            new AccelerateDecelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator
            (4);

    private static final long BACK_TO_NO_LIKE_DELAY = 5000;

    public enum LikeState {
        NO_LIKE, // no one clicked like
        PEER_LIKE, // peer clicked like
        ME_LIKE, // me clicked like
        BOTH_LIKE // both clicked like
    }

    private LikeState currentLikeState;
    private PulseImageView heartView;
    private DotsView vDotsView;
    private CircleView vCircleView;

    private AnimatorSet explodeAnimatorSet;
    private Runnable backToNOLike = new Runnable() {
        @Override
        public void run() {
            switchState(LikeState.NO_LIKE);
        }
    };

    private Runnable updateUIRunnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
        }
    };

    public HeartMagicLayout(@NonNull Context context) {
        super(context);
        initialize();
    }

    public HeartMagicLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public HeartMagicLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeartMagicLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    public void reset() {
        switchState(LikeState.NO_LIKE);
    }

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.pair_like_image, this, true);
        heartView = (PulseImageView) findViewById(R.id.heart_view);
        vDotsView = (DotsView) findViewById(R.id.dots);
        vCircleView = (CircleView) findViewById(R.id.circle);
        initliazeAnimators();
        reset();
    }

    private void initliazeAnimators() {
        explodeAnimatorSet = new AnimatorSet();
        explodeAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                vCircleView.setInnerCircleRadiusProgress(0);
                vCircleView.setOuterCircleRadiusProgress(0);
                vDotsView.setCurrentProgress(0);
                heartView.setScaleX(1);
                heartView.setScaleY(1);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // after both liked, pulse the heart for 5 seconds and back to no like state
                heartView.startPulse();
                postDelayed(backToNOLike, BACK_TO_NO_LIKE_DELAY);
            }
        });
        ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(vCircleView, CircleView
                .OUTER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
        outerCircleAnimator.setDuration(250);
        outerCircleAnimator.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(vCircleView, CircleView
                .INNER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
        innerCircleAnimator.setDuration(200);
        innerCircleAnimator.setStartDelay(200);
        innerCircleAnimator.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator heartScaleYAnimator = ObjectAnimator.ofFloat(heartView, ImageView.SCALE_Y,
                0.2f, 0.9f);
        heartScaleYAnimator.setDuration(350);
        heartScaleYAnimator.setStartDelay(250);
        heartScaleYAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator heartScaleXAnimator = ObjectAnimator.ofFloat(heartView, ImageView.SCALE_X,
                0.2f, 0.9f);
        heartScaleXAnimator.setDuration(350);
        heartScaleXAnimator.setStartDelay(250);
        heartScaleXAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator dotsAnimator = ObjectAnimator.ofFloat(vDotsView, DotsView.DOTS_PROGRESS,
                0, 1f);
        dotsAnimator.setDuration(900);
        dotsAnimator.setStartDelay(50);
        dotsAnimator.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);

        explodeAnimatorSet.playTogether(
                outerCircleAnimator,
                innerCircleAnimator,
                heartScaleYAnimator,
                heartScaleXAnimator,
                dotsAnimator
        );
    }

    private void switchState(LikeState likeState) {
        if (currentLikeState == likeState) {
            return;
        }
        currentLikeState = likeState;
        post(updateUIRunnable);
    }

    private void updateUI() {
        switch (currentLikeState) {
            case NO_LIKE:
                heartView.setImageResource(R.drawable.heart_holo);
                heartView.stopPulse();
                break;
            case ME_LIKE:
                heartView.setImageResource(R.drawable.heart_left_solid);
                heartView.startPulse();
                // big left solid heart, small right heart outline -no animating
                break;
            case PEER_LIKE:
                heartView.setImageResource(R.drawable.heart_right_solid);
                heartView.startPulse();
                // small left heart outline, big right solid heart -animating
                break;
            case BOTH_LIKE:
                heartView.setImageResource(R.drawable.heart_solid);
                heartView.stopPulse();
                explode();
                // notify time is added
                break;
        }
    }

    private void explode() {
        heartView.animate().cancel();
        heartView.setScaleX(0);
        heartView.setScaleY(0);
        vCircleView.setInnerCircleRadiusProgress(0);
        vCircleView.setOuterCircleRadiusProgress(0);
        vDotsView.setCurrentProgress(0);
        explodeAnimatorSet.start();
    }

    public void peerLike() {
        switch (currentLikeState) {
            case NO_LIKE:
                switchState(LikeState.PEER_LIKE);
                break;
            case ME_LIKE:
                switchState(LikeState.BOTH_LIKE);
                break;
        }
    }

    public void meLike() {
        switch (currentLikeState) {
            case NO_LIKE:
                switchState(LikeState.ME_LIKE);
                break;
            case PEER_LIKE:
                switchState(LikeState.BOTH_LIKE);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).setInterpolator
                        (DECELERATE_INTERPOLATOR);
                break;
            case MotionEvent.ACTION_UP:
                animate().scaleX(1).scaleY(1).setInterpolator(DECELERATE_INTERPOLATOR);
                if (currentLikeState != LikeState.BOTH_LIKE) {
                    performClick();
                }
                break;
        }
        return true;

    }
}
