package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResponsableTechActivity extends AppCompatActivity {
    CardView cardBornes;
   // CardView cardOneBorne;
    CardView cardAffectationList;
    private String fullname;
    private TextView nomEtablissementText;

    CardView cardHistoriqueList;
    CardView cardAlerteList;
    CardView cardRecherche;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable_tech);

        // Récupérer l'email de l'intent
     //   String userEmail = getIntent().getStringExtra("email");
        int idEtablissement = getIntent().getIntExtra("IdEtablissement", -1);
        int idUser = getIntent().getIntExtra("IdEmployes", -1);
        String userName = getIntent().getStringExtra("Nom");
        String userFirstName = getIntent().getStringExtra("Prenom");
        String nomEtablissement =getIntent().getStringExtra("NomEtablissement");
        String adresse = getIntent().getStringExtra("Address");
        int  idRole = getIntent().getIntExtra("Id_Role", -1);


        // Afficher l'email dans le TextView approprié
        TextView userNameTextView = findViewById(R.id.userRespTechTextView);

        fullname = userName + " " + userFirstName;
        userNameTextView.setText(fullname);
        // Ajouter la fonctionnalité de déconnexion
        ImageView imgDeconnexion = findViewById(R.id.imgLogOut);

        nomEtablissementText = findViewById(R.id.nomEtablissementTextView);
        nomEtablissementText.setText(nomEtablissement);
        imgDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode de déconnexion en utilisant les informations d'établissement
                logoutUser();
            }
        });


        ImageView imgRetour = findViewById(R.id.imgRetourRespTech);
// Ajouter un OnClickListener pour l'image de retour
        imgRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lors du clic sur l'image de retour
                finish();
            }
        });



         cardBornes = findViewById(R.id.cardBornes);
         cardAlerteList = findViewById(R.id.cardAlerte);
         cardHistoriqueList = findViewById(R.id.cardHistorique);
         cardRecherche = findViewById(R.id.cardRecherche);


         cardRecherche.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(ResponsableTechActivity.this, RechercheActivity.class);
                 intent.putExtra("Nom", userName);
                 intent.putExtra("IdEtablissement", idEtablissement);
                 intent.putExtra("IdEmployes", idUser);
                 intent.putExtra("NomEtablissement", nomEtablissement);
                 intent.putExtra("Address", adresse);
                 intent.putExtra("Id_Role", -1);
                 startActivity(intent);
             }
         });


        cardAlerteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResponsableTechActivity.this, AlertesRespActivity.class);
                intent.putExtra("Nom", userName);
                intent.putExtra("IdEtablissement", idEtablissement);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("NomEtablissement", nomEtablissement);
                intent.putExtra("Address", adresse);

                startActivity(intent);

            }
        });
        cardHistoriqueList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ResponsableTechActivity.this, ResponsableTechniqueHistoriqueMission.class);
                intent.putExtra("Nom", userName);
                intent.putExtra("IdEtablissement", idEtablissement);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("NomEtablissement", nomEtablissement);
                intent.putExtra("Address", adresse);

                startActivity(intent);

            }
        });

        cardBornes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResponsableTechActivity.this, BornesActivity.class);
                intent.putExtra("Nom", userName);
                intent.putExtra("IdEtablissement", idEtablissement);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("NomEtablissement", nomEtablissement);
                intent.putExtra("Address", adresse);
                startActivity(intent);
            }
        });
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.smartgel_v4.PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Supprimer les informations liées à l'utilisateur
        editor.remove("IdEmployes");
        editor.remove("Id_Role");
        editor.apply();

        // Rediriger vers LoginActivity
        Intent intent = new Intent(ResponsableTechActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Fermer l'activité actuelle pour éviter qu'elle ne reste dans la pile d'activités
    }
}