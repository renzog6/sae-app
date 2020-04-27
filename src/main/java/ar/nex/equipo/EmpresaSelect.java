package ar.nex.equipo;

/**
 *
 * @author Renzo
 */
public enum EmpresaSelect {

    TODAS(0, "Todas"),
    RRR(1, "Resconi Raul"), 
    RCM(2, "RCM S.A."),
    OTRA(3, "Otra Empresa");

    private final long id;
    private final String nombre;

    private EmpresaSelect(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    
}
