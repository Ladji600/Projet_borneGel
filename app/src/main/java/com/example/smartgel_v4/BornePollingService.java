package com.example.smartgel_v4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BornePollingService extends BroadcastReceiver {
    private static final String TAG = "BornePollingService";
    private static final String API_URL_BASE = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Alertes-Appli.php?id_etablissement=";
    private static final long INTERVAL = AlarmManager.INTERVAL_HALF_HOUR; // Interval de 30 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Service started");

        // Récupère dynamiquement le rôle de l'utilisateur actuel
        int roleUtilisateur = getRoleUtilisateurActuel(context);

        // Si le rôle de l'utilisateur est 2 (responsable), lancez la tâche asynchrone de polling des bornes
        if (roleUtilisateur == 2) {
            int idEtablissementUtilisateur = getIdEtablissementUtilisateurActuel(context);
            new BornePollingTask().execute(idEtablissementUtilisateur);
        }
    }

    // Méthode pour récupérer dynamiquement l'identifiant de l'établissement pour l'utilisateur actuel
    private int getIdEtablissementUtilisateurActuel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.smartgel_v4.PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Id_Etablissement", -1); // -1 est une valeur par défaut si l'identifiant n'est pas trouvé
    }

    // Méthode pour récupérer dynamiquement le rôle de l'utilisateur actuel
    private int getRoleUtilisateurActuel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.smartgel_v4.PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Id_Role", -1); // -1 est une valeur par défaut si le rôle n'est pas trouvé
    }

    // Classe interne pour effectuer la tâche asynchrone de polling des bornes
    private static class BornePollingTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            int idEtablissement = params[0];
            try {
                String apiUrl = API_URL_BASE + idEtablissement;
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    reader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Traitement des données reçues de l'API
                Log.d("API Response", result);
                // Ajoute ici le code pour traiter les données et afficher une notification si nécessaire
            } else {
                // Gérer les erreurs ou les cas où aucune donnée n'est renvoyée
                Log.e("API Error", "Erreur lors de la récupération des données de l'API");
            }
        }
    }
}
