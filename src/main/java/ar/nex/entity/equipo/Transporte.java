package ar.nex.entity.equipo;

import ar.nex.entity.empleado.Empleado;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "eq_transporte")
public class Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_transporte")
    private Long idTransporte;

    @Column(name = "nombre")
    private String nombre;
    @Column(name = "info")
    private String info;
    @Column(name = "estado")
    private Boolean estado;

    @JoinColumn(name = "id_acopaldo", referencedColumnName = "id_equipo")
    @OneToOne
    private Equipo acoplado;

    @JoinColumn(name = "id_camion", referencedColumnName = "id_equipo")
    @OneToOne
    private Equipo camion;

    @JoinColumn(name = "id_chofer", referencedColumnName = "id_persona")
    @OneToOne
    private Empleado chofer;

    public Transporte() {
    }

    public Transporte(Long idTransporte) {
        this.idTransporte = idTransporte;
    }

    public Long getIdTransporte() {
        return idTransporte;
    }

    public void setIdTransporte(Long idTransporte) {
        this.idTransporte = idTransporte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Equipo getAcoplado() {
        return acoplado;
    }

    public void setAcoplado(Equipo acoplado) {
        this.acoplado = acoplado;
    }

    public Equipo getCamion() {
        return camion;
    }

    public void setCamion(Equipo camion) {
        this.camion = camion;
    }

    public Empleado getChofer() {
        return chofer;
    }

    public void setChofer(Empleado chofer) {
        this.chofer = chofer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransporte != null ? idTransporte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transporte)) {
            return false;
        }
        Transporte other = (Transporte) object;
        if ((this.idTransporte == null && other.idTransporte != null) || (this.idTransporte != null && !this.idTransporte.equals(other.idTransporte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.equipo.Transporte[ idTransporte=" + idTransporte + " ]";
    }

}
