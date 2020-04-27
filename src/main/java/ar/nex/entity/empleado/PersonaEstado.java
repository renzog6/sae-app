package ar.nex.entity.empleado;

/**
 *
 * @author Renzo
 */
public enum PersonaEstado {

    ACTIVO(0, "Activo"),
    BAJA(1, "Baja"),
    OTRO(2, "Otro");

    private final int value;
    private final String estado;

    private PersonaEstado(int value, String estado) {
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
