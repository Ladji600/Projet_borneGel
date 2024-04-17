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
        idUser = getIntent().getIntExtra("idUser", -1);
        int idEtablissement = getIntent().getIntExtra("idEtablissement", -1);
        String userName = getIntent().getStringExtra("nom");
        String userFirstName = getIntent().getStringExtra("prenom");


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
        String url = "https://c6976853-fd03-45cd-b519-bcd0d86b6d8c.mock.pstmn.io/missionsHistorique";
        url += "?idEtablissement=" + idEtablissement;
        // Vous pouvez ajouter des paramètres supplémentaires à l'URL si nécessaire

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

                                // Vérifier si l'idEmploye correspond à l'idUser
                                int idEmploye = affectationObject.getInt("idEmploye");

                                if (idEmploye == idUser) {
                                    // Extraire les données de l'affectation
                                    int idBorne = affectationObject.getInt("idBorne");
                                    String salle = affectationObject.getString("salle");
                                    int batterie = affectationObject.getInt("batterie");
                                    int gel = affectationObject.getInt("gel");
                                    String mission = affectationObject.getString("mission");
                                    String date = affectationObject.getString("date");
                                    String heure = affectationObject.getString("heure");

                                    Log.d("API Response", "Response: " + response.toString());
                                    // Création de l'objet MyAffectation
                                    MyAffectation myAffectation = new MyAffectation(idBorne, salle, batterie, gel, mission, date, heure, idEmploye);

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
            private TextView salleTextView;
            private TextView batterieTextView;
            private TextView gelTextView;
            private TextView missionTextView;
            private TextView dateTextView;
            private TextView heureTextView;
            private TextView idEmploye;

            public AffectationsViewHolder(View itemView) {
                super(itemView);
                idBorneTextView = itemView.findViewById(R.id.text_id_borne);
                salleTextView = itemView.findViewById(R.id.text_salle);
                batterieTextView = itemView.findViewById(R.id.text_batterie);
                gelTextView = itemView.findViewById(R.id.text_gel);
                missionTextView = itemView.findViewById(R.id.text_mission);
                dateTextView = itemView.findViewById(R.id.text_date);
                heureTextView = itemView.findViewById(R.id.text_heure);
                idEmploye = itemView.findViewById(R.id.text_id_employe);

            }

            public void bind(MyAffectation affectation) {
                idBorneTextView.setText("Id Borne : " + affectation.getIdBorne());
                salleTextView.setText("Salle : " + affectation.getSalle());
                batterieTextView.setText("Niveau de batterie : " + affectation.getBatterie());
                gelTextView.setText("Niveau de gel : " + affectation.getGel());
                missionTextView.setText("Mission : " + affectation.getMission());
                dateTextView.setText("Date : " + affectation.getDate());
                heureTextView.setText("Heure : " + affectation.getHeure());
                idEmploye.setText("Id Employe :" + affectation.getIdEmploye());
            }
        }
    }
}
