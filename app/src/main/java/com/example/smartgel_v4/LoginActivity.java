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

import java.util.Iterator;

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
                Log.d("LoginActivity", "Email saisi : " + useremail);
                Log.d("LoginActivity", "Mot de passe saisi : " + userpassword);

                if (useremail.length() == 0 || userpassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez compléter tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();
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

            int id = response.getInt("id");
            String email = response.getString("email");
            String mdp = response.getString("mdp");
            String prenom = response.getString("prénom");
            String nom = response.getString("nom");
            String etablissement = response.getString("etablissement");
            String role = response.getString("role");

            Log.d("API_RESPONSE", "id: " + id);
            Log.d("API_RESPONSE", "email: " + email);
            Log.d("API_RESPONSE", "mdp: " + mdp);
            Log.d("API_RESPONSE", "prénom: " + prenom);
            Log.d("API_RESPONSE", "nom: " + nom);
            Log.d("API_RESPONSE", "etablissement: " + etablissement);
            Log.d("API_RESPONSE", "role: " + role);

            switch (role) {
                case "Agent":

                    // startActivity(new Intent(LoginActivity.this, ResponsableTechActivity.class));
                    Intent intent = new Intent(LoginActivity.this, AgentActivity.class);
                    // Mettre l'email dans l'intent
                    intent.putExtra("email", email);
                    intent.putExtra("etablissement", etablissement);
                    // Démarrer l'activité ResponsableTechActivity avec l'intent
                    startActivity(intent);
                    break;
                case "Responsable Agent":

                    // startActivity(new Intent(LoginActivity.this, ResponsableTechActivity.class));
                     intent = new Intent(LoginActivity.this, ResponsableAgentActivity.class);
                    // Mettre l'email dans l'intent
                    intent.putExtra("email", email);
                    intent.putExtra("etablissement", etablissement);
                    // Démarrer l'activité ResponsableTechActivity avec l'intent
                    startActivity(intent);
                    break;
                  case "Responsable Technique":

                      intent = new Intent(LoginActivity.this, EtablissementActivity.class);
                      // Mettre l'email dans l'intent
                      intent.putExtra("email", email);
                      intent.putExtra("id", id);
                      // Démarrer l'activité ResponsableTechActivity avec l'intent
                      startActivity(intent);

    break;

                default:
                    // Redirection par défaut si le rôle n'est pas reconnu
                    startActivity(new Intent(LoginActivity.this, DefaultActivity.class));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erreur réponse JSON", Toast.LENGTH_SHORT).show();
        }
    }



}
