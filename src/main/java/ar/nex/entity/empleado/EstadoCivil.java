package ar.nex.entity.empleado;

/**
 *
 * @author Renzo
 */
public enum EstadoCivil {

    SOLTERO(0, "Soltero"),
    CASADO(1, "Casado"),
    DIVORCIADO(2, "Divorciado"),
    VIUDO(3, "Viudo"),
    CONCUBINATO(4, "Concubinato"),
    OTRO(9, "Otro");

    private final int value;
    private final String estado;

    private EstadoCivil(int value, String estado) {
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