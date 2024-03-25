package com.example.smartgel_v4;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class OneBorneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_borne);

        // Appelle la méthode pour récupérer les données des bornes
        fetchBorneData();
    }

    private void fetchBorneData() {
        // URL de l'API à interroger
        String url = "https://c2a10eed-4b23-4f87-b01a-2596e8315607.mock.pstmn.io/JeanRostand?numeroESP=1";

        // Création de la requête JSON Object GET avec Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int idBorne = response.getInt("idBorne");
                            int batterie = response.getInt("Batterie");
                            int gel = response.getInt("Gel");
                            String heure = response.getString("Heure");
                            String date = response.getString("Date");

/*
                            Log.d("idBorne", String.valueOf(idBorne));
                            Log.d("batterie", String.valueOf(batterie));
                            Log.d("gel", String.valueOf(gel));
                            Log.d("heure", heure);
                            Log.d("date", date);

*/
                            // Mettre à jour les TextViews dans l'interface utilisateur avec les données récupérées
                            TextView idBorneTextView = findViewById(R.id.TextViewIdBorne);
                            TextView batterieTextView = findViewById(R.id.TextViewBatterie);
                            TextView gelTextView = findViewById(R.id.TextViewGel);
                            TextView heureTextView = findViewById(R.id.TextViewHeure);
                            TextView dateTextView = findViewById(R.id.TextViewDate);





                            idBorneTextView.setText("Id Borne: " + idBorne);
                            batterieTextView.setText("Batterie: " + batterie);
                            gelTextView.setText("Gel: " + gel);
                            heureTextView.setText("Heure :"+heure);
                            dateTextView.setText("Date : "+date);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log des erreurs de connexion dans le logcat
                        Log.e("API Error", "Erreur : " + error.getMessage(), error);
                    }
                });

        // Ajouter la requête à la file d'attente de Volley
        Volley.newRequestQueue(this).add(request);
    }
}
