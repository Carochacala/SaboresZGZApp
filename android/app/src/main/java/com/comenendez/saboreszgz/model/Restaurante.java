package com.comenendez.saboreszgz.model;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.List;

public class Restaurante implements Serializable {
    private String id;
    private String nombre;
    private String direccion;
    private String fotoUrl;
    private String horario;
    private List<String> platoDestacado;  // ← CAMBIADO A List
    private String tipoCocinaPais;
    private GeoPoint ubicacion;
    private double valoracionMedia;

    public Restaurante() {
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public List<String> getPlatoDestacado() {
        return platoDestacado;
    }

    public void setPlatoDestacado(List<String> platoDestacado) {
        this.platoDestacado = platoDestacado;
    }

    public String getTipoCocinaPais() {
        return tipoCocinaPais;
    }

    public void setTipoCocinaPais(String tipoCocinaPais) {
        this.tipoCocinaPais = tipoCocinaPais;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(GeoPoint ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getValoracionMedia() {
        return valoracionMedia;
    }

    public void setValoracionMedia(double valoracionMedia) {
        this.valoracionMedia = valoracionMedia;
    }
}