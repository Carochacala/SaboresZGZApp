package com.comenendez.saboreszgz;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargadorDatosFirebase {

    public static void cargarRestaurantesIniciales() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Map<String, Object>> todosLosRestaurantes = new ArrayList<>();

        // ==========================================
        // 🇧🇴 RESTAURANTES DE BOLIVIA
        // ==========================================
        todosLosRestaurantes.add(crearRestaurante(
                "Cibervia", "Bolivia", "https://ejemplo.com/bolivia1.jpg", "09:00-21:00",
                "Calle Delicias 65, 50017. Zaragoza", 41.6525, -0.9020, 4.7,
                Arrays.asList("Salteñas de carne (sin pasitas)", "Sopa de Maní", "Cuñapé calentito")
        ));

        todosLosRestaurantes.add(crearRestaurante(
                "El Rincón Boliviano", "Bolivia", "https://ejemplo.com/bolivia2.jpg", "12:00-16:00, 19:30-23:00",
                "Avenida San José 15, 50008. Zaragoza", 41.6410, -0.8755, 4.5,
                Arrays.asList("Pique Macho", "Majadito de Pollo", "Silpancho")
        ));

        // ==========================================
        // 🇨🇱 RESTAURANTES DE CHILE
        // ==========================================
        todosLosRestaurantes.add(crearRestaurante(
                "La Araucana", "Chile", "https://ejemplo.com/chile1.jpg", "13:00-16:00, 20:00-23:30",
                "Plaza San Francisco, 50006. Zaragoza", 41.6401, -0.8955, 4.4,
                Arrays.asList("Empanadas de Pino (sin pasas)", "Pastel de Choclo", "Completo Italiano")
        ));

        // ==========================================
        // 🇺🇾 RESTAURANTES DE URUGUAY
        // ==========================================
        todosLosRestaurantes.add(crearRestaurante(
                "El Chivito ZGZ", "Uruguay", "https://ejemplo.com/uruguay1.jpg", "13:00-16:30, 20:30-00:00",
                "Calle Luis Bermejo 2, 50009. Zaragoza", 41.6368, -0.8988, 4.8,
                Arrays.asList("Chivito al Plato", "Asado de Tira", "Pamplona de Pollo")
        ));

        // ==========================================
        // 🇲🇽 RESTAURANTES DE MÉXICO
        // ==========================================
        todosLosRestaurantes.add(crearRestaurante(
                "Distrito México", "México", "https://ejemplo.com/mexico1.jpg", "13:30-16:30, 20:30-23:30",
                "Calle José Pellicer Ossau 4, 50003. Zaragoza", 41.6528, -0.8781, 4.6,
                Arrays.asList("Tacos al Pastor", "Enchiladas Suizas", "Guacamole con Totopos")
        ));

        // ==========================================
        // 🇲🇦 RESTAURANTES DE MARRUECOS
        // ==========================================
        todosLosRestaurantes.add(crearRestaurante(
                "Restaurante Marrakech", "Marruecos", "https://ejemplo.com/marruecos1.jpg", "12:00-23:30",
                "Calle San Pablo 30, 50003. Zaragoza", 41.6565, -0.8845, 4.5,
                Arrays.asList("Tajine de Pollo al Limón", "Couscous de Ternera", "Pinchos Morunos")
        ));

        // ==========================================
        // 🇪🇨 RESTAURANTES DE ECUADOR
        // ==========================================
        todosLosRestaurantes.add(crearRestaurante(
                "La Hueca", "Ecuador", "https://ejemplo.com/ecuador1.jpg", "10:00-22:30",
                "Calle Leopoldo Romeo 22, 50002. Zaragoza", 41.6483, -0.8661, 4.4,
                Arrays.asList("Encebollado", "Bolón de Verde", "Llapingachos")
        ));

        // ==========================================
        // BUCLE PARA SUBIR TODOS A FIREBASE
        // ==========================================
        for (Map<String, Object> rest : todosLosRestaurantes) {
            String idDocumento = rest.get("nombre").toString().toLowerCase().replace(" ", "_");

            db.collection("restaurantes").document(idDocumento)
                    .set(rest)
                    .addOnSuccessListener(aVoid -> System.out.println("¡Subido con éxito: " + rest.get("nombre") + "!"))
                    .addOnFailureListener(e -> System.out.println("Error al subir " + rest.get("nombre") + ": " + e.getMessage()));
        }
    }

    //modelo de bbdd firebase
    private static Map<String, Object> crearRestaurante(String nombre, String tipoCocinaPais, String fotoUrl, String horario, String direccion, double latitud, double longitud, double valoracionMedia, List<String> platoDestacado) {
        Map<String, Object> restaurante = new HashMap<>();
        restaurante.put("direccion", direccion);
        restaurante.put("fotoUrl", fotoUrl);
        restaurante.put("horario", horario);
        restaurante.put("nombre", nombre);
        restaurante.put("platoDestacado", platoDestacado);
        restaurante.put("tipoCocinaPais", tipoCocinaPais);
        restaurante.put("ubicacion", new GeoPoint(latitud, longitud));
        restaurante.put("valoracionMedia", valoracionMedia);
        return restaurante;
    }
}