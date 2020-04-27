package ar.nex.entity.ubicacion;

public enum ContactoTipo {

    FIJO(1, "Fijo", 0), CELULAR(3, "Celular", 1), EMAIL(5, "Email", 2), WEB(7, "Web", 3), OTRO(9, "Otro", 4);

    private final int value;
    private final String nombre;
    private final int index;

    ContactoTipo(int value, String nombre, int index) {
        this.value = value;
        this.nombre = nombre;
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
