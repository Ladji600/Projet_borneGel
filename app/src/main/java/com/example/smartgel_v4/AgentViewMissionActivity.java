package com.example.smartgel_v4;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentViewMissionActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<MyMissionView> mMissions;
    private MissionsAdapter mMissionsAdapter;
    private int idEmploye;
    private String userName;
    private String userFirstName;
    private TextView nomEtablissementText;
    private String nomEtablissement;
    private static final String API_URL = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Mission-statut-post-Appli.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_view_mission);

        // Récupération des données de l'utilisateur depuis l'intent
        userName = getIntent().getStringExtra("Nom");
        userFirstName = getIntent().getStringExtra("Prenom");
        idEmploye = getIntent().getIntExtra("IdEmployes", -1);
        nomEtablissement = getIntent().getStringExtra("NomEtablissement");

        // Mise à jour du TextView avec le nom complet de l'utilisateur
        TextView userNameTextView = findViewById(R.id.nomUtilisateurTextView);
        String fullName = userName + " " + userFirstName;
        userNameTextView.setText(fullName);

        nomEtablissementText = findViewById(R.id.etablissementTextView);
        nomEtablissementText.setText(nomEtablissement);

        ImageView imgRetour = findViewById(R.id.icon_back);
// Ajouter un OnClickListener pour l'image de retour
        imgRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialisation de la RecyclerView
        mRecyclerView = findViewById(R.id.recycle_view_mission);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de la liste des missions
        mMissions = new ArrayList<>();

        // Utilisation de l'ID de l'employé pour récupérer les missions depuis l'API
        fetchDataFromAPI(idEmploye);
    }

    // Méthode pour récupérer les données des missions depuis l'API
    private void fetchDataFromAPI(int idEmploye) {
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Visualisation-Appli.php?id_employe=" + idEmploye;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Parcourir les données de l'API et ajouter à la liste
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject missionObject = response.getJSONObject(i);

                                int idMission = missionObject.getInt("IdMission");
                                String type = missionObject.getString("Type");
                                String heure = missionObject.getString("Heure");
                                String date = missionObject.getString("Date");
                                int idUser = missionObject.getInt("Id_Employes");
                                int idBorne = missionObject.getInt("Id_Borne");
                                String salle = missionObject.getString("Salle");
                                String niveauGel = missionObject.getString("Niveau_Gel");
                                String niveauBatterie = missionObject.getString("Niveau_Batterie");
                                int idStatus = missionObject.getInt("IdStatus_Mission");

                                // Créer un objet MyMissionView
                                MyMissionView mission = new MyMissionView(idMission, type, heure, date,idUser, idBorne, salle, niveauGel, niveauBatterie, idStatus);
                                // Ajouter la mission à la liste
                                mMissions.add(mission);

                                Log.d("MissionID:", "mission id : " + idMission);
                                Log.d("Mission List", "Number of missions: " + mMissions.size());
                            }

                            // Initialisation de l'adaptateur avec les missions récupérées
                            mMissionsAdapter = new MissionsAdapter(mMissions);
                            mRecyclerView.setAdapter(mMissionsAdapter);
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
        Volley.newRequestQueue(this).add(request);
    }

    // Adapter pour les missions
    private class MissionsAdapter extends RecyclerView.Adapter<MissionsAdapter.MissionsViewHolder> {

        private List<MyMissionView> mMissions;

        public MissionsAdapter(List<MyMissionView> missions) {
            mMissions = missions;
        }

        @NonNull
        @Override
        public MissionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_vission_agent, parent, false);
            return new MissionsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MissionsViewHolder holder, int position) {
            MyMissionView mission = mMissions.get(position);
            holder.bind(mission);
        }

        @Override
        public int getItemCount() {
            return mMissions.size();
        }

        public class MissionsViewHolder extends RecyclerView.ViewHolder {
            private TextView idMissionTextView, typeTextView, heureTextView, dateTextView, idBorneTextView, salleTextView, batterieTextView, gelTextView;
            private Button buttonFinirMission;

            public MissionsViewHolder(View itemView) {
                super(itemView);
                // idMissionTextView = itemView.findViewById(R.id.text_id_mission);
                typeTextView = itemView.findViewById(R.id.text_type);
                heureTextView = itemView.findViewById(R.id.text_heure);
                dateTextView = itemView.findViewById(R.id.text_date);
                idBorneTextView = itemView.findViewById(R.id.text_id_borne);
                salleTextView = itemView.findViewById(R.id.text_salle);
                batterieTextView = itemView.findViewById(R.id.text_batterie);
                gelTextView = itemView.findViewById(R.id.text_gel);
                buttonFinirMission = itemView.findViewById(R.id.button_finir_mission);
            }

            public void bind(MyMissionView mission) {
                // idMissionTextView.setText("Id Mission : " + mission.getIdMission());
                typeTextView.setText("Type : " + mission.getType());
                heureTextView.setText("Heure : " + mission.getHeure());
                dateTextView.setText("Date : " + mission.getDate());
                idBorneTextView.setText("Id Borne : " + mission.getIdBorne());
                salleTextView.setText("Salle : " + mission.getSalle());
                batterieTextView.setText("Niveau de Batterie : " + mission.getNiveauBatterie());
                gelTextView.setText("Niveau de Gel : " + mission.getNiveauGel());


                // Définir le OnClickListener pour le bouton "Finir Mission"
                buttonFinirMission.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Afficher la boîte de dialogue de confirmation
                        showConfirmationDialog(getAdapterPosition());
                    }
                });
            }

            // Méthode pour afficher la boîte de dialogue de confirmation
            private void showConfirmationDialog(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("Voulez-vous vraiment finir la mission?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Récupérer l'ID de la mission à marquer comme terminée
                                int idStatus = mMissions.get(position).getIdStatus();

                                Log.d("idStatus", "Valeur de idStatus : " + idStatus);

                                // Envoyer une requête POST à l'API pour marquer la mission comme terminée
                                updateMissionStatus(idStatus);
                                // Supprimer la mission de la liste
                                mMissions.remove(position);
                                // Notifier l'adaptateur du changement
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Ne rien faire
                            }
                        });
                builder.create().show();
            }

            // Méthode pour envoyer une requête POST à l'API pour mettre à jour le statut de la mission
            private void updateMissionStatus(final int idStatus) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Traitement de la réponse de l'API si nécessaire
                                // Vous pouvez afficher un message de succès ou d'échec ici

                                if (response != null && !response.isEmpty()) {
                                    Log.d("Mission Update", "Réponse de l'API : " + response);
                                } else {
                                    Log.d("Mission Update", "Réponse de l'API vide ou nulle");
                                }
                                Log.d("Mission Update", "Réponse de l'API : " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Gestion des erreurs de connexion
                                Toast.makeText(AgentViewMissionActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Paramètres de la requête POST
                        Map<String, String> params = new HashMap<>();
                        params.put("idStatus", String.valueOf(idStatus));
                        return params;
                    }
                };

                // Ajout de la requête à la file d'attente
                RequestQueue requestQueue = Volley.newRequestQueue(AgentViewMissionActivity.this);
                requestQueue.add(stringRequest);
            }

        }}
}
