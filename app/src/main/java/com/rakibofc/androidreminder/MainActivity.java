package com.rakibofc.androidreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public Button button5, button10, button15;
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button5 = findViewById(R.id.button5);
        button10 = findViewById(R.id.button10);
        button15 = findViewById(R.id.button15);

        scheduleNotification(getNotification("Notification from MainActivity"));
    }

    //schedule notification
    private void scheduleNotification(Notification notification){

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);

        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 5000, 6000, pendingIntent);
    }

    private Notification getNotification(String content){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_id);

        builder.setContentTitle("Schedule Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);

        return builder.build();
    }
}