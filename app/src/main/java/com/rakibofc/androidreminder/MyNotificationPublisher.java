package com.rakibofc.androidreminder;

import static android.content.Context.MODE_PRIVATE;

import static com.rakibofc.androidreminder.SetReminderService.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class MyNotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        assert notificationManager != null;
        notificationManager.notify(id, notification);

        Log.e("Info", "This is from MyNotificationPublisher BroadcastReceiver");

        Intent serviceIntent = new Intent(context, MyService.class);

        context.startService(serviceIntent);
    }
}
