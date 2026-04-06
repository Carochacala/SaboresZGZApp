package com.comenendez.saboreszgz;

public class ModeloCajitaRestaurante {
    String nombreRestaurante;
    String descripcionCortaRestaurante;
    float estrellansRestaurante;
    float distanciaRestaurante;
    int imagenRestauranteGuardada;
    String imagenRestauranteURL;

//CONSTRUCTOR
    public ModeloCajitaRestaurante(String nombreRestaurante,
                                   String descripcionCortaRestaurante,
                                   float estrellansRestaurante,
                                   float distanciaRestaurante,
                                   int imagenRestauranteGuardada,
                                   String imagenRestauranteURL) {
        this.nombreRestaurante = nombreRestaurante;
        this.descripcionCortaRestaurante = descripcionCortaRestaurante;
        this.estrellansRestaurante = estrellansRestaurante;
        this.distanciaRestaurante = distanciaRestaurante;
        this.imagenRestauranteGuardada = imagenRestauranteGuardada;
        this.imagenRestauranteURL = imagenRestauranteURL;
    }

    //GETTERS

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public String getDescripcionCortaRestaurante() {
        return descripcionCortaRestaurante;
    }

    public float getEstrellansRestaurante() {
        return estrellansRestaurante;
    }

    public float getDistanciaRestaurante() {
        return distanciaRestaurante;
    }

    public int getImagenRestauranteGuardada() {
        return imagenRestauranteGuardada;
    }

    public String getImagenRestauranteURL() {
        return imagenRestauranteURL;
    }
}
