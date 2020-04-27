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
@Table(name = "eq_gasto_neumatico")
@PrimaryKeyJoinColumn(name = "id_gasto")
public class GastoNeumatico extends Gasto {

    @Column(name = "numero")
    private String numero;
   
    @Column(name = "posicion")
    private String posicion;

    public GastoNeumatico() {
        super();
    }

    public GastoNeumatico(String codigo) {
        super(codigo);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.numero);
        hash = 97 * hash + Objects.hashCode(this.posicion);
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
        final GastoNeumatico other = (GastoNeumatico) obj;
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.posicion, other.posicion)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GastoNeumatico{" + "numero=" + numero + ", posicion=" + posicion + '}';
    }

  
}
