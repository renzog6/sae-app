package ar.nex.pedido;

/**
 *
 * @author Renzo
 */
public enum EstadoPedido {

    TODOS(0, "Todos"),
    PENDIENTE(1, "Pendiente"),
    COMPLETO(2, "Completo"),
    CANCELADO(3, "Cancelado");

    private final int value;
    private final String estado;

    private EstadoPedido(int value, String estado) {
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