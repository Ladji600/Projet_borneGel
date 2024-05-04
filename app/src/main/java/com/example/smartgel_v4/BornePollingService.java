package com.example.smartgel_v4;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BornePollingService extends Service {
    private static final String TAG = "BornePollingService";
    private static final String API_URL_BASE = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Alertes-Appli.php?id_etablissement=";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        // Récupère dynamiquement l'identifiant de l'établissement pour l'utilisateur actuel
      /*  int idEtablissementUtilisateur = getIdEtablissementUtilisateurActuel();

        public int getIdEtablissementUtilisateurActuel() {
            // Ici, tu devrais mettre le code pour récupérer dynamiquement l'identifiant de l'établissement
            // pour l'utilisateur actuel à partir de la source appropriée (base de données, préférences partagées, etc.)
            // Puis, retourne cet identifiant.
        }

        // Lance la tâche asynchrone de polling des bornes
        new BornePollingTask().execute(idEtablissementUtilisateur);*/

        return START_STICKY; // Indique que le service doit être redémarré s'il est arrêté de manière inattendue
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Retourne null car le service n'est pas lié à une activité
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        // Ajoute ici le code pour arrêter les tâches du service, s'il y en a
    }

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
