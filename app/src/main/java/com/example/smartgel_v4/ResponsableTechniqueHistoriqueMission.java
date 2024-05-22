package com.example.smartgel_v4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResponsableTechniqueHistoriqueMission extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private int idEtablissementIntent;
    private String nomUser;
    private int idUser;
    private AffectationsAdapter mAffectationsAdapter;
    private List<MyAffectation> mAffectations;
    private TextView nomEtablissementText;
    private String nomEtablissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_affectations);

        // Récupérer l'établissement de l'utilisateur depuis l'Intent

        nomUser = getIntent().getStringExtra("Nom");
        idUser = getIntent().getIntExtra("IdEmployes", -1);
        idEtablissementIntent = getIntent().getIntExtra("IdEtablissement", -1);
        nomEtablissement = getIntent().getStringExtra("NomEtablissement");




        // Initialisation de la RecyclerView
        mRecyclerView = findViewById(R.id.recycle_view_affectation);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        nomEtablissementText = findViewById(R.id.text_TitleEtablissement);
        nomEtablissementText.setText(nomEtablissement);

        // Initialisation de la liste d'affectations
        mAffectations = new ArrayList<>();
        mAffectationsAdapter = new AffectationsAdapter(mAffectations);
        mRecyclerView.setAdapter(mAffectationsAdapter);

        ImageView imgRetour = findViewById(R.id.icon_back_historique);
// Ajouter un OnClickListener pour l'image de retour
        imgRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Récupérer l'établissement sélectionné par l'utilisateur
        if (idEtablissementIntent!= -1) {
            // Appel de la méthode pour récupérer les données des affectations depuis l'API
            fetchAffectationsData();
            Log.e("iduser", "ID user dans la condition" + idEtablissementIntent);
        } else {
            Toast.makeText(this, "Erreur lors de la récupération de l'établissement", Toast.LENGTH_SHORT).show();
            Log.e("Erreur", "ID user invalide");
        }

        // Gestion du retour en arrière lorsque l'image est cliquée
        findViewById(R.id.icon_back_historique).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Utilisation de la méthode onBackPressed() pour revenir en arrière
            }
        });
    }

    private void fetchAffectationsData() {
        // URL de l'API à interroger
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Historique-Mission-Appli.php?Id_Etablissement=" + idEtablissementIntent;
        Log.d("idUser", "Valeur de idUser dans le fetch : " + idEtablissementIntent);
        Log.d("API_Request", "URL de l'API : " + url);


        // Création de la requête JSON Array GET avec Volley
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("API_Response", "Réponse JSON : " + response.toString());
                            // Parcourir le tableau JSON des affectations
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject affectationObject = response.getJSONObject(i);

                                // Extraire les données de l'affectation
                                String idMissionStr = affectationObject.getString("IdMission");
                                int idMission = Integer.parseInt(idMissionStr);
                                String typemission = affectationObject.getString("Type");
                                String heure = affectationObject.getString("Heure");
                                String date = affectationObject.getString("Date");
                                String idEmployeStr = affectationObject.getString("Id_Employes");
                                int idEmploye = Integer.parseInt(idEmployeStr);
                                String idBorneStr = affectationObject.getString("Id_Borne");
                                String salle =  affectationObject.getString("Salle");
                                int idEtablissement = affectationObject.getInt("Id_Etablissement");
                                int idBorne = Integer.parseInt(idBorneStr);


                                Log.d("API_Data", "Id Mission : " + idMission);

                                // Création de l'objet MyAffectation
                                MyAffectation myAffectation = new MyAffectation(idMission, typemission, heure, date, idEmploye, idBorne, salle, idEtablissement);

                                // Ajout de l'affectation à la liste
                                mAffectations.add(myAffectation);
                            }

                            // Rafraîchissement de la RecyclerView
                            mAffectationsAdapter.notifyDataSetChanged();
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

    // Classe adaptateur pour RecyclerView
    private static class AffectationsAdapter extends RecyclerView.Adapter<AffectationsAdapter.AffectationsViewHolder> {
        private List<MyAffectation> mAffectations;

        public AffectationsAdapter(List<MyAffectation> affectations) {
            mAffectations = affectations;
        }

        @NonNull
        @Override
        public AffectationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historique_affectations, parent, false);
            return new AffectationsViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull AffectationsViewHolder holder, int position) {
            MyAffectation affectation = mAffectations.get(position);
            holder.bind(affectation);
        }

        @Override
        public int getItemCount() {
            return mAffectations.size();
        }

        public static class AffectationsViewHolder extends RecyclerView.ViewHolder {
            private TextView idBorneTextView;
            private TextView idMission;
            private TextView typemissionTextView;
            private TextView dateTextView;
            private TextView heureTextView;

            private TextView idEmploye;

            private TextView niveauGel;
            private TextView niveauBatterie;
            private TextView salle;

            public AffectationsViewHolder(View itemView) {
                super(itemView);
                idBorneTextView = itemView.findViewById(R.id.text_id_borne);
                typemissionTextView = itemView.findViewById(R.id.text_type_mission);
                dateTextView = itemView.findViewById(R.id.text_date);
                heureTextView = itemView.findViewById(R.id.text_heure);
                idEmploye = itemView.findViewById(R.id.text_id_employe);
                idMission = itemView.findViewById(R.id.text_id_mission);
                salle = itemView.findViewById(R.id.text_salle);
            }

            public void bind(MyAffectation affectation) {
                idBorneTextView.setText("Id Borne : " + affectation.getIdBorne());
                idMission.setText("Id Mission :"+ affectation.getIdMission());
                dateTextView.setText("Date : " + affectation.getDate());
                heureTextView.setText("Heure : " + affectation.getHeure());
                idEmploye.setText("Id Employé :" + affectation.getIdEmploye());
                typemissionTextView.setText("Mission :"+ affectation.getTypeMission());
                salle.setText("Salle : " + affectation.getSalle());

            }
        }
    }
}
