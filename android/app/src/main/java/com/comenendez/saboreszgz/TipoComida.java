package com.comenendez.sabores_zgz;

public class TipoComida {
    private String nombre;
    private int banderaResId;  // Drawable de la bandera
    private int platoResId;    // Drawable del plato

    public TipoComida(String nombre, int banderaResId, int platoResId) {
        this.nombre = nombre;
        this.banderaResId = banderaResId;
        this.platoResId = platoResId;
    }

    public String getNombre() { return nombre; }
    public int getBanderaResId() { return banderaResId; }
    public int getPlatoResId() { return platoResId; }
}
