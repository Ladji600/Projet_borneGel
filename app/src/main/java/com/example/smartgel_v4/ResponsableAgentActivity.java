package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResponsableAgentActivity extends AppCompatActivity {
    CardView cardHistoriqueAffectation;
     private String fullname;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable_agent);

        // Récupérer l'email de l'intent
        String userEmail = getIntent().getStringExtra("email");
        String userName = getIntent().getStringExtra("nom");
        String userFirstName = getIntent().getStringExtra("prenom");
        int idEtablissement = getIntent().getIntExtra("idEtablissement", -1);
        int idUser = getIntent().getIntExtra("idUser", -1);


        // Afficher l'email dans le TextView approprié
        TextView userNameTextView = findViewById(R.id.userRespAgentTextView);
        fullname = userName + " " + userFirstName;
        userNameTextView.setText(fullname);

        // Ajouter la fonctionnalité de déconnexion
        ImageView imgDeconnexion = findViewById(R.id.imgLogOut);
        imgDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode de déconnexion de la classe utilitaire
                SessionManager.logout(ResponsableAgentActivity.this);
            }
        });

/*
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String useremail = sharedPreferences.getString("useremail", "").toString();
        Toast.makeText(getApplicationContext(),"Bienvenue : " +useremail,Toast.LENGTH_SHORT).show();
*/
        CardView cardHistoriqueAffectation = findViewById(R.id.cardHistorique);
        CardView cardBornes = findViewById(R.id.cardBornes);

       // String etablissement = "etablissement";
        cardHistoriqueAffectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ResponsableAgentActivity.this, HistoriqueAffectationsActivity.class);
                intent.putExtra("nom", userName);
                intent.putExtra("idEtablissement", idEtablissement);
                intent.putExtra("idUser", idUser);
                startActivity(intent);

            }
        });

        cardBornes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResponsableAgentActivity.this, BornesActivity.class);

                intent.putExtra("idEtablissement", idEtablissement); // Remplacez etablissementId par l'identifiant réel de l'établissement

                // Démarrer l'activité BornesActivity avec l'Intent
                startActivity(intent);
            }
        });
    }
}