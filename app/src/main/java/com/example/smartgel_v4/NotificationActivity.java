package com.example.smartgel_v4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    // Déclaration de la liste de notifications
    private List<NotificationItem> notificationList = new ArrayList<>();
    private int roleId; // Variable pour stocker l'identifiant de l'utilisateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Récupération de l'identifiant de l'utilisateur depuis l'intent ou d'une session

        roleId = getIntent().getIntExtra("Id_Role", -1);




        // Configuration de la RecyclerView pour afficher les notifications
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NotificationAdapter adapter = new NotificationAdapter(getUserNotifications(roleId));
        recyclerView.setAdapter(adapter);


        findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Utilisation de la méthode onBackPressed() pour revenir en arrière
            }
        });

        // Configuration du bouton de rafraîchissement
        Button refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshNotifications();
            }
        });
    }

    // Méthode pour récupérer les notifications spécifiques à un utilisateur
    private List<NotificationItem> getUserNotifications(int roleId) {
        // Logique pour récupérer les notifications en fonction de role de l'utilisateur
        // Ceci est un exemple, vous devez adapter cette méthode à votre logique d'application
        List<NotificationItem> userNotifications = new ArrayList<>();

        if (roleId == 1) {
            // Pour les Responsables Agent, afficher uniquement les notifications avec le titre "Vous avez une mission à réaliser"
            List<NotificationItem> savedNotifications = NotificationUtils.getSavedNotifications(this);
            for (NotificationItem notification : savedNotifications) {
                if (notification.getTitle().equals("Une nouvelle affectation a été créée.")) {
                    Log.d("NotificationActivity", "Notification mission: Title = " + notification.getTitle() + ", Message = " + notification.getMessage());
                    userNotifications.add(notification);
                }
            }
        } else if (roleId == 2) {
            // Pour les Responsables Technique, afficher uniquement les notifications avec le titre "Alerte, niveau de gel au batterie faible"
            List<NotificationItem> savedNotifications = NotificationUtils.getSavedNotifications(this);
            for (NotificationItem notification : savedNotifications) {
                Log.d("NotificationActivity", "Notification alerte: Title = " + notification.getTitle() + ", Message = " + notification.getMessage());
                if (notification.getTitle().equals("Alerte")) {
                    userNotifications.add(notification);
                }
            }
        } else {
            // Pour les autres utilisateurs, afficher toutes les notifications
            userNotifications.addAll(NotificationUtils.getSavedNotifications(this));
        }
        return userNotifications;
    }

    // Méthode pour rafraîchir les notifications
    private void refreshNotifications() {
        // Rafraîchir la liste des notifications en fonction de l'identifiant de l'utilisateur
        NotificationAdapter adapter = new NotificationAdapter(getUserNotifications(roleId));
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
    }
}
