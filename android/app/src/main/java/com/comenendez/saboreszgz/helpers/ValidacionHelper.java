package com.comenendez.saboreszgz.helpers;

import android.text.TextUtils;
import java.util.regex.Pattern;

/**
 * Clase helper para validaciones de formularios
 */
public class ValidacionHelper {

    // ============================================================
    // VALIDACIÓN DE EMAIL
    // ============================================================
    /**
     * Valida si el email tiene un formato correcto
     * @param email Email a validar
     * @return true si el email es válido, false en caso contrario
     */
    public static boolean isEmailValido(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        // Patrón básico para validar email
        Pattern pattern = Pattern.compile(
                "^[A-Za-z0-9+_.-]+@(.+)$"
        );
        return pattern.matcher(email).matches();
    }

    // ============================================================
    // VALIDACIÓN DE CONTRASEÑA
    // ============================================================
    /**
     * Valida si la contraseña tiene al menos 6 caracteres
     * @param password Contraseña a validar
     * @return true si la contraseña es válida, false en caso contrario
     */
    public static boolean isPasswordValida(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    // ============================================================
    // VALIDACIÓN DE CAMPO VACÍO
    // ============================================================
    /**
     * Verifica si un campo de texto está vacío
     * @param texto Texto a verificar
     * @return true si está vacío, false si tiene contenido
     */
    public static boolean isCampoVacio(String texto) {
        return TextUtils.isEmpty(texto) || texto.trim().isEmpty();
    }

    // ============================================================
    // VALIDACIÓN DE PUNTUACIÓN (VALORACIONES)
    // ============================================================
    /**
     * Verifica si la puntuación está entre 1 y 5
     * @param puntuacion Puntuación a validar (1-5)
     * @return true si es válida, false en caso contrario
     */
    public static boolean isPuntuacionValida(int puntuacion) {
        return puntuacion >= 1 && puntuacion <= 5;
    }

    // ============================================================
    // VALIDACIÓN DE NOMBRE
    // ============================================================
    /**
     * Verifica si el nombre tiene al menos 2 caracteres
     * @param nombre Nombre a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean isNombreValido(String nombre) {
        return !TextUtils.isEmpty(nombre) && nombre.trim().length() >= 2;
    }
}