package com.example.smartgel_v4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartgel_v4.NotificationItem;

import java.util.ArrayList;
import java.util.List;


public class NotificationActivity extends AppCompatActivity {

    // Déclaration de la liste de notifications
    private List<NotificationItem> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Récupération des notifications depuis une source dynamique (remplacez cette logique par la vôtre)
        retrieveNotificationsFromSource();

        // Configuration de la RecyclerView pour afficher les notifications
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
    }

    // Méthode pour récupérer les notifications depuis une source dynamique
    private void retrieveNotificationsFromSource() {
        // Remplacez cette logique par la récupération réelle des notifications depuis votre source de données
        // Par exemple, vous pouvez appeler une API pour récupérer les notifications
        // ou interroger une base de données locale
        // Pour cet exemple, je vais simplement ajouter quelques notifications statiques

        addNotification("Nouvelle affectation", "Une nouvelle affectation a été réalisée.");
        addNotification("Notification 2", "Une nouvelle affectation a été arealisé.");
    }

    // Méthode pour ajouter une notification à la liste
    private void addNotification(String title, String message) {
        NotificationItem notification = new NotificationItem(title, message);
        notificationList.add(notification);
    }
}
