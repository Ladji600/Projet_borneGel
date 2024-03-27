package com.example.smartgel_v4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    //variable ajoutées
    private RecyclerView mRecyclerView;
    private BorneAdapter mBorneAdapter;
    private List<MyBorne> mBornes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bornes);

        //
        mRecyclerView = findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBornes = new ArrayList<>();
        mBorneAdapter = new BorneAdapter(mBornes);
        mRecyclerView.setAdapter(mBorneAdapter);


        fetchBornesData();

    }

    private void fetchBornesData() {
        // URL de l'API à interroger
        String url = "https://c2a10eed-4b23-4f87-b01a-2596e8315607.mock.pstmn.io/dahsboard?etablissement=JeanRostand";

        // Création de la requête JSON Object GET avec Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract the "Bornes" array from the response object
                            JSONArray bornesArray = response.getJSONArray("Bornes");

                            // Parcourir le tableau JSON des bornes
                            for (int i = 0; i < bornesArray.length(); i++) {
                                JSONObject borneObject = bornesArray.getJSONObject(i);

                                int idBorne = borneObject.getInt("idBorne");
                                int batterie = borneObject.getInt("batterie");
                                int gel = borneObject.getInt("gel");
                                String heure = borneObject.getString("Heure");
                                String date = borneObject.getString("Date");

                                MyBorne borne = new MyBorne(idBorne, batterie, gel, heure, date);

                                mBornes.add(borne);


                          /*     // Afficher les informations de chaque borne
                                Log.d("Borne " + i, "Id Borne: " + idBorne +
                                        ", Batterie: " + batterie +
                                        ", Gel: " + gel +
                                        ", Heure: " + heure +
                                        ", Date: " + date);*/
                            }    mBorneAdapter.notifyDataSetChanged(); 
                                                                       

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

    //classe adaptateur pour RecyclerView

    private class BorneAdapter extends RecyclerView.Adapter<BorneAdapter.BorneViewHolder> {

        private List<MyBorne> myBornes;

        public BorneAdapter(List<MyBorne> bornes) {
            myBornes = bornes;
        }

        @NonNull
        @Override
        public BorneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bornes, parent, false);
            return new BorneViewHolder(view);
        }

        public void onBindViewHolder(@NonNull BorneViewHolder holder, int position) {
            MyBorne borne = myBornes.get(position);
            holder.bind(borne);

        }

        public int getItemCount() {
            return myBornes.size();
        }

        public class BorneViewHolder extends RecyclerView.ViewHolder {
            private TextView idBorneTextView, batterieTextView, gelTextView, heureTextView, dateTextView;

            public BorneViewHolder(View itemView) {

                super(itemView);
                idBorneTextView = itemView.findViewById(R.id.text_id_borne);
                batterieTextView = itemView.findViewById(R.id.text_batterie);
                gelTextView = itemView.findViewById(R.id.text_gel);
                heureTextView = itemView.findViewById(R.id.text_heure);
                dateTextView = itemView.findViewById(R.id.text_date);
            }

            public void bind(MyBorne borne) {
                idBorneTextView.setText("id Borne : " + borne.getIdBorne());
                batterieTextView.setText("Niveua de batterie : " + borne.getBatterie());
                gelTextView.setText("Niveau de gel : " + borne.getGel());
                heureTextView.setText("Heure : " + borne.getHeure());
                dateTextView.setText("Date : " + borne.getDate());
            }
        }
    }

}
