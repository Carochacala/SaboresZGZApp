package com.comenendez.saboreszgz;

import com.google.firebase.firestore.GeoPoint;
import java.util.List;

public class Restaurante {
    private String nombre;
    private String tipoCocinaPais;
    private String fotoUrl;
    private String horario;
    private String direccion;
    private double valoracionMedia;
    private List<String> platoDestacado;
    private GeoPoint ubicacion;

    // Constructor vacío necesario para Firebase
    public Restaurante() {}

    // Getters
    public String getNombre() { return nombre; }
    public String getTipoCocinaPais() { return tipoCocinaPais; }
    public String getFotoUrl() { return fotoUrl; }
    public String getHorario() { return horario; }
    public String getDireccion() { return direccion; }
    public double getValoracionMedia() { return valoracionMedia; }
    public List<String> getPlatoDestacado() { return platoDestacado; }
    public GeoPoint getUbicacion() { return ubicacion; }
}