package com.comenendez.saboreszgz.data;

import android.util.Log;
import com.comenendez.saboreszgz.model.Restaurante;
import com.comenendez.saboreszgz.model.Usuario;
import com.comenendez.saboreszgz.model.Valoracion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void getFavoritosIdsFromFirestore(OnFavoritosLoadedListener listener) {
        String userId = getCurrentUserId();
        android.util.Log.d("FAVORITOS", "getFavoritosIdsFromFirestore - userId: " + userId);

        if (userId == null) {
            android.util.Log.d("FAVORITOS", "userId es null");
            listener.onLoaded(new ArrayList<>());
            return;
        }

        db.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    android.util.Log.d("FAVORITOS", "Documento obtenido, exists: " + documentSnapshot.exists());
                    if (documentSnapshot.exists()) {
                        List<String> favoritos = (List<String>) documentSnapshot.get("favoritos");
                        android.util.Log.d("FAVORITOS", "favoritos del documento: " + (favoritos != null ? favoritos.toString() : "null"));
                        if (favoritos != null) {
                            listener.onLoaded(favoritos);
                        } else {
                            listener.onLoaded(new ArrayList<>());
                        }
                    } else {
                        listener.onLoaded(new ArrayList<>());
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.d("FAVORITOS", "Error: " + e.getMessage());
                    listener.onLoaded(new ArrayList<>());
                });
    }
    public void getFavoritosDelUsuario(String userId, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnCompleteListener(listener);
    }
    public interface OnFavoritosLoadedListener {
        void onLoaded(List<String> favoritos);
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
    // Obtener restaurantes favoritos por lista de IDs
    public void getFavoritosRestaurantes(List<String> ids, EventListener<QuerySnapshot> listener) {
        if (ids == null || ids.isEmpty()) {
            listener.onEvent(null, null);
            return;
        }

        db.collection(RESTAURANTES_COLLECTION)
                .whereIn(FieldPath.documentId(), ids)
                .addSnapshotListener(listener);
    }

    public List<String> getFavoriteIds() {
        if (currentUser == null || currentUser.getFavoritos() == null) {
            return new ArrayList<>();
        }
        return currentUser.getFavoritos();
    }
    public void loadCurrentUser(Runnable onComplete) {
        String userId = getCurrentUserId();
        if (userId == null) {
            if (onComplete != null) onComplete.run();
            return;
        }

        db.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUser = documentSnapshot.toObject(Usuario.class);
                        if (currentUser != null) {
                            currentUser.setId(documentSnapshot.getId());
                        }
                    }
                    if (onComplete != null) onComplete.run();
                })
                .addOnFailureListener(e -> {
                    if (onComplete != null) onComplete.run();
                });
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

    // Actualizar valoración existente
    public void updateValoracion(String valoracionId, int nuevaPuntuacion, String nuevoComentario, OnCompleteListener<Void> listener) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("puntuacion", nuevaPuntuacion);
        updates.put("comentario", nuevoComentario);
        updates.put("fecha", com.google.firebase.firestore.FieldValue.serverTimestamp());

        db.collection(VALORACIONES_COLLECTION)
                .document(valoracionId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Obtener restauranteId para actualizar la media
                    db.collection(VALORACIONES_COLLECTION)
                            .document(valoracionId)
                            .get()
                            .addOnSuccessListener(doc -> {
                                String restauranteId = doc.getString("restauranteId");
                                if (restauranteId != null) {
                                    actualizarValoracionMedia(restauranteId);
                                }
                                if (listener != null) listener.onComplete(null);
                            })
                            .addOnFailureListener(e -> {
                                if (listener != null) listener.onComplete(null);
                            });
                })
                .addOnFailureListener(e -> {
                    if (listener != null) listener.onComplete(null);
                });
    }

    // Eliminar valoración
    public void deleteValoracion(String valoracionId, OnCompleteListener<Void> listener) {
        android.util.Log.d("ELIMINAR", "deleteValoracion llamado con ID: " + valoracionId);

        db.collection(VALORACIONES_COLLECTION)
                .document(valoracionId)
                .get()
                .addOnSuccessListener(doc -> {
                    android.util.Log.d("ELIMINAR", "Documento encontrado: " + doc.exists());
                    String restauranteId = doc.getString("restauranteId");
                    android.util.Log.d("ELIMINAR", "restauranteId: " + restauranteId);

                    db.collection(VALORACIONES_COLLECTION)
                            .document(valoracionId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                android.util.Log.d("ELIMINAR", "Documento eliminado correctamente");
                                if (restauranteId != null) {
                                    actualizarValoracionMedia(restauranteId);
                                }
                                if (listener != null) listener.onComplete(null);
                            })
                            .addOnFailureListener(e -> {
                                android.util.Log.d("ELIMINAR", "Error al eliminar: " + e.getMessage());
                                if (listener != null) listener.onComplete(null);
                            });
                })
                .addOnFailureListener(e -> {
                    android.util.Log.d("ELIMINAR", "Error al obtener documento: " + e.getMessage());
                    if (listener != null) listener.onComplete(null);
                });
    }



}