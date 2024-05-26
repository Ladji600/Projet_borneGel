package com.example.smartgel_v4;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;


public class NotificationWorker extends Worker {

    private static final String TAG = "NotificationWorker";
    private int idEtablissement; // Déclaration de la variable idEtablissement en tant que variable de classe


    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Logique pour générer la notification
        Log.d(TAG, "Notification start");

        // Faites l'appel à l'API ici
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.smartgel_v4.PREFERENCES", Context.MODE_PRIVATE);
        int idEtablissement = sharedPreferences.getInt("Id_Etablissement", -1);

        if (idEtablissement == -1) {
            // Gérer le cas où l'identifiant d'établissement n'est pas trouvé
            Log.e(TAG, "Identifiant d'établissement non trouvé dans les préférences partagées.");
            return Result.failure();
        }

        if (!isResponsableAgent()) {
            Log.d(TAG, "L'utilisateur n'est pas un Responsable Agent.");
            return Result.success(); // On arrête le travail ici si ce n'est pas un responsable agent
        }


        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Alertes-Appli.php?id_etablissement=" + idEtablissement;

        Log.d(TAG, "URL de l'API : " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Gérez la réponse JSON ici
                        handleApiResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérez les erreurs ici
                        Log.e(TAG, "Erreur lors de la récupération des données de l'API: " + error.getMessage());
                    }
                });

        // Ajoutez la requête à la file d'attente de Volley
        Volley.newRequestQueue(getApplicationContext()).add(request);

        // Logique pour afficher la notification
        // NotificationUtils.showNotification(getApplicationContext()); // Commentez cette ligne, car nous allons afficher la notification après avoir traité la réponse de l'API
        Log.d(TAG, "Notification sent");
        return Result.success();
    }

    private boolean isResponsableAgent() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.smartgel_v4.PREFERENCES", Context.MODE_PRIVATE);
        int roleId = sharedPreferences.getInt("Id_Role", -1);
        // Vérifie si c'est un Responsable Agent (ID de rôle 2)
        return roleId == 2;
    }


    private void handleApiResponse(JSONArray response) {
        Log.d(TAG, "Réponse de l'API : " + response.toString());

        // Parcourir la réponse JSON et traiter chaque objet
        for (int i = 0; i < response.length(); i++) {
            try {
                // Extraire les données de chaque objet JSON
                JSONObject jsonObject = response.getJSONObject(i);
                String idBorne = jsonObject.getString("IdBorne");
                String niveauGel = jsonObject.getString("Niveau_Gel");
                String niveauBatterie = jsonObject.getString("Niveau_Batterie");
                String salle = jsonObject.getString("Salle");

                // Construction du contenu de la notification
                String notificationContent = "Borne : " + idBorne + "\nNiveau de gel : " + niveauGel + "\nNiveau de batterie : " + niveauBatterie + "\nSalle : " + salle;

                // Création de l'intent pour ouvrir AlertesActivity lorsque la notification est cliquée
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                // Création d'un PendingIntent pour ouvrir l'activité lors du clic sur la notification
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Affichage de la notification
                NotificationUtils.showNotification(getApplicationContext(), "Alerte nivel faible de batterie ou gel ", notificationContent, pendingIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
