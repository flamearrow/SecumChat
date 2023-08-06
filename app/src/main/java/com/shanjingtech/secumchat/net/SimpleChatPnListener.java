package com.shanjingtech.secumchat.net;

import android.util.Log;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.shanjingtech.secumchat.db.Message;
import com.shanjingtech.secumchat.db.MessageDAO;

import org.jetbrains.annotations.NotNull;


public class SimpleChatPnListener extends SubscribeCallback {

    private MessageDAO messageDAO;

    public SimpleChatPnListener(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    @Override
    public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
        Log.d("BGLM", "status: " + status.getOperation());
        Log.d("BGLM", "category: " + status.getCategory());
        switch (status.getOperation()) {
            // combine unsubscribe and subscribe handling for ease of use
            case PNSubscribeOperation:
            case PNUnsubscribeOperation:
                // Note: subscribe statuses never have traditional errors,
                // just categories to represent different issues or successes
                // that occur as part of subscribe
                switch (status.getCategory()) {
                    case PNConnectedCategory:
                        // No error or issue whatsoever.
                    case PNReconnectedCategory:
                        // Subscribe temporarily failed but reconnected.
                        // There is no longer any issue.
                    case PNDisconnectedCategory:
                        // No error in unsubscribing from everything.
                    case PNUnexpectedDisconnectCategory:
                        // Usually an issue with the internet connection.
                        // This is an error: handle appropriately.
                    case PNAccessDeniedCategory:
                        // Access Manager does not allow this client to subscribe to this
                        // channel and channel group configuration. This is
                        // another explicit error.
                    default:
                        // You can directly specify more errors by creating
                        // explicit cases for other error categories of
                        // `PNStatusCategory` such as `PNTimeoutCategory` or
                        // `PNMalformedFilterExpressionCategory` or
                        // `PNDecryptionErrorCategory`.
                }

            case PNHeartbeatOperation:
                // Heartbeat operations can in fact have errors, so it's important to check first for an error.
                // For more information on how to configure heartbeat notifications through the status
                // PNObjectEventListener callback, refer to
                // /docs/sdks/android/api-reference/configuration#configuration_basic_usage
                if (status.isError()) {
                    // There was an error with the heartbeat operation, handle here
                } else {
                    // heartbeat operation was successful
                }
            default: {
                // Encountered unknown status type
            }
        }
    }

    @Override
    public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
        String messagePublisher = pnMessageResult.getPublisher();
        Log.d("BGLM", "Message publisher: " + messagePublisher);
        Log.d("BGLM", "Message Payload: " + pnMessageResult.getMessage());
        Log.d("BGLM", "Message Subscription: " + pnMessageResult.getSubscription());
        Log.d("BGLM", "Message Channel: " + pnMessageResult.getChannel());
        Log.d("BGLM", "Message timetoken: " + pnMessageResult.getTimetoken());

//        Message message = new Message.Builder().setMessageId()
        messageDAO.insertMessage(Message.fromPNMessageResult(pnMessageResult));
    }

    @Override
    public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
        Log.d("BGLM", "Presence Event: " + presence.getEvent());
        // Can be join, leave, state-change or timeout

        Log.d("BGLM", "Presence Channel: " + presence.getChannel());
        // The channel to which the message was published

        Log.d("BGLM", "Presence Occupancy: " + presence.getOccupancy());
        // Number of users subscribed to the channel

        Log.d("BGLM", "Presence State: " + presence.getState());
        // User state

        Log.d("BGLM", "Presence UUID: " + presence.getUuid());
        // UUID to which this event is related

        presence.getJoin();
        // List of users that have joined the channel (if event is 'interval')

        presence.getLeave();
        // List of users that have left the channel (if event is 'interval')

        presence.getTimeout();
        // List of users that have timed-out off the channel (if event is 'interval')

        presence.getHereNowRefresh();
        // Indicates to the client that it should call 'hereNow()' to get the
        // complete list of users present in the channel.
    }

    @Override
    public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult signal) {
        Log.d("BGLM", "Signal publisher: " + signal.getPublisher());
        Log.d("BGLM", "Signal payload: " + signal.getMessage());
        Log.d("BGLM", "Signal subscription: " + signal.getSubscription());
        Log.d("BGLM", "Signal channel: " + signal.getChannel());
        Log.d("BGLM", "Signal timetoken: " + signal.getTimetoken());
    }

    @Override
    public void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {

    }

    @Override
    public void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult) {

    }

    @Override
    public void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult) {

    }

    @Override
    public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {
        PNMessageAction pnMessageAction = pnActionResult.getMessageAction();
        Log.d("BGLM", "faewfeawfeaw");
        Log.d("BGLM", "Message action type: " + pnMessageAction.getType());
        Log.d("BGLM", "Message action value: " + pnMessageAction.getValue());
        Log.d("BGLM", "Message action uuid: " + pnMessageAction.getUuid());
        Log.d("BGLM", "Message action actionTimetoken: " + pnMessageAction.getActionTimetoken());
        Log.d("BGLM", "Message action messageTimetoken: " + pnMessageAction.getMessageTimetoken());
        Log.d("BGLM", "Message action subscription: " + pnActionResult.getSubscription());
        Log.d("BGLM", "Message action channel: " + pnActionResult.getChannel());
        Log.d("BGLM", "Message action timetoken: " + pnActionResult.getTimetoken());
    }

    @Override
    public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {
        Log.d("BGLM", "File channel: " + pnFileEventResult.getChannel());
        Log.d("BGLM", "File publisher: " + pnFileEventResult.getPublisher());
        Log.d("BGLM", "File message: " + pnFileEventResult.getMessage());
        Log.d("BGLM", "File timetoken: " + pnFileEventResult.getTimetoken());
        Log.d("BGLM", "File file.id: " + pnFileEventResult.getFile().getId());
        Log.d("BGLM", "File file.name: " + pnFileEventResult.getFile().getName());
        Log.d("BGLM", "File file.url: " + pnFileEventResult.getFile().getUrl());
    }
}
