package com.example.smartgel_v4;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.smartgel_v4.NotificationActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BornePollingJobService extends JobIntentService {
    private static final String TAG = "BornePollingJobService";
    private static final String CHANNEL_ID = "BornePollingJobServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    protected void onHandleWork(Intent intent) {
        Log.d(TAG, "Job started");

        // Afficher un toast pour indiquer que le job a démarré
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(this, "Job started", Toast.LENGTH_SHORT).show());

        // Créer une notification pour indiquer que le job a démarré
        createNotification(this, "Job started", "Le job a démarré à " + System.currentTimeMillis());

        int roleUtilisateur = getRoleUtilisateurActuel(this);
        Log.v(TAG, "Current user role: " + roleUtilisateur);

        if (roleUtilisateur == 2 || roleUtilisateur == 3) {
            int idEtablissementUtilisateur = getIdEtablissementUtilisateurActuel(this);
            String apiUrl = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Alertes-Appli.php?id_etablissement=" + idEtablissementUtilisateur;
            new BornePollingTask(this).execute(apiUrl);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Job destroyed");
    }

    private int getIdEtablissementUtilisateurActuel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.smartgel_v4.PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Id_Etablissement", -1);
    }

    private int getRoleUtilisateurActuel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.smartgel_v4.PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Id_Role", -1);
    }

    private static void createNotification(Context context, String title, String message) {
        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.icon_notification1)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Vérifier si l'application a la permission d'écrire des notifications
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            NotificationManagerCompat.from(context).notify(0, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "BornePollingJobServiceChannel";
            String description = "Channel for Borne Polling Job Service";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static class BornePollingTask extends AsyncTask<String, Void, String> {
        private Context mContext;

        public BornePollingTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... urls) {
            String apiUrl = urls[0];
            Log.d(TAG, "Requesting URL: " + apiUrl);
            // Reste de votre code...

            // Ajouter des logs pour tester les messages
            Log.d(TAG, "Mila ceci est le code de l'envoie d'alerte - Dally");

            // Envoyer une notification pour vérifier si le timer fonctionne
            createNotification(mContext, "Message de test", "Message pour savoir si le timer fonctionne");

            return null;
        }
    }
}
