package com.example.smartgel_v4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RechercheActivity extends AppCompatActivity {

    EditText editTextSearch;
    Button buttonSearch;
    ListView listViewResults;
    ArrayAdapter<String> adapter;
    ArrayList<String> resultsList;
    ArrayList<JSONObject> allBornes;
    int idEtablissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        listViewResults = findViewById(R.id.listViewResults);

        resultsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultsList);
        listViewResults.setAdapter(adapter);

        // Récupérer l'ID de l'établissement
        idEtablissement = getIntent().getIntExtra("Id_Etablissement", -1);
        if (idEtablissement == -1) {
            idEtablissement = getIntent().getIntExtra("IdEtablissement", -1);
        }


        // Fetch all bornes on startup
        fetchAllBornes();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchBornes(query);
                } else {
                    Toast.makeText(RechercheActivity.this, "Veuillez entrer un IdBorne ou une Salle", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchAllBornes() {
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Tous_Bornes_Recherche_API.php?id_etablissement="+idEtablissement;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        allBornes = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                allBornes.add(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("API_RESPONSE", "Toutes les bornes récupérées: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RechercheActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        Log.d("API_ERROR", error.getMessage());
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void searchBornes(String query) {
        resultsList.clear();

        for (JSONObject borne : allBornes) {
            try {
                String idBorne = borne.getString("IdBorne");
                String salle = borne.getString("Salle");

                if (idBorne.equalsIgnoreCase(query) || salle.equalsIgnoreCase(query)) {
                    resultsList.add("IdBorne: " + idBorne + "\nSalle: " + salle + "\nNiveau Gel: " + borne.getString("Niveau_Gel") + "\nNiveau Batterie: " + borne.getString("Niveau_Batterie"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (resultsList.isEmpty()) {
            resultsList.add("Aucun résultat trouvé.");
        }

        adapter.notifyDataSetChanged();
    }
}
