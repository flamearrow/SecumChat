package com.shanjingtech.secumchat.message;

/**
 * Provides peer's username for the current conversation.
 *
 * If not chatting with a peer through {@link SecumMessageActivity} at the moment,
 * {@link #peerName} will be null.
 *
 */
public class CurrentPeerUserNameProvider {
    String peerName = null;

    public String getPeerUserName() {
        return peerName;
    }

    public void setPeerUserName(String peerName) {
        this.peerName = peerName;
    }

    public boolean isPeerUserNameEqualTo(String expectedUserName) {
        return peerName == null ? false : peerName.equals(expectedUserName);
    }
}
