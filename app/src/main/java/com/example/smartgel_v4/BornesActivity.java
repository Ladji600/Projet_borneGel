package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class BornesActivity extends AppCompatActivity {

    // Variables ajoutées
    private RecyclerView mRecyclerView;
    private BorneAdapter mBorneAdapter;
    private List<MyBorne> mBornes;
    private int idEtablissement;

    private TextView nomEtablissementText;
    private String nomEtablissement;

    //private String nameEtablissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bornes);

        // Initialisation de la RecyclerView
        mRecyclerView = findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Initialisation de la liste des bornes
        mBornes = new ArrayList<>();
        mBorneAdapter = new BorneAdapter(mBornes);
        mRecyclerView.setAdapter(mBorneAdapter);

        String userName = getIntent().getStringExtra("Nom");
        String userFirstName = getIntent().getStringExtra("Prenom");
        String nomEtablissement =getIntent().getStringExtra("NomEtablissement");

        nomEtablissementText = findViewById(R.id.etablissementTittleTextView);
        nomEtablissementText.setText(nomEtablissement);

        idEtablissement = -1; // Initialisation à une valeur par défaut
        if (getIntent().hasExtra("IdEtablissement")) {
            // Si l'intent contient l'extra avec la clé "idEtablissement"
            idEtablissement = getIntent().getIntExtra("IdEtablissement", -1);
        } else if (getIntent().hasExtra("Id_Etablissement")) {
            // Si l'intent contient l'extra avec la clé "Id_Etablissement"
            idEtablissement = getIntent().getIntExtra("Id_Etablissement", -1);
        }

        if (idEtablissement != -1) {
            // Utilisation de l'ID de l'établissement pour récupérer les données des bornes depuis l'API
            fetchBornesData(idEtablissement);
        } else {
            // Gérer le cas où l'ID de l'établissement n'est pas disponible
        }


        // Gestion du retour en arrière lorsque l'image est cliquée
        findViewById(R.id.icon_back_bornes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Utilisation de la méthode onBackPressed() pour revenir en arrière
            }
        });
    }

    // Méthode pour récupérer les données des bornes depuis l'API
    private void fetchBornesData(int idEtablissement) {
        // URL de l'API à interroger
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Bornes-Appli.php?id_etablissement=" + idEtablissement;

        // Création de la requête JSON Object GET avec Volley
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            // Parcours du tableau JSON des bornes
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject borneObject = response.getJSONObject(i);

                                // Extraction des données de chaque borne
                                int idBorne = borneObject.getInt("IdBorne");
                                int niveauGel = borneObject.getInt("Niveau_Gel");
                                int niveauBatterie = borneObject.getInt("Niveau_Batterie");
                                String salle = borneObject.getString("Salle");
                                String heure = borneObject.getString("Heure");
                                String date = borneObject.getString("Date");


                                // Création de l'objet MyBorne
                                MyBorne borne = new MyBorne(idBorne, niveauGel, niveauBatterie, salle, heure, date);

                                // Ajout de la borne à la liste
                                mBornes.add(borne);
                            }

                            // Rafraîchissement de la RecyclerView
                            mBorneAdapter.notifyDataSetChanged();

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

        // Ajout de la requête à la file d'attente de Volley
        Volley.newRequestQueue(this).add(request);
    }

    // Classe adaptateur pour RecyclerView
    private class BorneAdapter extends RecyclerView.Adapter<BorneAdapter.BorneViewHolder> {

        private List<MyBorne> myBornes;

        public BorneAdapter(List<MyBorne> bornes) {
            myBornes = bornes;
        }


        @Override
        public BorneViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bornes, parent, false);
            return new BorneViewHolder(view);
        }

        @Override
        public void onBindViewHolder( BorneViewHolder holder, int position) {
            MyBorne borne = myBornes.get(position);
            holder.bind(borne);
        }

        @Override
        public int getItemCount() {
            return myBornes.size();
        }

        public class BorneViewHolder extends RecyclerView.ViewHolder {
            private TextView idBorneTextView, batterieTextView, gelTextView, heureTextView, dateTextView, salleTextView;

            public BorneViewHolder(View itemView) {
                super(itemView);
                idBorneTextView = itemView.findViewById(R.id.text_id_borne);
                batterieTextView = itemView.findViewById(R.id.text_batterie);
                salleTextView = itemView.findViewById(R.id.text_salle);
                gelTextView = itemView.findViewById(R.id.text_gel);
                heureTextView = itemView.findViewById(R.id.text_heure);
                dateTextView = itemView.findViewById(R.id.text_date);
            }

            public void bind(MyBorne borne) {
                idBorneTextView.setText("id Borne : " + borne.getIdBorne());
                batterieTextView.setText("Niveau de batterie : " + borne.getBatterie());
                salleTextView.setText("Salle : " + borne.getSalle());
                gelTextView.setText("Niveau de gel : " + borne.getGel());
                heureTextView.setText("Heure : " + borne.getHeure());
                dateTextView.setText("Date : " + borne.getDate());
            }
        }
    }
}
