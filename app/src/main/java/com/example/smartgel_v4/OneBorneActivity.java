package com.example.smartgel_v4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class OneBorneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_borne);

        // Appelle la méthode pour récupérer les données des bornes
        fetchBorneData();

// Retour en arrière lorsque l'image est cliquée
        ImageView imgBack = findViewById(R.id.icon_back_one_borne);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Utiliser la méthode onBackPressed() pour revenir en arrière
            }
        });
    }

    private void fetchBorneData() {
        // URL de l'API à interroger
        String url = "https://c6976853-fd03-45cd-b519-bcd0d86b6d8c.mock.pstmn.io/JeanRostand?id=1";

        // Création de la requête JSON Object GET avec Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int idBorne = response.getInt("idBorne");
                            int batterie = response.getInt("Batterie");
                            int gel = response.getInt("Gel");
                            String heure = response.getString("Heure");
                            String date = response.getString("Date");

                            //creation de l'objet MyBorne avec les donées recuperées
                            MyBorne borne = new MyBorne(idBorne, batterie, gel, heure, date);

                            //mettre à jour l'interface utislisateur en utilisant les données de l'ojet MyBorne
                            updateUi(borne);

/*
                            Log.d("idBorne", String.valueOf(idBorne));
                            Log.d("batterie", String.valueOf(batterie));
                            Log.d("gel", String.valueOf(gel));
                            Log.d("heure", heure);
                            Log.d("date", date);


                            // Mettre à jour les TextViews dans l'interface utilisateur avec les données récupérées
                           TextView idBorneTextView = findViewById(R.id.TextViewIdBorne);
                            TextView batterieTextView = findViewById(R.id.TextViewBatterie);
                            TextView gelTextView = findViewById(R.id.TextViewGel);
                            TextView heureTextView = findViewById(R.id.TextViewHeure);
                            TextView dateTextView = findViewById(R.id.TextViewDate);





                            idBorneTextView.setText("Id Borne: " + idBorne);
                            batterieTextView.setText("Batterie: " + batterie);
                            gelTextView.setText("Gel: " + gel);
                            heureTextView.setText("Heure :"+heure);
                            dateTextView.setText("Date : "+date);
*/

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

    private void updateUi(MyBorne borne){
        TextView idBorneTextView = findViewById(R.id.TextViewIdBorne);
        TextView batterieTextView = findViewById(R.id.TextViewBatterie);
        TextView gelTextView = findViewById(R.id.TextViewGel);
        TextView heureTextView = findViewById(R.id.TextViewHeure);
        TextView dateTextView = findViewById(R.id.TextViewDate);

        idBorneTextView.setText("Id Borne: " + borne.getIdBorne());
        batterieTextView.setText("Batterie: " + borne.getBatterie());
        gelTextView.setText("Gel: " + borne.getGel());
        heureTextView.setText("Heure :"+borne.getHeure());
        dateTextView.setText("Date : "+borne.getDate());

    }
}
