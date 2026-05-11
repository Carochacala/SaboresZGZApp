package com.comenendez.saboreszgz.modelo;

import com.google.firebase.firestore.GeoPoint; // Importante importar esto
import java.util.List;

public class Restaurante {
    private String id;
    private String nombre;
    private String tipoCocinaPais;
    private String fotoUrl;
    private String horario;
    private String direccion;
    private double valoracionMedia;
    private List<String> platoDestacado;

    // Cambiamos latitud y longitud por esto para que coincida con Firebase
    private GeoPoint ubicacion;

    public Restaurante() {

    } // Constructor vacío obligatorio

    // GETTERS Y SETTERS
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

    public String getTipoCocinaPais() {
        return tipoCocinaPais;
    }
    public void setTipoCocinaPais(String tipoCocinaPais) {
        this.tipoCocinaPais = tipoCocinaPais;
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

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getValoracionMedia() {
        return valoracionMedia;
    }
    public void setValoracionMedia(double valoracionMedia) {
        this.valoracionMedia = valoracionMedia;
    }

    public List<String> getPlatoDestacado() {
        return platoDestacado;
    }
    public void setPlatoDestacado(List<String> platoDestacado) {
        this.platoDestacado = platoDestacado;
    }

    // ubicacion con geopoint
    public GeoPoint getUbicacion() {
        return ubicacion; }
    public void setUbicacion(GeoPoint ubicacion) {
        this.ubicacion = ubicacion; }
}