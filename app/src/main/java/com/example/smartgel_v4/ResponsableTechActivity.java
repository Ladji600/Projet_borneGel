package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
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
                // Appel de la méthode de déconnexion de la classe utilitaire
                SessionManager.logout(ResponsableTechActivity.this);
            }
        });


        ImageView imgRetour = findViewById(R.id.imgRetourRespTech);
// Ajouter un OnClickListener pour l'image de retour
        imgRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lors du clic sur l'image de retour
                // Par exemple, revenir à l'activité précédente
                finish();
            }
        });

     /*   SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String useremail = sharedPreferences.getString("email", "").toString();
        Toast.makeText(getApplicationContext(),"Bienvenue : " +useremail,Toast.LENGTH_SHORT).show();
*/
        // cardOneBorne = findViewById(R.id.cardOneBorne);
         cardBornes = findViewById(R.id.cardBornes);
         cardAlerteList = findViewById(R.id.cardAlerte);
         cardHistoriqueList = findViewById(R.id.cardHistorique);



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
}