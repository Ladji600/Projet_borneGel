package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class LoginActivityPost extends AppCompatActivity {

    EditText edEmail, edPassword;
    Button btnConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.editTextEmail);
        edPassword = findViewById(R.id.editTextPassword);
        btnConnexion = findViewById(R.id.buttonConnexion);

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = edEmail.getText().toString().trim(); // Mettre à jour useremail avec le nouvel email
                String userpassword = edPassword.getText().toString().trim(); // Mettre à jour userpassword avec le nouveau mot de passe
                // Ajouter des logs pour vérifier les valeurs saisies
                Log.d("LoginActivity", "Email saisi : " + useremail);
                Log.d("LoginActivity", "Mot de passe saisi : " + userpassword);

                if (useremail.length() == 0 || userpassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez compléter tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();
                    // Appel à l'API
                    callLoginAPI(useremail, userpassword);
                    //  edEmail.getText().clear();
                    // edPassword.getText().clear();

                }
            }
        });
    }

    private void callLoginAPI(String email, String password) {
        String url = "https://8f5c7810-61e7-476a-80c7-31a5dfb3ed93.mock.pstmn.io/connexion1";

        //String url = "https://46b15cfb-cb64-459e-b200-e0252f8636ca.mock.pstmn.io/login";
        // Création des paramètres de la requête
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        // Création du corps de la requête JSON
        JSONObject jsonParams = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Traitement de la réponse de l'API
                        handleLoginResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivityPost.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void handleLoginResponse(JSONObject response) {
        try {
         /*   // Affichage de toutes les informations de la réponse dans le logcat
            Iterator<String> keys = response.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Log.d("API_RESPONSE", key + ": " + response.get(key));*/

            if (response.has("message")) {
                String userMessage = response.getString("message");
                Log.d("API_RESPONSE", "id: " + userMessage);
           /* if (response.has("IdEmployes")) {
                int userId = response.getInt("IdEmployes");
                Log.d("API_RESPONSE", "id: " + userId);
            }

            if (response.has("Nom")) {
                String userNom= response.getString("Nom");
                Log.d("API_RESPONSE", "nom: " + userNom);
            }

            if (response.has("Prenom")) {
                String userPrenom = response.getString("Prenom");
                Log.d("API_RESPONSE", "prenom: " + userPrenom);
            }
            if (response.has("Mail")) {
                String userEmail = response.getString("Mail");
                Log.d("API_RESPONSE", "mail: " + userEmail);
            }
            if (response.has("Mot_De_Passe")) {
                String userMDP = response.getString("Mot_De_Passe");
                Log.d("API_RESPONSE", "mdp: " + userMDP);
            }
            if (response.has("Id_Role")) {
                int userRole = response.getInt("Id_Role");
                Log.d("API_RESPONSE", "id_role: " + userRole);
            }
            if (response.has("Id_Etablissement")) {
                int userEtablissement = response.getInt("Id_Etablissement");
                Log.d("API_RESPONSE", "Etablissement: " + userEtablissement);*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erreur réponse JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
