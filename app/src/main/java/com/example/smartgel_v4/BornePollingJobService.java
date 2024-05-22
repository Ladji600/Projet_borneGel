package com.example.smartgel_v4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class BornePollingJobService extends JobService {

    private static final String CHANNEL_ID = "BornePollingChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d("BornePollingJobService", "Job started");
        // Code pour exécuter votre tâche
        showNotification();
        // Indiquer que le travail est terminé
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("BornePollingJobService", "Job stopped");
        // Nettoyer les tâches si nécessaire
        return false;
    }

    private void showNotification() {
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, AlertesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notification automatique")
                .setContentText("Ceci est une notification automatique générée toutes les 15 minutes.")
                .setSmallIcon(R.drawable.icon_notification1)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
        Log.d("BornePollingJobService", "Notification shown");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "BornePollingChannel";
            String description = "Channel for BornePolling notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d("BornePollingJobService", "Notification channel created");
        }
    }
}
