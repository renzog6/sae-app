package ar.nex.entity;

/**
 *
 * @author Renzo
 */
public enum SeguroTipo {

    EMPLEADO(0, "Empleado"),
    EQUIPO(1, "Equipo"),
    CAMPO(2, "Campo"),
    SIEMBRA(3, "Siembra"),
    CEREAL(4, "Cereal"),
    OTRO(5, "Otro");

    private final int value;
    private final String estado;

    private SeguroTipo(int value, String estado) {
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
