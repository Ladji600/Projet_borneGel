package com.example.smartgel_v4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlertesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<MyAlerte> mAlertes;
    private AlertesAdapter mAlertesAdapter;
    private int idEtablissement;
    private String userName;
    private int idUser;
    private String userFirstName;
    private String userEmail;
  //  private String nomEtablissement;
  private String etablissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertes);

        // Récupération des données de l'utilisateur depuis l'intent
        userName = getIntent().getStringExtra("nom");
        idUser = getIntent().getIntExtra("idUser", -1);
        userFirstName = getIntent().getStringExtra("prenom");
        userEmail = getIntent().getStringExtra("email");

        // Mise à jour du TextView avec le nom complet de l'utilisateur
        TextView userNameTextView = findViewById(R.id.nomUtilisateurTextView);
        String fullName = userName + " " + userFirstName;
        userNameTextView.setText(fullName);

        // Initialisation de la RecyclerView
        mRecyclerView = findViewById(R.id.recycle_view_alerte);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupération de l'ID de l'établissement depuis l'intent
        idEtablissement = getIntent().getIntExtra("idEtablissement", -1);
        if (idEtablissement != -1) {
            // Utilisation de l'ID de l'établissement pour récupérer les données des alertes depuis l'API
            fetchDataFromAPI(idEtablissement);
            // Appel de la méthode pour récupérer le nom de l'établissement
            fetchEtablissementName(idEtablissement);
        } else {
            // Gérer le cas où l'ID de l'établissement n'est pas disponible
        }
    }

    // Méthode pour récupérer le nom de l'établissement depuis l'API
    private void fetchEtablissementName(int idEtablissement) {
        String url = "https://804b3669-1a04-43a0-8a07-09a076ab6c78.mock.pstmn.io/dashboard?idEtablissement=" + idEtablissement;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           etablissement = response.getString("Etablissement");
                            // Mettre à jour le TextView avec le nom de l'établissement
                            TextView etablissementTextView = findViewById(R.id.etablissementTextView);
                            etablissementTextView.setText(etablissement);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Erreur : " + error.getMessage(), error);
                    }
                });

        // Ajouter la requête à la file d'attente de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    // Méthode pour récupérer les données des alertes depuis l'API
    private void fetchDataFromAPI(int idEtablissement) {
        String url = "https://804b3669-1a04-43a0-8a07-09a076ab6c78.mock.pstmn.io/alerts?idEtablissement=" + idEtablissement;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray alertesArray = response.getJSONArray("alertes");
                            mAlertes = new ArrayList<>();
                            // Parcourir les données de l'API et ajouter à la liste
                            for (int i = 0; i < alertesArray.length(); i++) {
                                JSONObject alerteObject = alertesArray.getJSONObject(i);
                                int idBorne = alerteObject.getInt("idBorne");
                                String salle = alerteObject.getString("salle");
                                int batterie = alerteObject.getInt("batterie");
                                int gel = alerteObject.getInt("gel");
                                String date = alerteObject.getString("date");
                                String heure = alerteObject.getString("heure");
                                // Créer un objet MyAlerte
                                MyAlerte alerte = new MyAlerte(idBorne, salle, batterie, gel, date, heure);
                                // Ajouter l'alerte à la liste
                                mAlertes.add(alerte);
                            }
                            mAlertesAdapter = new AlertesAdapter(mAlertes);
                            mRecyclerView.setAdapter(mAlertesAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Erreur : " + error.getMessage(), error);
                    }
                });

        // Ajouter la requête à la file d'attente de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    // Adapter pour Alertes
    private class AlertesAdapter extends RecyclerView.Adapter<AlertesAdapter.AlertesViewHolder> {

        private List<MyAlerte> myAlertes;

        public AlertesAdapter(List<MyAlerte> alertes) {
            myAlertes = alertes;
        }

        @NonNull
        @Override
        public AlertesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alertes, parent, false);
            return new AlertesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlertesViewHolder holder, int position) {
            MyAlerte alerte = myAlertes.get(position);
            holder.bind(alerte);
        }

        @Override
        public int getItemCount() {
            return myAlertes.size();
        }

        public class AlertesViewHolder extends RecyclerView.ViewHolder {
            private TextView idBorneTextView, batterieTextView, gelTextView, heureTextView, dateTextView, salleTextView;
            private Button btnAffectation;

            public AlertesViewHolder(View itemView) {
                super(itemView);
                idBorneTextView = itemView.findViewById(R.id.text_id_borne);
                batterieTextView = itemView.findViewById(R.id.text_batterie);
                salleTextView = itemView.findViewById(R.id.text_salle);
                gelTextView = itemView.findViewById(R.id.text_gel);
                heureTextView = itemView.findViewById(R.id.text_heure);
                dateTextView = itemView.findViewById(R.id.text_date);
                btnAffectation = itemView.findViewById(R.id.button_appeler_affectation);
            }

            public void bind(MyAlerte alerte) {
                idBorneTextView.setText("id Borne : " + alerte.getIdBorne());
                batterieTextView.setText("Niveau de batterie : " + alerte.getBatterie());
                salleTextView.setText("Salle : " + alerte.getSalle());
                gelTextView.setText("Niveau de gel : " + alerte.getGel());
                heureTextView.setText("Heure : " + alerte.getHeure());
                dateTextView.setText("Date : " + alerte.getDate());

                // Définir le click listener pour le bouton dans onBindViewHolder()
                btnAffectation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Création de l'intent pour passer à DoMissionActivity
                        Intent intent = new Intent(AlertesActivity.this, DoMissionActivity.class);

                        // Ajout des données à passer à DoMissionActivity
                        intent.putExtra("idUser", idUser);
                        intent.putExtra("email", userEmail);
                        intent.putExtra("nom", userName);
                        intent.putExtra("prenom", userFirstName);
                        intent.putExtra("idEtablissement", idEtablissement);
                        intent.putExtra("idBorne", alerte.getIdBorne());
                        intent.putExtra("gel", alerte.getGel());
                        intent.putExtra("batterie", alerte.getBatterie());
                        intent.putExtra("salle", alerte.getSalle());
                        intent.putExtra("heure", alerte.getHeure());
                        intent.putExtra("date", alerte.getDate());
                        intent.putExtra("Etablissement",etablissement );
                        startActivity(intent); // Démarrage de DoMissionActivity
                    }
                });
            }
        }
    }
}
