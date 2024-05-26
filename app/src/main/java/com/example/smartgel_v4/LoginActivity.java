package com.example.smartgel_v4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText edEmail, edPassword;
    Button btnConnexion;
    TextView textViewSite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Vérifier si un utilisateur est déjà connecté
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.smartgel_v4.PREFERENCES", MODE_PRIVATE);
        int idEmployes = sharedPreferences.getInt("IdEmployes", -1);
        if (idEmployes != -1) {
            // Rediriger l'utilisateur vers l'activité appropriée en fonction de son rôle
            int idRole = sharedPreferences.getInt("Id_Role", -1);
            redirectToActivity(idRole, idEmployes);

        }

        edEmail = findViewById(R.id.editTextEmail);
        edPassword = findViewById(R.id.editTextPassword);
        btnConnexion = findViewById(R.id.buttonConnexion);
        textViewSite = findViewById(R.id.textViewSite);



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

        textViewSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers la page Web du projet
                String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/WEB/connexion.html"; // Remplacez par l'URL réelle de votre projet
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
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
            String nomEtablissement = response.getString("NomEtablissement");

            int idEtablissement = response.getInt("Id_Etablissement");



            // Enregistrer les informations de session dans les préférences partagées
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.smartgel_v4.PREFERENCES", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("IdEmployes", idUser);
            editor.putInt("Id_Role", role);
            editor.putInt("Id_Etablissement", idEtablissement);
            editor.putString("Nom", nom);
            editor.putString("Prenom", prenom);
            editor.putString("NomEtablissement", nomEtablissement);
            editor.apply();

            // Planifier l'alarme pour des notifications périodiques
            AlarmScheduler.scheduleRepeatingAlarm(this);

            // Ajout de la gestion de l'optimisation de la batterie
      //      requestBatteryOptimizationExemption();

            // Redirection en fonction du rôle de l'utilisateur
            redirectToActivity(role, idUser);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erreur réponse JSON", Toast.LENGTH_SHORT).show();
            Log.d("API_RESPONSE", "Réponse reçue : " + response.toString());
        }
    }

    private void redirectToActivity(int role, int idUser) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.smartgel_v4.PREFERENCES", MODE_PRIVATE);
        String email = sharedPreferences.getString("Mail", "");
        String nom = sharedPreferences.getString("Nom", "");
        String prenom = sharedPreferences.getString("Prenom", "");
        int idEtablissement = sharedPreferences.getInt("Id_Etablissement", -1);
        String nomEtablissement = sharedPreferences.getString("NomEtablissement", "");
        String adresse = sharedPreferences.getString("Address", "");

        Intent intent;
        switch (role) {
            case 3: // Responsable Technique
                intent = new Intent(LoginActivity.this, EtablissementActivity.class);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("Mail", email);
                intent.putExtra("Nom", nom);
                intent.putExtra("Prenom", prenom);
                intent.putExtra("Id_Role", role);
                startActivity(intent);

                break;
            case 2: // Responsable Agent
                intent = new Intent(LoginActivity.this, ResponsableAgentActivity.class);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("Mail", email);
                intent.putExtra("Nom", nom);
                intent.putExtra("Prenom", prenom);
                intent.putExtra("Id_Etablissement", idEtablissement);
                intent.putExtra("NomEtablissement", nomEtablissement);
                intent.putExtra("Address", adresse);
                intent.putExtra("Id_Role", role);

                startActivity(intent);

                break;
            case 1: // Agent
                intent = new Intent(LoginActivity.this, AgentActivity.class);
                intent.putExtra("IdEmployes", idUser);
                intent.putExtra("Mail", email);
                intent.putExtra("Nom", nom);
                intent.putExtra("Prenom", prenom);
                intent.putExtra("Id_Etablissement", idEtablissement);
                intent.putExtra("NomEtablissement", nomEtablissement);
                intent.putExtra("Address", adresse);
                intent.putExtra("Id_Role", role);
                startActivity(intent);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Rôle non reconnu", Toast.LENGTH_SHORT).show();
                break;
        }
    }
   /* private void requestBatteryOptimizationExemption() {
        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        } else {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
        }
        startActivity(intent);
    }*/

}