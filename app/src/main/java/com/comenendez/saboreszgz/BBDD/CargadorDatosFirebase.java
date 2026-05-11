package com.comenendez.saboreszgz.BBDD;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargadorDatosFirebase {

    public static void cargarRestaurantesIniciales() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Map<String, Object>> todosLosRestaurantes = new ArrayList<>();

        // 🇨🇴 COLOMBIA
        todosLosRestaurantes.add(crearRestaurante("La Patacona", "Colombia", "url_colombia1.jpg", "12:00-23:00", "Calle del Heroísmo 4, Zaragoza", 41.6501, -0.8820, 4.6, Arrays.asList("Bandeja Paisa", "Arepa de Choclo", "Sancocho")));
        todosLosRestaurantes.add(crearRestaurante("El Rincón Paisa", "Colombia", "url_colombia2.jpg", "13:00-22:30", "Avenida Goya 30, Zaragoza", 41.6425, -0.8901, 4.4, Arrays.asList("Empanadas Colombianas", "Ajiaco", "Patacones con Hogao")));

        // 🇧🇴 BOLIVIA
        todosLosRestaurantes.add(crearRestaurante("Cibervia", "Bolivia", "url_bolivia1.jpg", "09:00-21:00", "Calle Delicias 65, Zaragoza", 41.6525, -0.9020, 4.7, Arrays.asList("Salteñas de carne (sin pasitas)", "Sopa de Maní", "Cuñapé")));
        todosLosRestaurantes.add(crearRestaurante("El Rincón Boliviano", "Bolivia", "url_bolivia2.jpg", "12:00-23:00", "Avenida San José 15, Zaragoza", 41.6410, -0.8755, 4.5, Arrays.asList("Pique Macho", "Majadito de Pollo", "Silpancho")));

        // 🇲🇦 MARRUECOS
        todosLosRestaurantes.add(crearRestaurante("Marrakech ZGZ", "Marruecos", "url_marruecos1.jpg", "12:00-23:30", "Calle San Pablo 30, Zaragoza", 41.6565, -0.8845, 4.5, Arrays.asList("Tajine de Pollo al Limón", "Couscous de Verduras", "Pinchos Morunos")));
        todosLosRestaurantes.add(crearRestaurante("La Medina", "Marruecos", "url_marruecos2.jpg", "13:00-00:00", "Calle Mayor 12, Zaragoza", 41.6530, -0.8750, 4.7, Arrays.asList("Zaalouk", "Harira", "Briouats de carne")));

        // 🇲🇽 MÉXICO
        todosLosRestaurantes.add(crearRestaurante("Distrito México", "México", "url_mexico1.jpg", "13:30-23:30", "Calle José Pellicer 4, Zaragoza", 41.6528, -0.8781, 4.6, Arrays.asList("Tacos al Pastor", "Enchiladas Suizas", "Guacamole")));
        todosLosRestaurantes.add(crearRestaurante("La Cantina de Frida", "México", "url_mexico2.jpg", "13:00-00:00", "Plaza Santa Cruz, Zaragoza", 41.6535, -0.8770, 4.8, Arrays.asList("Fajitas de Pollo", "Quesadillas", "Nachos con Queso")));

        // 🇮🇹 ITALIA
        todosLosRestaurantes.add(crearRestaurante("La Mafia", "Italia", "url_italia1.jpg", "13:30-16:00, 20:30-23:30", "Plaza Aragón 3, Zaragoza", 41.6480, -0.8850, 4.3, Arrays.asList("Pizza Margarita", "Pasta Carbonara Original", "Tiramisú")));
        todosLosRestaurantes.add(crearRestaurante("Trattoria del Centro", "Italia", "url_italia2.jpg", "13:00-23:00", "Calle Alfonso I, Zaragoza", 41.6545, -0.8785, 4.7, Arrays.asList("Lasaña de Carne", "Risotto de Setas", "Panna Cotta")));

        // 🇯🇵 JAPÓN
        todosLosRestaurantes.add(crearRestaurante("Sakura", "Japón", "url_japon1.jpg", "13:00-16:30, 20:00-23:30", "Paseo de la Mina 5, Zaragoza", 41.6475, -0.8760, 4.5, Arrays.asList("Nigiri de Salmón", "Ramen Tonkotsu", "Gyozas")));
        todosLosRestaurantes.add(crearRestaurante("Koto Sushi", "Japón", "url_japon2.jpg", "13:30-23:30", "Calle Francisco de Vitoria, Zaragoza", 41.6430, -0.8825, 4.8, Arrays.asList("Sashimi Variado", "Udon de Ternera", "Mochis de Té Verde")));

        // 🇺🇸 EE UU
        todosLosRestaurantes.add(crearRestaurante("Foster's Hollywood", "EE UU", "url_eeuu1.jpg", "13:00-00:00", "Centro Comercial GranCasa, Zaragoza", 41.6665, -0.8920, 4.1, Arrays.asList("Costillas BBQ", "Bacon Cheese Burger", "Aros de Cebolla")));
        todosLosRestaurantes.add(crearRestaurante("Tommy Mel's", "EE UU", "url_eeuu2.jpg", "13:00-23:30", "Calle Azoque 60, Zaragoza", 41.6495, -0.8840, 4.2, Arrays.asList("Classic Diner Burger", "Batido de Vainilla", "Mac & Cheese")));

        // 🇮🇳 INDIA
        todosLosRestaurantes.add(crearRestaurante("Taj Mahal", "India", "url_india1.jpg", "12:30-16:00, 19:30-23:30", "Calle Maestro Marquina 12, Zaragoza", 41.6415, -0.8860, 4.6, Arrays.asList("Chicken Tikka Masala", "Pan Naan", "Samosas Veganas")));
        todosLosRestaurantes.add(crearRestaurante("Curry y Canela", "India", "url_india2.jpg", "13:00-23:00", "Calle San Antonio María Claret, Zaragoza", 41.6450, -0.8950, 4.5, Arrays.asList("Cordero Rogan Josh", "Arroz Basmati", "Pollo Tandoori")));

        // 🇨🇳 CHINA
        todosLosRestaurantes.add(crearRestaurante("Palacio Chino", "China", "url_china1.jpg", "12:00-16:30, 20:00-00:00", "Avenida Madrid 120, Zaragoza", 41.6505, -0.9050, 4.0, Arrays.asList("Pato Asado", "Arroz Tres Delicias", "Rollitos de Primavera")));
        todosLosRestaurantes.add(crearRestaurante("Gran Muralla", "China", "url_china2.jpg", "12:30-23:30", "Calle Corona de Aragón 40, Zaragoza", 41.6420, -0.8980, 4.2, Arrays.asList("Cerdo Agridulce", "Fideos Fritos con Ternera", "Dumplings")));

        // 🇹🇷 TURQUÍA
        todosLosRestaurantes.add(crearRestaurante("Estambul Kebab", "Turquía", "url_turquia1.jpg", "12:00-01:00", "Calle Conde Aranda 50, Zaragoza", 41.6545, -0.8885, 4.3, Arrays.asList("Döner Kebab de Ternera", "Lahmacun (Pizza Turca)", "Baklava (con nueces)")));
        todosLosRestaurantes.add(crearRestaurante("Capadocia", "Turquía", "url_turquia2.jpg", "13:00-00:00", "Calle Don Jaime I 20, Zaragoza", 41.6538, -0.8765, 4.5, Arrays.asList("Iskender Kebab", "Falafel", "Hummus")));

        // 🇻🇪 VENEZUELA
        todosLosRestaurantes.add(crearRestaurante("La Arepería", "Venezuela", "url_venezuela1.jpg", "11:00-23:00", "Calle Cervantes 8, Zaragoza", 41.6455, -0.8835, 4.8, Arrays.asList("Arepa Reina Pepiada", "Tequeños", "Pabellón Criollo")));
        todosLosRestaurantes.add(crearRestaurante("El Rincón Venezolano", "Venezuela", "url_venezuela2.jpg", "12:30-22:30", "Calle Santa Inés 2, Zaragoza", 41.6570, -0.8890, 4.7, Arrays.asList("Cachapa con Queso", "Hallaca (sin pasas)", "Empanada de Cazón")));

        // 🇬🇷 GRECIA
        todosLosRestaurantes.add(crearRestaurante("Taverna Griega", "Grecia", "url_grecia1.jpg", "13:00-16:00, 20:00-23:30", "Calle Méndez Núñez 15, Zaragoza", 41.6530, -0.8790, 4.6, Arrays.asList("Moussaka", "Gyros de Cerdo", "Ensalada Feta")));
        todosLosRestaurantes.add(crearRestaurante("Olimpo", "Grecia", "url_grecia2.jpg", "13:30-23:00", "Plaza San Pedro Nolasco, Zaragoza", 41.6515, -0.8745, 4.5, Arrays.asList("Souvlaki de Pollo", "Tzatziki", "Spanakopita")));

        // 🇨🇱 CHILE
        todosLosRestaurantes.add(crearRestaurante("La Araucana", "Chile", "url_chile1.jpg", "13:00-23:30", "Plaza San Francisco, Zaragoza", 41.6401, -0.8955, 4.4, Arrays.asList("Empanadas de Pino (sin pasas)", "Pastel de Choclo", "Completo Italiano")));
        todosLosRestaurantes.add(crearRestaurante("Sabor Andino", "Chile", "url_chile2.jpg", "12:30-22:30", "Calle de los Diputados, Zaragoza", 41.6562, -0.8930, 4.5, Arrays.asList("Chorrillana", "Cazuela de Ave", "Mote con Huesillo")));

        // 🇺🇾 URUGUAY
        todosLosRestaurantes.add(crearRestaurante("El Chivito ZGZ", "Uruguay", "url_uruguay1.jpg", "13:00-00:00", "Calle Luis Bermejo 2, Zaragoza", 41.6368, -0.8988, 4.8, Arrays.asList("Chivito al Plato", "Asado de Tira", "Pamplona de Pollo")));
        todosLosRestaurantes.add(crearRestaurante("La Parrilla Charrua", "Uruguay", "url_uruguay2.jpg", "13:00-23:30", "Avenida Tenor Fleta 40, Zaragoza", 41.6405, -0.8810, 4.7, Arrays.asList("Matambre a la Pizza", "Choripán", "Dulce de Leche")));

        // 🇪🇨 ECUADOR
        todosLosRestaurantes.add(crearRestaurante("La Hueca", "Ecuador", "url_ecuador1.jpg", "10:00-22:30", "Calle Leopoldo Romeo 22, Zaragoza", 41.6483, -0.8661, 4.4, Arrays.asList("Encebollado", "Bolón de Verde", "Llapingachos")));
        todosLosRestaurantes.add(crearRestaurante("Mitad del Mundo", "Ecuador", "url_ecuador2.jpg", "11:00-23:00", "Calle Boggiero 55, Zaragoza", 41.6580, -0.8870, 4.5, Arrays.asList("Ceviche de Camarón", "Fritada de Chancho", "Seco de Pollo")));

        // BUCLE PARA SUBIR TODOS A FIREBASE
        for (Map<String, Object> rest : todosLosRestaurantes) {
            String idDocumento = rest.get("nombre").toString().toLowerCase().replace(" ", "_");

            db.collection("restaurantes").document(idDocumento)
                    .set(rest)
                    .addOnSuccessListener(aVoid -> System.out.println("¡Subido con éxito: " + rest.get("nombre") + "!"))
                    .addOnFailureListener(e -> System.out.println("Error al subir " + rest.get("nombre") + ": " + e.getMessage()));
        }
    }

    // GEOPOINT PARA UBICACION,tmb en detallerestaurante.java
    private static Map<String, Object> crearRestaurante(String nombre, String tipoCocinaPais, String fotoUrl, String horario, String direccion, double latitud, double longitud, double valoracionMedia, List<String> platoDestacado) {
        Map<String, Object> restaurante = new HashMap<>();
        restaurante.put("direccion", direccion);
        restaurante.put("fotoUrl", fotoUrl);
        restaurante.put("horario", horario);
        restaurante.put("nombre", nombre);
        restaurante.put("platoDestacado", platoDestacado);
        restaurante.put("tipoCocinaPais", tipoCocinaPais);

        // Creamos el GeoPoint nativo de Firebase usando la latitud y longitud
        restaurante.put("ubicacion", new com.google.firebase.firestore.GeoPoint(latitud, longitud));

        restaurante.put("valoracionMedia", valoracionMedia);
        return restaurante;
    }
}