package ar.nex.sincronizar;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
public enum SincronizarEstado {

    PENDIENTE(0),
    SINCRONIZADO(1),
    OTRO(2);

    private final int value;

    private SincronizarEstado(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
