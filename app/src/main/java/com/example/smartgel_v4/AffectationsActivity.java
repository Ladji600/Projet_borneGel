package com.example.smartgel_v4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectationsActivity extends AppCompatActivity {

    // Variables ajoutées
    private RecyclerView mRecyclerView;
    private int userEtablissement;
    private AffectationsAdapter mAffectationsAdapter;
    private List<MyAffectation> mAffectations;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affectations);

        // Récupérer l'établissement de l'utilisateur depuis l'Intent
        userEtablissement = getIntent().getIntExtra("idEtablissement",-1);

        // Initialisation de la RecyclerView
        mRecyclerView = findViewById(R.id.recycle_view_affectation);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de la liste d'affectations
        mAffectations = new ArrayList<>();
        mAffectationsAdapter = new AffectationsAdapter(mAffectations);
        mRecyclerView.setAdapter(mAffectationsAdapter);

        // Récupérer l'établissement sélectionné par l'utilisateur
        if (userEtablissement != -1) {
            // Appel de la méthode pour récupérer les données des affectations depuis l'API
            fetchAffectationsData();
        } else {
            Toast.makeText(this, "Erreur lors de la récupération de l'établissement", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAffectationsData() {
        // URL de l'API à interroger
        String url = "https://c6976853-fd03-45cd-b519-bcd0d86b6d8c.mock.pstmn.io/connexion";
        url += "?idEtablissement=" + userEtablissement;

        // Création de la requête JSON Array GET avec Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parcourir le tableau JSON des affectations
                            JSONArray affectationsArray = response.getJSONArray("affectations");
                            for (int i = 0; i < affectationsArray.length(); i++) {
                                JSONObject affectationObject = affectationsArray.getJSONObject(i);

                                // Extraire les données de l'affectation
                                int idBorne = affectationObject.getInt("idBorne");
                                String salle = affectationObject.getString("salle");
                                int batterie = affectationObject.getInt("batterie");
                                int gel = affectationObject.getInt("gel");
                                String mission = affectationObject.getString("mission");
                                String date = affectationObject.getString("date");
                                String heure = affectationObject.getString("heure");
                                int idEmploye = affectationObject.getInt("idEmploye");

                                // Extraire les autres données nécessaires...

                                // Création de l'objet MyAffectation
                                MyAffectation myAffectation = new MyAffectation(idBorne, salle, batterie, gel, mission, date, heure, idEmploye);

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
    static class AffectationsAdapter extends RecyclerView.Adapter<AffectationsAdapter.AffectationsViewHolder> {
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

        public class AffectationsViewHolder extends RecyclerView.ViewHolder {
            private TextView idBorneTextView;
            private TextView salleTextView;
            private TextView batterieTextView;
            private TextView gelTextView;
            private TextView missionTextView;
            private TextView dateTextView;
            private TextView heureTextView;


            public AffectationsViewHolder(View itemView) {
                super(itemView);
                idBorneTextView = itemView.findViewById(R.id.text_id_borne);
                salleTextView = itemView.findViewById(R.id.text_salle);
                batterieTextView = itemView.findViewById(R.id.text_batterie);
                gelTextView =  itemView.findViewById(R.id.text_gel);
                missionTextView = itemView.findViewById(R.id.text_mission);
                dateTextView = itemView.findViewById(R.id.text_date);
                heureTextView = itemView.findViewById(R.id.text_heure);

            }

            public void bind(MyAffectation affectation) {
                idBorneTextView.setText("ID Affectation : " + affectation.getIdBorne());
                salleTextView.setText("ID Affectation : " + affectation.getSalle());
                batterieTextView.setText("ID Affectation : " + affectation.getBatterie());
                gelTextView.setText("ID Affectation : " + affectation.getGel());
                missionTextView.setText("ID Affectation : " + affectation.getMission());
                dateTextView.setText("ID Affectation : " + affectation.getDate());
                heureTextView.setText("ID Affectation : " + affectation.getHeure());

            }
        }
    }
}
