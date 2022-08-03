package com.rakibofc.androidreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public TextView textView;
    public String time;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());

        Date date = new Date();
        long dateInMillis = date.getTime();

        dateInMillis += 5000;

        time = simpleDateFormat.format(dateInMillis);
        textView.setText(time);

        /*
        try {
            scheduleNotification();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */

        Intent serviceIntent = new Intent(getApplicationContext(), SetReminderService.class);
        serviceIntent.putExtra("time", time);
        startService(serviceIntent);
    }
}