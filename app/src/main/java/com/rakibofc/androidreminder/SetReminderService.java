package com.rakibofc.androidreminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SetReminderService extends Service {

    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1001, notifyApi26OrOlder("Tap to show prayer time", "Tap to show prayer time", "It helps to send reminder and other services"));
        } else {
            startForeground(1001, notifyApi25OrLower("Tap to show prayer time", "It helps to send reminder and other services"));
        }

        try {
            scheduleNotification();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    //schedule notification
    private void scheduleNotification() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendarTime = Calendar.getInstance();

        calendarTime.set(Calendar.HOUR_OF_DAY, 22);
        calendarTime.set(Calendar.MINUTE, 30);
        calendarTime.set(Calendar.SECOND, 10);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarTime.getTimeInMillis(), getPendingIntent(getNotification()));
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarTime.getTimeInMillis(), getPendingIntent(getNotification()));
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent getPendingIntent(Notification notification) {

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);

        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return PendingIntent.getBroadcast(this, 195, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            return PendingIntent.getBroadcast(this, 195, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    private Notification getNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_id);
        builder.setContentTitle("Android Reminder");
        builder.setContentText("This notification from BroadcastReceiver");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);

        return builder.build();
    }

    // Push Notification for Api 25 Or Lower
    private Notification notifyApi25OrLower(String title, String text) {

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(false)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentIntent(pendingIntent);

        return notificationBuilder.build();
    }

    // Push Notification for Api 25 Or Higher
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification notifyApi26OrOlder(String channelId, String title, String text) {

        NotificationChannel channel = new NotificationChannel(
                channelId,
                channelId,
                NotificationManager.IMPORTANCE_LOW
        );
        channel.setShowBadge(false);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Notification.Builder notificationBuilder = new Notification.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(false)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentIntent(pendingIntent);

        return notificationBuilder.build();
    }
}