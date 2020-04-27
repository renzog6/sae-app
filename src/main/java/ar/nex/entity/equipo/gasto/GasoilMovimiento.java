package ar.nex.entity.equipo.gasto;

/**
 *
 * @author Renzo
 */
public enum GasoilMovimiento {

    CARGA(0, "Carga"),
    DESCARDA(1, "Descarga"),
    OTRO(2, "Otro");

    private final int value;
    private final String estado;

    private GasoilMovimiento(int value, String estado) {
        this.value = value;
        this.estado = estado;
    }

    public int getValue() {
        return value;
    }

    public String getNombre() {
        return estado;
    }

    @Override
    public String toString() {
        return estado;
    }

}
