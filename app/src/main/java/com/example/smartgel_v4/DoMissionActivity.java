package com.example.smartgel_v4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.ProgressBar;
import android.widget.AdapterView;



public class DoMissionActivity extends AppCompatActivity {
    private static final String API_URL = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Affectation-Mission-Appli.php";
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
    private int idUser;




    private HashMap<String, Integer> userIdMap; // Declare userIdMap here


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_mission);

        userIdMap = new HashMap<>();

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

        // Récupération des informations passées depuis l'activité précédente
        idEtablissement = getIntent().getIntExtra("Id_Etablissement", -1);
        idUserActual = getIntent().getIntExtra("IdEmployes", -1);
        userEmail = getIntent().getStringExtra("Mail");
        userName = getIntent().getStringExtra("Nom");
        userFirstName = getIntent().getStringExtra("Prenom");
        idBorneM = getIntent().getIntExtra("IdBorne", -1);
        gelM = getIntent().getIntExtra("Niveau_Gel", -1);
        batterieM = getIntent().getIntExtra("Niveau_Batterie", -1);
        salleM = getIntent().getStringExtra("Salle");
        heureM = getIntent().getStringExtra("Heure");
        dateM = getIntent().getStringExtra("Date");
        nomEtablissement = getIntent().getStringExtra("NomEtablissement");

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

        // Récupération des missions
        fetchMissions();

        // Récupération des utilisateurs (agents) depuis l'API
        fetchUsers();

        // Gestion du clic sur le bouton "Affectater"
        Button btnAffectation = findViewById(R.id.button_affecter);
        btnAffectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Récupération des valeurs sélectionnées dans les spinners
                User selectedUser = (User) spinnerUsers.getSelectedItem();
                int selectedUserId = selectedUser.getId();
                String selectedMission = spinnerMission.getSelectedItem().toString();
                // Envoi de la requête API POST avec StringRequest
                StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                // Gérer la réponse de l'API si nécessaire
                                Toast.makeText(DoMissionActivity.this, "Affectation effectuée avec succès", Toast.LENGTH_SHORT).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    String jsonResponse = new String(error.networkResponse.data);
                                    String cleanedJsonResponse = jsonResponse.replaceAll("<br", "");
                                    try {
                                        JSONObject jsonObject = new JSONObject(cleanedJsonResponse);
                                        // Handle the parsed JSON object
                                        
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        // Handle parsing errors
                                        Log.e("JSON Parse Error", "Error parsing JSON response: " + cleanedJsonResponse, e);
                                    }
                                } else {
                                    Log.e("API Error", "Erreur : " + error.getMessage(), error);
                                    // Handle API errors if necessary
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("type", selectedMission);
                        params.put("idEmployes", String.valueOf(selectedUserId));
                        params.put("idBorne", String.valueOf(idBorneM));
                        return params;
                    }
                };

                // Ajout de la requête à la file d'attente de Volley
                Volley.newRequestQueue(DoMissionActivity.this).add(stringRequest);
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



    // Méthode pour récupérer les missions
    private void fetchMissions() {
        // Création d'une liste de missions
        List<String> missionsList = new ArrayList<>();
        missionsList.add("Niveau de batterie");
        missionsList.add("Niveau de gel");
        missionsList.add("Niveau de batterie et gel");

        // Adapter pour le spinner des missions
        ArrayAdapter<String> missionsAdapter = new ArrayAdapter<>(DoMissionActivity.this,
                android.R.layout.simple_spinner_item, missionsList);
        missionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMission.setAdapter(missionsAdapter);
    }

    // Méthode pour récupérer les utilisateurs (agents) depuis l'API
    private void fetchUsers() {
        String url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/Employes-Agents-Appli.php?id_etablissement=" + idEtablissement;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("Fetch Users", "Response: " + response.toString());

                            List<User> usersList = new ArrayList<>(); // List to store user objects

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                String name = userObject.getString("Nom");
                                String prenom = userObject.getString("Prenom");
                                idUser = userObject.getInt("IdEmployes");
                                // Combine name and surname for display
                                String fullName = name + " " + prenom;
                                usersList.add(new User(fullName, idUser));

                                // Add user name and corresponding ID to the map
                                userIdMap.put(fullName, idUser);
                            }
                            //code ajouer il faut le tester
                            StringBuilder usersInfo = new StringBuilder();
                            for (User user : usersList) {
                                usersInfo.append("Nom: ").append(user.getFullName()).append(", ");
                                usersInfo.append("IdEmployes: ").append(user.getId()).append("\n");
                            }
                            Log.d("Fetch Users", "Données d'utilisateurs récupérées :\n" + usersInfo.toString());
///jusqu'à ci
                            if (!usersList.isEmpty()) {
                                // Créez un adaptateur personnalisé pour le spinner
                                ArrayAdapter<User> usersAdapter = new ArrayAdapter<User>(DoMissionActivity.this,
                                        android.R.layout.simple_spinner_item, usersList) {
                                    // Override getView() pour afficher le nom complet de l'utilisateur
                                    @NonNull
                                    @Override
                                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                        TextView textView = (TextView) super.getView(position, convertView, parent);
                                        User user = usersList.get(position);
                                        textView.setText(user.getFullName());
                                        return textView;
                                    }

                                    // Override getDropDownView() pour afficher le nom complet de l'utilisateur dans la liste déroulante
                                    @Override
                                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                                        User user = usersList.get(position);
                                        textView.setText(user.getFullName());
                                        return textView;
                                    }
                                };

                                // Définissez l'adaptateur pour le spinner
                                spinnerUsers.setAdapter(usersAdapter);
                                Log.d("Fetch Users", "Spinner Adapter Set with " + usersList.size() + " users");

                                // Ajoute d'un écouteur d'élément sélectionné pour le spinnerUsers
                                spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        User selectedUser = usersList.get(position);
                                        Log.d("Spinner Item Selected", "Selected User: " + selectedUser.getFullName());
                                        int userId = selectedUser.getId(); // Retrieve the Id_Employes from the selected user
                                        Log.d("Spinner Item Selected", "Selected User: " + selectedUser.getFullName() + ", Id_Employes: " + userId);
                                        // You can use userId as needed here
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        Log.d("Spinner Item Selected", "No user selected");
                                    }
                                });
                            } else {
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

        Volley.newRequestQueue(this).add(request);
    }
}