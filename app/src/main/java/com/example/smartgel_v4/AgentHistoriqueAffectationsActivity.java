package com.example.smartgel_v4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgentHistoriqueAffectationsActivity extends AppCompatActivity {

    // Variables ajoutées
    private RecyclerView mRecyclerView;

    private int idUser;

    private int idEtablissement;
    private AffectationsAdapter mAffectationsAdapter;
    private List<MyAffectation> mAffectations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_affectations);

        // Récupérer l'id de l'utilisateur depuis l'Intent
        idUser = getIntent().getIntExtra("IdEmployes", -1);
        int idEtablissement = getIntent().getIntExtra("Id_Etablissement", -1);
        String userName = getIntent().getStringExtra("Nom");
        String userFirstName = getIntent().getStringExtra("Prenom");


        Log.d("idUser", "Id de l'utilisateur : " + idUser);
        // Initialisation de la RecyclerView
        mRecyclerView = findViewById(R.id.recycle_view_affectation);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de la liste d'affectations
        mAffectations = new ArrayList<>();
        mAffectationsAdapter = new AffectationsAdapter(mAffectations);
        mRecyclerView.setAdapter(mAffectationsAdapter);

        // Vérifier si idUser est null
        if (idUser != -1) {
            // Appel de la méthode pour récupérer les données des affectations depuis l'API
            fetchAffectationsData();

            // Gestion du retour en arrière lorsque l'image est cliquée
            findViewById(R.id.icon_back_historique).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Utilisation de la méthode onBackPressed() pour revenir en arrière
                }
            });
        } else {
            // Afficher un message d'erreur si idUser est null
            Toast.makeText(this, "ID de l'utilisateur non disponible", Toast.LENGTH_SHORT).show();
            Log.e("Erreur", "ID de l'utilisateur non disponible");
        }
    }

    private void fetchAffectationsData() {
        // URL de l'API à interroger
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Historique-Mission-Appli.php";
        url += "?id_employes=" + idEtablissement;

        // Création de la requête JSON Array GET avec Volley
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Parcourir le tableau JSON des affectations
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject affectationObject = response.getJSONObject(i);

                                // Extraire les données de l'affectation
                                int idMission = affectationObject.getInt("IdMission");
                                String typemission = affectationObject.getString("Type");
                                String heure = affectationObject.getString("Heure");
                                String date = affectationObject.getString("Date");
                                int idEmploye = affectationObject.getInt("Id_Employes");
                                int idBorne = affectationObject.getInt("Id_Borne");

                                // Vérifier si l'idEmploye correspond à l'idUser
                                if (idEmploye == idUser) {
                                    // Création de l'objet MyAffectation
                                    MyAffectation myAffectation = new MyAffectation(idMission, typemission, heure, date, idEmploye, idBorne);

                                    // Ajout de l'affectation à la liste
                                    mAffectations.add(myAffectation);
                                }
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

            public AffectationsViewHolder(View itemView) {
                super(itemView);
                idBorneTextView = itemView.findViewById(R.id.text_id_borne);
                typemissionTextView = itemView.findViewById(R.id.text_type_mission);
                dateTextView = itemView.findViewById(R.id.text_date);
                heureTextView = itemView.findViewById(R.id.text_heure);
                idEmploye = itemView.findViewById(R.id.text_id_employe);
                idMission = itemView.findViewById(R.id.text_id_mission);
            }

            public void bind(MyAffectation affectation) {
                idBorneTextView.setText("Id Borne : " + affectation.getIdBorne());
                idMission.setText("Id Mission :"+ affectation.getIdMission());
                dateTextView.setText("Date : " + affectation.getDate());
                heureTextView.setText("Heure : " + affectation.getHeure());
                idEmploye.setText("Id Employé :" + affectation.getIdEmploye());
                typemissionTextView.setText("Mission :"+ affectation.getTypeMission());
            }
        }
    }
}
