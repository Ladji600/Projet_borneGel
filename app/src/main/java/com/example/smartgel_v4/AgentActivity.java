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
    private TextView nomEtablissementText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        // Récupérer l'email de l'intent
        String userEmail = getIntent().getStringExtra("Mail");
        String userName = getIntent().getStringExtra("Nom");
        String userFirstName = getIntent().getStringExtra("Prenom");
        int idEtablissement = getIntent().getIntExtra("Id_Etablissement", -1);
        int idUser = getIntent().getIntExtra("IdEmployes", -1 );
        String nomEtablissement = getIntent().getStringExtra("NomEtablissement");
        String adresse = getIntent().getStringExtra("Address");

        Log.d("idUser", "Agent Activity Id de l'utilisateur : " + idUser);

        nomEtablissementText = findViewById(R.id.nomEtablissementTextView);
        nomEtablissementText.setText(nomEtablissement);

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
        CardView cardNotification = findViewById(R.id.cardNotifications);
       // String etablissement = "etablissement";


        cardNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgentActivity.this, MissionNotifsActivity.class);
                intent.putExtra("Nom", userName);
                intent.putExtra("Id_Etablissement", idEtablissement);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("Prenom", userFirstName);
                intent.putExtra("NomEtablissement", nomEtablissement);
                intent.putExtra("Address", adresse);
                Log.d("idUser", "Intent Activity Id de l'utilisateur : " + idUser);
                startActivity(intent);
            }
        });
        cardAffectationHistorique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AgentActivity.this, AgentHistoriqueAffectationsActivity.class);
                intent.putExtra("Nom", userName);
                intent.putExtra("Id_Etablissement", idEtablissement);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("Prenom", userFirstName);
                Log.d("idUser", "Intent Activity Id de l'utilisateur : " + idUser);
                intent.putExtra("NomEtablissement", nomEtablissement);
                intent.putExtra("Address", adresse);
                startActivity(intent);

            }
        });

        cardBornes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgentActivity.this, BornesActivity.class);

                intent.putExtra("Id_Etablissement", idEtablissement); // Remplacez etablissementId par l'identifiant réel de l'établissement
                intent.putExtra("Nom", userName);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("Prenom", userFirstName);
                intent.putExtra("NomEtablissement", nomEtablissement);
                intent.putExtra("Address", adresse);
                // Démarrer l'activité BornesActivity avec l'Intent
                startActivity(intent);

            }
        });
    }
}