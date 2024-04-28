package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText edEmail, edPassword;
    Button btnConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.editTextEmail);
        edPassword = findViewById(R.id.editTextPassword);
        btnConnexion = findViewById(R.id.buttonConnexion);

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = edEmail.getText().toString().trim();
                String userpassword = edPassword.getText().toString().trim();

                if (useremail.length() == 0 || userpassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez compléter tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    callLoginAPI(useremail, userpassword);
                }
            }
        });
    }

    private void callLoginAPI(String email, String password) {
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Connexion2-Appli.php";
        url += "?email=" + email + "&password=" + password;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleLoginResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.getMessage());

                    }
                });

        Volley.newRequestQueue(this).add(request);
    }


    private void handleLoginResponse(JSONObject response) {
        try {
            Log.d("API_RESPONSE", "Réponse reçue : " + response.toString());

            int idUser = response.getInt("IdEmployes");
            String nom = response.getString("Nom");
            String prenom = response.getString("Prenom");
            String email = response.getString("Mail");
            String mdp = response.getString("Mot_De_Passe");
            int role = response.getInt("Id_Role");




           // sendEmail(email, prenom, nom);

            Log.d("idUser", "LOgin Id de l'utilisateur : " + idUser);

            // Vous pouvez récupérer les autres données ici

            // Redirection en fonction du rôle de l'utilisateur
            switch (role) {
                case 3: // Responsable Technique
                    Intent intentResponsableTech = new Intent(LoginActivity.this, EtablissementActivity.class);
                    intentResponsableTech.putExtra("IdEmployes", idUser);
                    intentResponsableTech.putExtra("Mail", email);
                    intentResponsableTech.putExtra("Nom", nom);
                    intentResponsableTech.putExtra("Prenom", prenom);

                    startActivity(intentResponsableTech);
                    break;
                case 2: // Responsable Agent
                    Intent intentResponsableAgent = new Intent(LoginActivity.this, ResponsableAgentActivity.class);
                    int idEtablissement = response.getInt("Id_Etablissement");
                    String nomEtablissement = response.getString("NomEtablissement");
                    String adresse = response.getString("Address");
                   // int idEtablissement = response.getInt("Id_Etablissement");
                    intentResponsableAgent.putExtra("IdEmployes", idUser);
                    intentResponsableAgent.putExtra("Mail", email);
                    intentResponsableAgent.putExtra("Nom", nom);
                    intentResponsableAgent.putExtra("Prenom", prenom);
                    intentResponsableAgent.putExtra("Id_Etablissement", idEtablissement);
                    intentResponsableAgent.putExtra("NomEtablissement", nomEtablissement);
                    intentResponsableAgent.putExtra("Address", adresse);
                    startActivity(intentResponsableAgent);
                    break;
                case 1: // Agent
                    Intent intentAgent = new Intent(LoginActivity.this, AgentActivity.class);
                     idEtablissement = response.getInt("Id_Etablissement");
                     nomEtablissement = response.getString("NomEtablissement");
                     adresse = response.getString("Address");
                  //  idEtablissement = response.getInt("Id_Etablissement");
                    intentAgent.putExtra("IdEmployes", idUser);
                    intentAgent.putExtra("Mail", email);
                    intentAgent.putExtra("Nom", nom);
                    intentAgent.putExtra("Prenom",prenom);
                    intentAgent.putExtra("Id_Etablissement", idEtablissement);
                    intentAgent.putExtra("NomEtablissement", nomEtablissement);
                    intentAgent.putExtra("Address", adresse);
                    startActivity(intentAgent);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Rôle non reconnu", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erreur réponse JSON", Toast.LENGTH_SHORT).show();
            Log.d("API_RESPONSE", "Réponse reçue : " + response.toString());
        }
    }
}
