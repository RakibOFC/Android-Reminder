package com.rakibofc.androidreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public TextView textView;
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";
    public String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        time = "10:34 pm";

        try {
            scheduleNotification(getNotification());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("HOUR_OF_DAY", "Catch");
        }
    }

    //schedule notification
    @SuppressLint({"SetTextI18n", "NewApi"})
    private void scheduleNotification(Notification notification) throws ParseException {

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);

        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Date date = simpleDateFormat.parse(time);

        assert date != null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        Calendar calendarTime = Calendar.getInstance();

        calendarTime.set(Calendar.HOUR_OF_DAY, hour);
        calendarTime.set(Calendar.MINUTE, min);
        calendarTime.set(Calendar.SECOND, 45);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarTime.getTimeInMillis(), 5000, pendingIntent);

        textView.setText(hour + ":" + min);
    }

    private Notification getNotification(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_id);
        builder.setContentTitle("Schedule Notification");
        builder.setContentText("Notification from MainActivity");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);

        return builder.build();
    }
}