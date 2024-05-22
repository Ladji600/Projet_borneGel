package com.example.smartgel_v4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BornePollingJobServiceAPI extends JobService {

    private static final String CHANNEL_ID = "BornePollingChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d("BornePollingJobService", "Job started");

        // Récupérer l'ID de l'établissement depuis les préférences partagées
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.smartgel_v4.PREFERENCES", MODE_PRIVATE);
        int idEtablissement = -1; // Initialiser l'ID de l'établissement à une valeur par défaut

        // Vérifier d'abord la clé "Id_Etablissement"
        if (sharedPreferences.contains("Id_Etablissement")) {
            idEtablissement = sharedPreferences.getInt("Id_Etablissement", -1);
        } else {
            // Si la clé "Id_Etablissement" n'existe pas, vérifier la clé "IdEtablissement"
            if (sharedPreferences.contains("IdEtablissement")) {
                idEtablissement = sharedPreferences.getInt("IdEtablissement", -1);
            }
        }

        // Récupérer l'ID du rôle de l'utilisateur
        int idRole = sharedPreferences.getInt("Id_Role", -1);

        // Vérifier si l'utilisateur a un rôle différent de 1 (Agent)
        if (idRole != 1) {
            // Construire l'URL de la requête API avec l'ID de l'établissement
            String apiUrl = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Alertes-Appli.php?idEtablissement=" + idEtablissement;

            // Exécuter la requête API
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET, apiUrl, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            handleApiResponse(response);
                            jobFinished(params, false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("BornePollingJobService", "API request failed", error);
                            jobFinished(params, false);
                        }
                    }
            );

            queue.add(jsonArrayRequest);
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("BornePollingJobService", "Job stopped");
        return false;
    }

    private void handleApiResponse(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject borne = response.getJSONObject(i);
                int niveauGel = borne.getInt("Niveau_Gel");
                int niveauBatterie = borne.getInt("Niveau_Batterie");
                int idBorne = borne.getInt("IdBorne");
                String salle = borne.getString("Salle");

                // Vérifier si le niveau de batterie ou de gel est inférieur à 10%
                //    if (niveauGel < 10 || niveauBatterie < 10) {
                // Afficher la notification
                showNotification(idBorne, salle, niveauBatterie, niveauGel);
                //  }
            }
        } catch (JSONException e) {
            Log.e("BornePollingJobService", "Failed to parse JSON response", e);
        }
    }

    private void showNotification(int idBorne, String salle, int niveauBatterie, int niveauGel) {
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, AlertesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = "Borne ID " + idBorne + " dans la salle " + salle + " a un niveau de batterie de " + niveauBatterie + "% et un niveau de gel de " + niveauGel + "%.";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alerte Borne de Gel")
                .setContentText(message)
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
