package com.comenendez.saboreszgz.data;

import android.util.Log;
import com.comenendez.saboreszgz.model.Restaurante;
import com.comenendez.saboreszgz.model.Usuario;
import com.comenendez.saboreszgz.model.Valoracion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private static FirebaseRepository instance;
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    private final String RESTAURANTES_COLLECTION = "restaurantes";
    private final String USERS_COLLECTION = "usuarios";
    private Usuario currentUser;

    // Constructor privado (patrón Singleton)
    private FirebaseRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    // Obtener la única instancia
    public static synchronized FirebaseRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseRepository();
        }
        return instance;
    }

    // ========== AUTENTICACIÓN ==========

    public FirebaseAuth getAuth() {
        return auth;
    }

    public String getCurrentUserId() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }

    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    // ========== USUARIO ==========

    public void loadCurrentUser() {
        String userId = getCurrentUserId();
        if (userId == null) return;

        db.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUser = documentSnapshot.toObject(Usuario.class);
                        if (currentUser != null) {
                            currentUser.setId(documentSnapshot.getId());
                        }
                        Log.d("Firebase", "Usuario cargado: " + (currentUser != null ? currentUser.getNombre() : "null"));
                    }
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error al cargar usuario", e));
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    // ========== FAVORITOS ==========

    public boolean isFavorite(String restauranteId) {
        if (currentUser == null || currentUser.getFavoritos() == null) return false;
        return currentUser.getFavoritos().contains(restauranteId);
    }

    public void addFavorite(String restauranteId, OnCompleteListener<Void> listener) {
        String userId = getCurrentUserId();
        if (userId == null) return;

        db.collection(USERS_COLLECTION)
                .document(userId)
                .update("favoritos", com.google.firebase.firestore.FieldValue.arrayUnion(restauranteId))
                .addOnCompleteListener(listener);
    }

    public void removeFavorite(String restauranteId, OnCompleteListener<Void> listener) {
        String userId = getCurrentUserId();
        if (userId == null) return;

        db.collection(USERS_COLLECTION)
                .document(userId)
                .update("favoritos", com.google.firebase.firestore.FieldValue.arrayRemove(restauranteId))
                .addOnCompleteListener(listener);
    }

    // ========== RESTAURANTES ==========

    // Obtener todos los restaurantes en tiempo real
    public void getAllRestaurantes(EventListener<QuerySnapshot> listener) {
        db.collection(RESTAURANTES_COLLECTION)
                .orderBy("nombre")
                .addSnapshotListener(listener);
    }
    public void getRestauranteById(String id, EventListener<DocumentSnapshot> listener) {
        db.collection(RESTAURANTES_COLLECTION)
                .document(id)
                .addSnapshotListener(listener);
    }
    // Filtrar restaurantes por tipo de cocina
    public void getRestaurantesByTipo(String tipoCocina, EventListener<QuerySnapshot> listener) {
        db.collection(RESTAURANTES_COLLECTION)
                .whereEqualTo("tipoCocinaPais", tipoCocina)
                .addSnapshotListener(listener);
    }

    // ========== VALORACIONES ==========

    private final String VALORACIONES_COLLECTION = "valoraciones";

    // Añadir valoración
    public void addValoracion(String restauranteId, int puntuacion, String comentario, OnCompleteListener<Void> listener) {
        String usuarioId = getCurrentUserId();
        if (usuarioId == null) {
            if (listener != null) listener.onComplete(null);
            return;
        }

        // Obtener nombre del usuario
        db.collection(USERS_COLLECTION).document(usuarioId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String nombreUsuario = documentSnapshot.getString("nombre");
                    if (nombreUsuario == null) nombreUsuario = "Usuario";

                    Valoracion valoracion = new Valoracion(usuarioId, restauranteId, puntuacion, comentario, nombreUsuario);

                    db.collection(VALORACIONES_COLLECTION)
                            .add(valoracion)
                            .addOnSuccessListener(documentReference -> {
                                actualizarValoracionMedia(restauranteId);
                                if (listener != null) listener.onComplete(null);
                            })
                            .addOnFailureListener(e -> {
                                if (listener != null) listener.onComplete(null);
                            });
                });
    }

    // Obtener valoraciones de un restaurante
    public void getValoracionesByRestaurante(String restauranteId, EventListener<QuerySnapshot> listener) {
        db.collection(VALORACIONES_COLLECTION)
                .whereEqualTo("restauranteId", restauranteId)
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener(listener);
    }

    // Verificar si el usuario ya valoró este restaurante
    public void getUserValoracion(String restauranteId, OnCompleteListener<QuerySnapshot> listener) {
        String usuarioId = getCurrentUserId();
        if (usuarioId == null) return;

        db.collection(VALORACIONES_COLLECTION)
                .whereEqualTo("usuarioId", usuarioId)
                .whereEqualTo("restauranteId", restauranteId)
                .limit(1)
                .get()
                .addOnCompleteListener(listener);
    }

    // Actualizar valoración media del restaurante
    private void actualizarValoracionMedia(String restauranteId) {
        db.collection(VALORACIONES_COLLECTION)
                .whereEqualTo("restauranteId", restauranteId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    double suma = 0;
                    int count = 0;

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Integer puntuacion = doc.getLong("puntuacion").intValue();
                        suma += puntuacion;
                        count++;
                    }

                    double media = count > 0 ? suma / count : 0;

                    db.collection(RESTAURANTES_COLLECTION)
                            .document(restauranteId)
                            .update("valoracionMedia", media);
                });
    }
}