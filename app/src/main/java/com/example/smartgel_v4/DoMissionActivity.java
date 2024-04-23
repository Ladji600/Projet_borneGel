package com.example.smartgel_v4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DoMissionActivity extends AppCompatActivity {

    private Spinner spinnerMission, spinnerUsers;
    private int idEtablissement;
    private int idBorneM;
    private int idUserActual;
    private String userEmail;
    private String userName;
    private String userFirstName;
    private String heureM;
    private String dateM;
    private int gelM;
    private int batterieM;
    private String salleM;
    private String nomEtablissement;

 //   private List<NotificationItem> notificationList = new ArrayList<>();
 //   private NotificationAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_mission);

        // Initialisation des spinners pour les missions et les utilisateurs
        spinnerMission = findViewById(R.id.spinner_mission);
        spinnerUsers = findViewById(R.id.spinner_users);

        // Gestion du retour en arrière lorsque l'image est cliquée
        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Utilisation de la méthode onBackPressed() pour revenir en arrière
            }
        });
        // Initialisation de la RecyclerView pour les notifications
   /*     RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
*/
        // Récupération des informations passées depuis l'activité précédente
        idEtablissement = getIntent().getIntExtra("idEtablissement", -1);
        idUserActual = getIntent().getIntExtra("idUser", -1);
        userEmail = getIntent().getStringExtra("email");
        userName = getIntent().getStringExtra("nom");
        userFirstName = getIntent().getStringExtra("prenom");
        idBorneM = getIntent().getIntExtra("idBorne", -1);
        gelM = getIntent().getIntExtra("gel", -1);
        batterieM = getIntent().getIntExtra("batterie", -1);
        salleM = getIntent().getStringExtra("salle");
        heureM = getIntent().getStringExtra("heure");
        dateM = getIntent().getStringExtra("date");
        nomEtablissement = getIntent().getStringExtra("Etablissement");

        // Mise à jour des TextView avec les données récupérées
        TextView textEtablissement = findViewById(R.id.text_etablissement);
        textEtablissement.setText("Etablissement : " + nomEtablissement);

        TextView textIdEtablissement = findViewById(R.id.text_id_etablissement);
        textIdEtablissement.setText("ID Etablissement : " + idEtablissement);

        TextView textSalle = findViewById(R.id.text_salle);
        textSalle.setText("Salle : " + salleM);

        TextView textIdBorne = findViewById(R.id.text_id_borne);
        textIdBorne.setText("Id Borne : " + idBorneM);

        TextView userNameTextView = findViewById(R.id.text_responsable_agent);
        String fullName = userName + " " + userFirstName;
        userNameTextView.setText(fullName);

        // Récupération des missions depuis l'API
        fetchMissions();

        // Récupération des utilisateurs (agents) depuis l'API
        fetchUsers();

        // Gestion du clic sur le bouton "Affectater"
        Button btnAffectation = findViewById(R.id.button_affecter);
        btnAffectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des valeurs sélectionnées dans les spinners
                String selectedMission = spinnerMission.getSelectedItem().toString();
                String selectedUser = spinnerUsers.getSelectedItem().toString();

            /*    //envoie de mail
                String recipientEmail = "recipient@example.com"; // Adresse e-mail du destinataire
                String subject = "Nouvelle affectation réalisée";
                String message = "Une nouvelle affectation a été réalisée. Veuillez vérifier.";

                EmailSender.sendEmail(recipientEmail, subject, message);
*/
                // Construction de l'objet JSON pour la requête API POST
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("email", selectedUser);
                    requestBody.put("idborne", idBorneM); // Ajoutez l'ID de la borne correct ici
                    requestBody.put("etablissement", nomEtablissement);
                    requestBody.put("idEtablissment", idEtablissement);
                    requestBody.put("salle", salleM);
                    requestBody.put("mission", selectedMission);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Envoi de la requête API POST
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        "https://804b3669-1a04-43a0-8a07-09a076ab6c78.mock.pstmn.io/affecterMission",
                        requestBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Gérer la réponse de l'API si nécessaire
                                Toast.makeText(DoMissionActivity.this, "Affectation effectuée avec succès", Toast.LENGTH_SHORT).show();

                                // Envoyer la notification
                                NotificationHelper.sendNotification(DoMissionActivity.this, "Nouvelle affectation", "Une nouvelle affectation a été réalisée.");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("API Error", "Erreur : " + error.getMessage(), error);
                                // Gérer les erreurs de l'API si nécessaire
                            }
                        });

                // Ajout de la requête à la file d'attente de Volley
                Volley.newRequestQueue(DoMissionActivity.this).add(jsonObjectRequest);
            }
        });

        // Gestion du clic sur le bouton de retour
        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Utilisation de la méthode onBackPressed() pour revenir en arrière
            }
        });
    }


// Méthode pour ajouter une notification à la liste
 /*   private void addNotification(String title, String message) {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }*/
    // Méthode pour récupérer les missions depuis l'API
    private void fetchMissions() {
        // Création d'une liste de missions
        List<String> missionsList = new ArrayList<>();
        missionsList.add("Changer batterie");
        missionsList.add("Charger gel");
        missionsList.add("Changer batterie et gel");

        // Adapter pour le spinner des missions
        ArrayAdapter<String> missionsAdapter = new ArrayAdapter<>(DoMissionActivity.this,
                android.R.layout.simple_spinner_item, missionsList);
        missionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMission.setAdapter(missionsAdapter);
    }

    // Méthode pour récupérer les utilisateurs (agents) depuis l'API
    private void fetchUsers() {
        // URL de l'API pour récupérer les utilisateurs (agents)
        String url = "https://804b3669-1a04-43a0-8a07-09a076ab6c78.mock.pstmn.io/employes?idEtablissement=" + idEtablissement;

        // Requête JSON pour récupérer les utilisateurs (agents)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray usersArray = response.getJSONArray("user");
                            Log.d("Fetch Users", "Response: " + response.toString());

                            // Liste pour stocker les emails des utilisateurs (agents) ayant le rôle 1
                            List<String> usersList = new ArrayList<>();

                            // Parcours des utilisateurs et ajout à la liste si leur rôle est 1 (agent)
                            for (int i = 0; i < usersArray.length(); i++) {
                                JSONObject userObject = usersArray.getJSONObject(i);
                                int role = userObject.getInt("Id_Role");
                                if (role == 1) {
                                    String userEmail = userObject.getString("Mail");
                                    usersList.add(userEmail);
                                }
                            }
                            // Ajouter un log pour vérifier les données d'utilisateurs récupérées
                            Log.d("Fetch Users", "Données d'utilisateurs récupérées : " + usersList.toString());

                            // Vérifiez si des données d'utilisateurs ont été récupérées
                            if (!usersList.isEmpty()) {
                                // Adapter pour le spinner des utilisateurs
                                ArrayAdapter<String> usersAdapter = new ArrayAdapter<>(DoMissionActivity.this,
                                        android.R.layout.simple_spinner_item, usersList);
                                usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerUsers.setAdapter(usersAdapter);
                            } else {
                                // Gérer le cas où aucune donnée d'utilisateur n'est disponible
                                Log.d("Fetch Users", "Aucun utilisateur disponible");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Erreur : " + error.getMessage(), error);
                    }
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}