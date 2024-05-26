package com.example.smartgel_v4;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    private String nomEtablissement;

  private TextView nomEtablissementText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertes);

        // Récupération des données de l'utilisateur depuis l'intent
        userName = getIntent().getStringExtra("Nom");
        idUser = getIntent().getIntExtra("IdEmployes", -1);
        userFirstName = getIntent().getStringExtra("Prenom");
        userEmail = getIntent().getStringExtra("Email");
        nomEtablissement =getIntent().getStringExtra("NomEtablissement");
        //idEtablissement = getIntent().getIntExtra("EXTRA_ID_ETABLISSEMENT", -1);

      // Récupération de l'ID de l'établissement depuis les préférences partagées
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.smartgel_v4.PREFERENCES", Context.MODE_PRIVATE);
     /*   if (isResponsableAgentOrTechnique()) {
            idEtablissement = sharedPreferences.getInt("Id_Etablissement", -1);
        } else {
            idEtablissement = sharedPreferences.getInt("IdEtablissement", -1);
        }

        // Gestion de l'ID de l'établissement si non trouvé
        if (idEtablissement == -1) {
            Log.e(TAG, "Identifiant d'établissement non trouvé dans les préférences partagées.");
            // Traitez ce cas selon vos besoins, par exemple, affichez un message d'erreur ou terminez l'activité.
            finish();
            return;
        }*/

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
        // Mise à jour du TextView avec le nom complet de l'utilisateur
        TextView userNameTextView = findViewById(R.id.nomUtilisateurTextView);
        String fullName = userName + " " + userFirstName;
        userNameTextView.setText(fullName);

        // Initialisation de la RecyclerView
        mRecyclerView = findViewById(R.id.recycle_view_alerte);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dans la méthode onCreate() après la création de la RecyclerView
        mAlertes = new ArrayList<>();

        // Récupération de l'ID de l'établissement depuis l'intent
        idEtablissement = getIntent().getIntExtra("Id_Etablissement", -1);
        if (idEtablissement != -1) {
            // Utilisation de l'ID de l'établissement pour récupérer les données des alertes depuis l'API
            fetchDataFromAPI(idEtablissement);
            // Appel de la méthode pour récupérer le nom de l'établissement
        } else {
            // Gérer le cas où l'ID de l'établissement n'est pas disponible
        }
    }

    // Méthode pour récupérer le nom de l'établissement depuis l'API


    // Méthode pour récupérer les données des alertes depuis l'API
    private void fetchDataFromAPI(int idEtablissement) {
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Alertes-Appli.php?id_etablissement=" + idEtablissement;

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
                                JSONObject alerteObject = response.getJSONObject(i);

                                int idBorne = alerteObject.getInt("IdBorne");
                                int gel = alerteObject.getInt("Niveau_Gel");
                                int batterie = alerteObject.getInt("Niveau_Batterie");
                                String heure = alerteObject.getString("Heure");
                                String salle = alerteObject.getString("Salle");
                                String date = alerteObject.getString("Date");
                                int idEta = alerteObject.getInt("Id_Etablissement");
                                // Créer un objet MyAlerte
                                MyAlerte alerte = new MyAlerte(idBorne, gel, batterie, salle, heure, date, idEta);
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
        Volley.newRequestQueue(this).add(request);
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
                        intent.putExtra("IdEmployes", idUser);
                        intent.putExtra("Mail", userEmail);
                        intent.putExtra("Nom", userName);
                        intent.putExtra("Prenom", userFirstName);
                        intent.putExtra("Id_Etablissement", idEtablissement);
                        intent.putExtra("IdBorne", alerte.getIdBorne());
                        intent.putExtra("Niveau_Gel", alerte.getGel());
                        intent.putExtra("Niveau_Batterie", alerte.getBatterie());
                        intent.putExtra("Salle", alerte.getSalle());
                        intent.putExtra("Heure", alerte.getHeure());
                        intent.putExtra("Date", alerte.getDate());
                        intent.putExtra("NomEtablissement",nomEtablissement );
                        startActivity(intent); // Démarrage de DoMissionActivity
                    }
                });
            }
        }
    }
  /*  private boolean isResponsableAgentOrTechnique() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.smartgel_v4.PREFERENCES", Context.MODE_PRIVATE);
        int roleId = sharedPreferences.getInt("Id_Role", -1);
        // Vérifie si c'est un Responsable Agent (ID de rôle 2) ou un Responsable Technique (ID de rôle 3)
        return roleId == 2 || roleId == 3;
    }*/
}
