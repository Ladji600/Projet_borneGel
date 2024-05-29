package com.example.smartgel_v4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class AlarmScheduler {

    public static void scheduleRepeatingAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Définir l'alarme pour se déclencher toutes les 5 minutes
        long intervalMillis = 5 * 60 * 1000;
       // long intervalMillis = 2 * 60 * 1000;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), intervalMillis, pendingIntent);
    }
}
