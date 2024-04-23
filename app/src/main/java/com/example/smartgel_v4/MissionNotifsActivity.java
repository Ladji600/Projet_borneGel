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
import android.widget.Toast;

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

public class MissionNotifsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

 //   private List<MissionItem> missionList;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_notifs);

        recyclerView = findViewById(R.id.recycler_view_mission);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        missionList = new ArrayList<>();

        // Récupération de l'ID de l'utilisateur actuel
        int userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
            fetchMissions(userId);
        } else {
            Toast.makeText(this, "ID utilisateur non valide", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMissions(int userId) {
        String url = "https://804b3669-1a04-43a0-8a07-09a076ab6c78.mock.pstmn.io/mission?idUser=" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject missionObject = response.getJSONObject(i);
                                String missionTitle = missionObject.getString("title");
                                String missionDescription = missionObject.getString("description");
                                // Ajoutez d'autres données de mission si nécessaire
                                MissionItem missionItem = new MissionItem(missionTitle, missionDescription);
                                missionList.add(missionItem);
                            }
                            // Créer et définir l'adaptateur ici
                            MissionAdapter missionAdapter = new MissionAdapter(MissionNotifsActivity.this, missionList);
                            recyclerView.setAdapter(missionAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MissionNotifsActivity.this, "Erreur lors du traitement de la réponse JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Erreur : " + error.getMessage());
                        Toast.makeText(MissionNotifsActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    // Classe de modèle MissionItem
    private static class MissionItem {
        private String title;
        private String description;

        public MissionItem(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }

    // Classe de l'adaptateur MissionAdapter
    private static class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionViewHolder> {
        private MissionItem context;
        private List<MissionItem> missionList;

        public MissionAdapter(MissionItem context, List<MissionItem> missionList) {
            this.context = context;
            this.missionList = missionList;
        }

        @Override
        public MissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mission_notifs, parent, false);
            return new MissionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MissionViewHolder holder, int position) {
            MissionItem missionItem = missionList.get(position);
            holder.idBorneText.setText(missionItem.getTitle());
            holder.missionText.setText(missionItem.getDescription());
            //ajouter +
        }

        @Override
        public int getItemCount() {
            return missionList.size();
        }

        public static class MissionViewHolder extends RecyclerView.ViewHolder {
            public TextView idBorneText;
            public TextView missionText;
            public TextView salleText;
            public TextView emailText;


            public MissionViewHolder(View itemView) {
                super(itemView);
                 idBorneText = itemView.findViewById(R.id.text_id_borne);
                missionText = itemView.findViewById(R.id.text_mission);
                salleText = itemView.findViewById(R.id.text_salle);
                emailText = itemView.findViewById(R.id.text_email);
            }
        }
    }*/
}
