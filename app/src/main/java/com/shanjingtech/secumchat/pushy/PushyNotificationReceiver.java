package com.shanjingtech.secumchat.pushy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import androidx.core.app.NotificationCompat;

import com.shanjingtech.secumchat.DebugActivity;
import com.shanjingtech.secumchat.R;

/**
 * Handles push notifications from Pushy.
 */
public class PushyNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationText = "New message";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }

        // Prepare a notification with vibration, sound and lights
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.cat_head_white)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.cat_head))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(notificationText)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context,
                        DebugActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (context.NOTIFICATION_SERVICE);

        // Build the notification and display it
        notificationManager.notify(1, builder.build());

    }
}
