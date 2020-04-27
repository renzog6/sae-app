package ar.nex.entity.equipo.gasto;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "eq_gasto_repuesto")
@PrimaryKeyJoinColumn(name = "id_gasto")
public class GastoRepuesto extends Gasto{
    
    @Column(name = "codigo_alt")
    private String codigoAlt;
    

    public GastoRepuesto() {
        super();
    }

    public GastoRepuesto(String codigo) {
        super(codigo);
    }

    public String getCodigoAlt() {
        return codigoAlt;
    }

    public void setCodigoAlt(String codigoAlt) {
        this.codigoAlt = codigoAlt;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.codigoAlt);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GastoRepuesto other = (GastoRepuesto) obj;
        if (!Objects.equals(this.codigoAlt, other.codigoAlt)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GastoRepuesto{" + "codigoAlt=" + codigoAlt + '}';
    }

    
}
