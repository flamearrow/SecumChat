package com.shanjingtech.secumchat.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Image view to display like states
 */
public class PairLikeImageView extends android.support.v7.widget.AppCompatImageView {
    public enum LikeState {
        NO_LIKE, // no one clicked like
        PEER_LIKE, // peer clicked like
        ME_LIKE, // me clicked like
        BOTH_LIKE // both clicked like
    }

    private LikeState currentLikeState;

    public PairLikeImageView(Context context) {
        super(context);
    }

    public PairLikeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PairLikeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initialize() {
        currentLikeState = LikeState.NO_LIKE;
    }

    private void switchState(LikeState likeState) {
        if (currentLikeState == likeState) {
            return;
        }
        currentLikeState = likeState;
        updateUI();
    }

    private void updateUI() {
        switch (currentLikeState) {
            case NO_LIKE:
                // small static heart outline
                break;
            case ME_LIKE:
                // big left solid heart, small right heart outline -no animating
                break;
            case PEER_LIKE:
                // small left heart outline, big right solid heart -animating
                break;
            case BOTH_LIKE:
                // big solid full heart animating: explode and go back to NO_IKE, animation needs
                // to notify time add
                // add animation listener, when animation ends, switch back to no like
                break;
        }
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
}
