package ar.nex.entity.empleado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "rh_familia")
public class Familia extends Persona {

    @Column(name = "id_familia")
    private Long idFamilia;

    @Column(name = "relacion")
    private String relacion;

    @JoinColumn(name = "pariente", referencedColumnName = "id_persona")
    @ManyToOne
    private Persona pariente;

    public Persona getPariente() {
        return pariente;
    }

    public void setPariente(Persona pariente) {
        this.pariente = pariente;
    }

    public Familia() {
        super();
    }

    public Familia(Long idFamilia) {
        this.idFamilia = idFamilia;
    }

    public Long getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(Long idFamilia) {
        this.idFamilia = idFamilia;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFamilia != null ? idFamilia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Familia)) {
            return false;
        }
        Familia other = (Familia) object;
        if ((this.idFamilia == null && other.idFamilia != null) || (this.idFamilia != null && !this.idFamilia.equals(other.idFamilia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " - " + relacion;
    }

}
