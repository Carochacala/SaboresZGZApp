package com.comenendez.saboreszgz.model;

import java.util.List;

public class Usuario {
    private String id;
    private String email;
    private String nombre;
    private String tipoUsuario;  // "normal" o "admin"
    private List<String> favoritos;

    // Constructor vacío (necesario para Firestore)
    public Usuario() {
    }

    // Constructor para usuarios nuevos
    public Usuario(String email, String nombre, String tipoUsuario) {
        this.email = email;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<String> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<String> favoritos) {
        this.favoritos = favoritos;
    }

    public boolean isAdmin() {
        return "admin".equals(tipoUsuario);
    }
}