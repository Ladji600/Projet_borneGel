package com.example.smartgel_v4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgel_v4.MyEtablissement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EtablissementActivity extends AppCompatActivity {

    // Variables ajoutées
    private RecyclerView mRecyclerView;
    private String userName;

    private String prenom;
    private String userEmail;
    private String etablissementName;
    private String addresse;

    private int idEtablissement;
    private EtablissementAdapter mEtablissementAdapter;
    private List<MyEtablissement> mEtablissements;
    private int userId; // Déclaration de la variable userId
   // private int userEtablissment;

    private int idRole;
   // public EtablissementActivity() {
    //}

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etablissement);
        userEmail = getIntent().getStringExtra("Mail");
       userName = getIntent().getStringExtra("Nom");
       userId = getIntent().getIntExtra("IdEmployes", -1);
       prenom = getIntent().getStringExtra("Prenom");
       idRole = getIntent().getIntExtra("Id_Role", -1);



        Log.d("IntentData", "Email: " + userEmail);
        Log.d("IntentData", "Nom: " + userName);
        Log.d("IntentData", "IdEmployes: " + userId);
        Log.d("IntentData", "Prenom: " + prenom);
        Log.d("EtablissementActivity", "NomEtablissement: " + etablissementName);
        Log.d("EtablissementActivity", "IdEtablissement: " + idEtablissement);

        Log.d("idUser", "Valeur de idUser dans le REsponsableActivity : " + userId);
        // Initialisation de la RecyclerView
        mRecyclerView = findViewById(R.id.recycle_view_etablissement);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//deconnexion
        ImageView imgDeconnexion = findViewById(R.id.imgLogOut);
        imgDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode de déconnexion en utilisant les informations d'établissement
                logoutUser();
            }
        });


        // Initialisation de la liste d'établissements
        mEtablissements = new ArrayList<>();
        mEtablissementAdapter = new EtablissementAdapter(mEtablissements, new OnItemClickListener() {
            @Override
            public void onItemClick(MyEtablissement etablissement) {

               idEtablissement = etablissement.getIdEtablissement();
                // Récupérer les informations de l'établissement cliqué
               etablissementName = etablissement.getEtablissement();
               addresse = etablissement.getAddresse();
                // Gérer le clic sur un établissement ici
                // Vous pouvez démarrer une nouvelle activité ou effectuer toute autre action souhaitée
                Intent intent = new Intent(EtablissementActivity.this, ResponsableTechActivity.class);
                intent.putExtra("Nom", userName);
                intent.putExtra("IdEtablissement", idEtablissement);
                intent.putExtra("IdEmployes", userId);
                intent.putExtra("Prenom", prenom);
                intent.putExtra("NomEtablissement", etablissementName);
                intent.putExtra("Address",addresse);
                intent.putExtra("Id_Role",-1);

                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mEtablissementAdapter);

        // Récupérer l'ID de l'utilisateur depuis l'intent
        userId = getIntent().getIntExtra("IdEmployes", -1);
        if (userId != -1) {
            // Appel de la méthode pour récupérer les données des établissements depuis l'API
            fetchEtablissementData();
        } else {
            Toast.makeText(this, "Erreur lors de la récupération de l'ID utilisateur", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchEtablissementData() {
        // URL de l'API à interroger
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Etablissement-Appli.php?Id_Employes=" + userId;

        // Création de la requête JSON Array GET avec Volley
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Parcourir le tableau JSON des établissements
                            Log.d("API_Response", "Réponse JSON : " + response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject etablissementObject = response.getJSONObject(i);

                                 idEtablissement = etablissementObject.getInt("IdEtablissement");
                                 etablissementName = etablissementObject.getString("NomEtablissement");
                                 addresse = etablissementObject.getString("Address");

                                // Création de l'objet MyEtablissement
                                MyEtablissement myEtablissement = new MyEtablissement(idEtablissement, etablissementName, addresse);

                                // Ajout de l'établissement à la liste
                                mEtablissements.add(myEtablissement);
                            }

                            // Rafraîchissement de la RecyclerView
                            mEtablissementAdapter.notifyDataSetChanged();
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
    // Interface pour gérer les clics sur les éléments de la liste
    public interface OnItemClickListener {
        void onItemClick(MyEtablissement etablissement);
    }

    // Classe adaptateur pour RecyclerView
    private class EtablissementAdapter extends RecyclerView.Adapter<EtablissementAdapter.EtablissementViewHolder> {

        private List<MyEtablissement> mEtablissements;
        private OnItemClickListener mListener; // Référence à l'interface OnItemClickListener

        // Constructeur prenant en paramètre une liste d'établissements et un écouteur d'événements
        public EtablissementAdapter(List<MyEtablissement> etablissements, OnItemClickListener listener) {
            mEtablissements = etablissements;
            mListener = listener;
        }

        @NonNull
        @Override
        public EtablissementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_etablissement, parent, false);
            return new EtablissementViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EtablissementViewHolder holder, int position) {
            MyEtablissement etablissement = mEtablissements.get(position);
            holder.bind(etablissement);

            // Ajout du clic sur l'élément de la liste
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(etablissement);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mEtablissements.size();
        }

        public class EtablissementViewHolder extends RecyclerView.ViewHolder {
            private TextView idEtablissementTextView, etablissementTextView, addresseEtablissement;

            public EtablissementViewHolder(View itemView) {
                super(itemView);
                idEtablissementTextView = itemView.findViewById(R.id.text_id_etablissement);
                etablissementTextView = itemView.findViewById(R.id.text_etablissement);
                addresseEtablissement = itemView.findViewById(R.id.text_addresse);
            }

            public void bind(MyEtablissement etablissement) {
                idEtablissementTextView.setText("ID Etablissement : " + etablissement.getIdEtablissement());
                etablissementTextView.setText("Etablissement : " + etablissement.getEtablissement());
                addresseEtablissement.setText(("Adresse : " + etablissement.getAddresse()));

            }
        }
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.smartgel_v4.PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Supprimer les informations liées à l'utilisateur
        editor.remove("IdEmployes");
        editor.remove("Id_Role");
        editor.apply();

        // Rediriger vers LoginActivity
        Intent intent = new Intent(EtablissementActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Fermer l'activité actuelle pour éviter qu'elle ne reste dans la pile d'activités

    }
}
