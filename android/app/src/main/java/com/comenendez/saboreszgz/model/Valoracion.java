package com.comenendez.saboreszgz.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class Valoracion {
    private String usuarioId;
    private String restauranteId;
    private int puntuacion;
    private String comentario;
    private Timestamp fecha;
    private String nombreUsuario;

    // Constructor vacío (necesario para Firestore)
    public Valoracion() {
    }

    @Exclude  // ← Agrega esto
    private String id;  // Firestore ignorará este campo

    // Constructor con parámetros
    public Valoracion(String usuarioId, String restauranteId, int puntuacion, String comentario, String nombreUsuario) {
        this.usuarioId = usuarioId;
        this.restauranteId = restauranteId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.nombreUsuario = nombreUsuario;
        this.fecha = Timestamp.now();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(String restauranteId) {
        this.restauranteId = restauranteId;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}