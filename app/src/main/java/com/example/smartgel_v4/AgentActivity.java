package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AgentActivity extends AppCompatActivity {

    private String fullname;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        // Récupérer l'email de l'intent
        String userEmail = getIntent().getStringExtra("email");
        String userName = getIntent().getStringExtra("nom");
        String userFirstName = getIntent().getStringExtra("prenom");
        int idEtablissement = getIntent().getIntExtra("idEtablissement", -1);
        int idUser = getIntent().getIntExtra("idUser", -1 );

        Log.d("idUser", "Agent Activity Id de l'utilisateur : " + idUser);

        // Afficher l'email dans le TextView approprié
        TextView userNameTextView = findViewById(R.id.userAgentTextView);
        fullname = userName + " " + userFirstName ;
        userNameTextView.setText(fullname);

        // Ajouter la fonctionnalité de déconnexion
        ImageView imgDeconnexion = findViewById(R.id.imgLogOut);
        imgDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode de déconnexion de la classe utilitaire
                SessionManager.logout(AgentActivity.this);
            }
        });


     /*   SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String useremail = sharedPreferences.getString("useremail", "").toString();
        Toast.makeText(getApplicationContext(),"Bienvenue : " +useremail,Toast.LENGTH_SHORT).show();
*/
        CardView cardAffectationHistorique = findViewById(R.id.cardHistorique);
        CardView cardBornes = findViewById(R.id.cardBornes);
       // String etablissement = "etablissement";

        cardAffectationHistorique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AgentActivity.this, AgentHistoriqueAffectationsActivity.class);
                intent.putExtra("nom", userName);
                intent.putExtra("idEtablissement", idEtablissement);
                intent.putExtra("idUser", idUser);
                intent.putExtra("prenom", userFirstName);
                Log.d("idUser", "Intent Activity Id de l'utilisateur : " + idUser);
                startActivity(intent);

            }
        });

        cardBornes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgentActivity.this, BornesActivity.class);

                intent.putExtra("idEtablissement", idEtablissement); // Remplacez etablissementId par l'identifiant réel de l'établissement
                intent.putExtra("nom", userName);
                intent.putExtra("idUser", idUser);
                intent.putExtra("prenom", userFirstName);
                // Démarrer l'activité BornesActivity avec l'Intent
                startActivity(intent);

            }
        });
    }
}