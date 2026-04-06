public class TipoComida {

    private String nombre;
    private int banderaResId;

    public TipoComida(String nombre, int banderaResId) {
        this.nombre = nombre;
        this.banderaResId = banderaResId;
    }

    public String getNombre() {
        return nombre;
    }

    public int getBanderaResId() {
        return banderaResId;
    }
}
