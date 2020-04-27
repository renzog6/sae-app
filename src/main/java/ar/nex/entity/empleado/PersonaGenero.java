package ar.nex.entity.empleado;

/**
 *
 * @author Renzo
 */
public enum PersonaGenero {

    MASCULICNO(0, "Masculino"),
    FEMENICO(1, "Femenino"),
    OTRO(2, "Otro");

    private final int value;
    private final String estado;

    private PersonaGenero(int value, String estado) {
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