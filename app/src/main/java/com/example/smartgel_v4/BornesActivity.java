package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BornesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bornes);

        fetchBornesData();

    }
    private void fetchBornesData() {
        // URL de l'API à interroger
        String url = "https://c2a10eed-4b23-4f87-b01a-2596e8315607.mock.pstmn.io/dahsboard?etablissement=JeanRostand";

        // Création de la requête JSON Object GET avec Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract the "Bornes" array from the response object
                            JSONArray bornesArray = response.getJSONArray("Bornes");

                            // Parcourir le tableau JSON des bornes
                            for (int i = 0; i < bornesArray.length(); i++) {
                                JSONObject borne = bornesArray.getJSONObject(i);

                                int idBorne = borne.getInt("idBorne");
                                int batterie = borne.getInt("batterie");
                                int gel = borne.getInt("gel");
                                String heure = borne.getString("Heure");
                                String date = borne.getString("Date");

                                // Afficher les informations de chaque borne
                                Log.d("Borne " + i, "Id Borne: " + idBorne +
                                        ", Batterie: " + batterie +
                                        ", Gel: " + gel +
                                        ", Heure: " + heure +
                                        ", Date: " + date);
                            }
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