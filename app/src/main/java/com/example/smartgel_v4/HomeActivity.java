package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    CardView cardBornes;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String useremail = sharedPreferences.getString("useremail", "").toString();
        Toast.makeText(getApplicationContext(),"Bienvenue : " +useremail,Toast.LENGTH_SHORT).show();

        CardView cardOneBorne = findViewById(R.id.cardOneBorne);
        CardView cardBornes = findViewById(R.id.cardBornes);

        cardOneBorne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this,OneBorneActivity.class));

            }
        });

        cardBornes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,BornesActivity.class));
            }
        });
    }
}