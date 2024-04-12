package com.example.smartgel_v4;

import android.content.Context;
import android.content.Intent;

public class SessionManager {

    public static void logout(Context context) {
        // Créer un Intent pour démarrer l'activité de connexion
        Intent intent = new Intent(context, LoginActivity.class);
        // Effacer toutes les activités précédentes de la pile d'activités
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Démarrer l'activité de connexion
        context.startActivity(intent);
    }
}
