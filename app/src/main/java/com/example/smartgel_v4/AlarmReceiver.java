package com.example.smartgel_v4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Déclencher le travailleur ici
        WorkManager.getInstance(context).enqueue(new OneTimeWorkRequest.Builder(NotificationWorker.class).build());
    }
}
