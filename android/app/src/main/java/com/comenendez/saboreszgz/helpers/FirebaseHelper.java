package com.comenendez.saboreszgz.helpers;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.comenendez.saboreszgz.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseHelper {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    // ============================================================
    // AUTENTICACIÓN
    // ============================================================

    /**
     * Obtiene el usuario actual logueado
     * @return FirebaseUser o null si no hay sesión
     */
    public static FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    /**
     * Obtiene el ID del usuario actual
     * @return String con el UID o null
     */
    public static String getCurrentUserId() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }

    /**
     * Obtiene el email del usuario actual
     * @return String con el email o null
     */
    public static String getUserEmail() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;
    }

    /**
     * Verifica si hay un usuario logueado
     * @return true si hay sesión activa
     */
    public static boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    /**
     * Cierra la sesión del usuario actual
     */
    public static void logout() {
        auth.signOut();
    }

    /**
     * Cierra sesión y redirige a la pantalla de login
     * @param activity Actividad desde la que se llama
     */
    public static void logoutAndRedirect(AppCompatActivity activity) {
        auth.signOut();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Muestra un mensaje si el usuario no está logueado
     * @param activity Actividad desde la que se llama
     * @return true si no está logueado
     */
    public static boolean checkLoginAndWarn(AppCompatActivity activity) {
        if (!isUserLoggedIn()) {
            Toast.makeText(activity, "Inicia sesión para acceder a esta función", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}