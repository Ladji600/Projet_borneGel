package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResponsableTechActivity extends AppCompatActivity {
    CardView cardBornes;
    CardView cardOneBorne;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable_tech);

        // Récupérer l'email de l'intent
        String userEmail = getIntent().getStringExtra("email");

        // Afficher l'email dans le TextView approprié
        TextView userEmailTextView = findViewById(R.id.userRespTechTextView);
        userEmailTextView.setText(userEmail);

        // Ajouter la fonctionnalité de déconnexion
        ImageView imgDeconnexion = findViewById(R.id.imgLogOut);
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
         cardOneBorne = findViewById(R.id.cardOneBorne);
         cardBornes = findViewById(R.id.cardBornes);

        cardOneBorne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ResponsableTechActivity.this,OneBorneActivity.class));

            }
        });

        cardBornes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResponsableTechActivity.this,BornesActivity.class));
            }
        });
    }
}