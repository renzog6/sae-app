package ar.nex.entity.empresa;

/**
 *
 * @author Renzo
 */
public enum EmpresaEstado {

    ACTIVO(0, "Activo"),
    BAJA(1, "Baja"),
    SUPENDIDO(2, "Supendido"),
    OTRO(9, "Otro");

    private final int value;
    private final String estado;

    private EmpresaEstado(int value, String estado) {
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
