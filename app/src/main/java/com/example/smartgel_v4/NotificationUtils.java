package com.example.smartgel_v4;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationUtils {

    // Pour enregistrer la notification
    public static void saveNotification(Context context, String title, String message) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.smartgel_v4.NOTIFICATIONS", Context.MODE_PRIVATE);
        Log.d("NotificationUtils", "Saving notification: Title = " + title + ", Message = " + message);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int count = sharedPreferences.getInt("NotificationCount", 0);
        editor.putString("NotificationTitle_" + count, title);
        editor.putString("NotificationMessage_" + count, message);
        editor.putInt("NotificationCount", count + 1);
        editor.apply();
    }

    // Pour récupérer les notifications enregistrées
    public static List<NotificationItem> getSavedNotifications(Context context) {
        List<NotificationItem> savedNotifications = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.smartgel_v4.NOTIFICATIONS", Context.MODE_PRIVATE);
        int count = sharedPreferences.getInt("NotificationCount", 0);
        for (int i = 0; i < count; i++) {
            String title = sharedPreferences.getString("NotificationTitle_" + i, "");
            String message = sharedPreferences.getString("NotificationMessage_" + i, "");
            savedNotifications.add(new NotificationItem(title, message));
        }
        return savedNotifications;
    }
}
