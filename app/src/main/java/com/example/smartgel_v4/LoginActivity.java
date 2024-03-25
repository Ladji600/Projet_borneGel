package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText edEmail, edPAssword;
    Button btnConnexion;
    TextView textSite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.editTextEmail);
        edPAssword = findViewById(R.id.editTextPassword);
        btnConnexion = findViewById(R.id.buttonConnexion);
        textSite = findViewById((R.id.textViewSite));

        btnConnexion.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = edEmail.getText().toString();
                String userpassword = edPAssword.getText().toString();

                if(useremail.length()==0 || userpassword.length()==0){
                    Toast.makeText(getApplicationContext(),"Veuillez completer tous les champs",Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(getApplicationContext(), "Connexion reusie", Toast.LENGTH_SHORT).show();
                    //pour être rediger dans une autre view

                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
            }
        }));
        /*
        textSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redirection to the site web


            }
        });*/

    }
}
