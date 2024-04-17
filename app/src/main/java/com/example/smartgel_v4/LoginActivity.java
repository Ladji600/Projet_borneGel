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
        String url = "https://c6976853-fd03-45cd-b519-bcd0d86b6d8c.mock.pstmn.io/connexion";
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
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void handleLoginResponse(JSONObject response) {
        try {
            Log.d("API_RESPONSE", "Réponse reçue : " + response.toString());

            int idUser = response.getInt("idUser");
            String email = response.getString("email");
         //   String mdp = response.getString("mdp");
            String prenom = response.getString("prenom");
            String nom = response.getString("nom");
          //  int idEtablissement = response.getInt("idEtablissement");
            int role = response.getInt("role");

            Log.d("idUser", "LOgin Id de l'utilisateur : " + idUser);

            // Vous pouvez récupérer les autres données ici

            // Redirection en fonction du rôle de l'utilisateur
            switch (role) {
                case 3: // Responsable Technique
                    Intent intentResponsableTech = new Intent(LoginActivity.this, EtablissementActivity.class);
                    intentResponsableTech.putExtra("idUser", idUser);
                    intentResponsableTech.putExtra("email", email);
                    intentResponsableTech.putExtra("nom", nom);
                    intentResponsableTech.putExtra("prenom", prenom);

                    startActivity(intentResponsableTech);
                    break;
                case 2: // Responsable Agent
                    Intent intentResponsableAgent = new Intent(LoginActivity.this, ResponsableAgentActivity.class);
                    int idEtablissement = response.getInt("idEtablissement");
                    intentResponsableAgent.putExtra("idUser", idUser);
                    intentResponsableAgent.putExtra("email", email);
                    intentResponsableAgent.putExtra("nom", nom);
                    intentResponsableAgent.putExtra("prenom", prenom);
                    intentResponsableAgent.putExtra("idEtablissement", idEtablissement);
                    startActivity(intentResponsableAgent);
                    break;
                case 1: // Agent
                    Intent intentAgent = new Intent(LoginActivity.this, AgentActivity.class);
                    idEtablissement = response.getInt("idEtablissement");
                    intentAgent.putExtra("idUser", idUser);
                    intentAgent.putExtra("email", email);
                    intentAgent.putExtra("nom", nom);
                    intentAgent.putExtra("prenom",prenom);
                    intentAgent.putExtra("idEtablissement", idEtablissement);
                    startActivity(intentAgent);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Rôle non reconnu", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erreur réponse JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
