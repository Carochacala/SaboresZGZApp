package com.comenendez.saboreszgz.helpers;

import android.content.Context;
import android.widget.Toast;

/**
 * Helper para mostrar mensajes Toast de forma uniforme
 */
public class ToastHelper {

    /**
     * Muestra un mensaje corto normal
     */
    public static void mostrarMensaje(Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

    /**
     * Muestra un mensaje largo
     */
    public static void mostrarMensajeLargo(Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    /**
     * Muestra un mensaje de éxito (con emoji ✅)
     */
    public static void mostrarExito(Context context, String mensaje) {
        Toast.makeText(context, "✅ " + mensaje, Toast.LENGTH_SHORT).show();
    }

    /**
     * Muestra un mensaje de error (con emoji ❌)
     */
    public static void mostrarError(Context context, String mensaje) {
        Toast.makeText(context, "❌ " + mensaje, Toast.LENGTH_SHORT).show();
    }

    /**
     * Muestra un mensaje de advertencia (con emoji ⚠️)
     */
    public static void mostrarAdvertencia(Context context, String mensaje) {
        Toast.makeText(context, "⚠️ " + mensaje, Toast.LENGTH_SHORT).show();
    }
}