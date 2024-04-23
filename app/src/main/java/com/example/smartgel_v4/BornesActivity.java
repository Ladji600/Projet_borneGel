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

        // Récupération de l'ID de l'établissement depuis l'intent
        idEtablissement = getIntent().getIntExtra("idEtablissement", -1);

        if (idEtablissement != -1) {
            // Utilisation de l'ID de l'établissement pour récupérer les données des bornes depuis l'API
            fetchBornesData(idEtablissement);
        } else {
            // Gérer le cas où l'ID de l'établissement n'est pas disponible
        }


      //  TextView EtablissementTittleTextView = findViewById(R.id.etablissementTittleTextView);
       // EtablissementTittleTextView.setText(etanlissementName);

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
        String url = "https://804b3669-1a04-43a0-8a07-09a076ab6c78.mock.pstmn.io/dashboard?idEtablissement=" + idEtablissement;

        // Création de la requête JSON Object GET avec Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extraction du tableau "Bornes" de l'objet de réponse
                             nomEtablissement = response.getString("Etablissement");

                            TextView etablissementTittleTextView = findViewById(R.id.etablissementTittleTextView);
                            etablissementTittleTextView.setText(nomEtablissement);

                            JSONArray bornesArray = response.getJSONArray("Bornes");

                            // Parcours du tableau JSON des bornes
                            for (int i = 0; i < bornesArray.length(); i++) {
                                JSONObject borneObject = bornesArray.getJSONObject(i);

                                // Extraction des données de chaque borne
                                int idBorne = borneObject.getInt("idBorne");
                                int gel = borneObject.getInt("gel");
                                int batterie = borneObject.getInt("batterie");
                                String salle = borneObject.getString("salle");
                                String heure = borneObject.getString("heure");
                                String date = borneObject.getString("date");

                                // Création de l'objet MyBorne
                                MyBorne borne = new MyBorne(idBorne, gel, batterie, salle, heure, date);

                                // Ajout de la borne à la liste
                                mBornes.add(borne);
                                Log.d("API Response", "Response: " + response.toString());
                               // Log.d("ETABLISSEMENT", "Nom de l'établissement : " + nomEtablissement); // Ajoutez cette ligne

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
