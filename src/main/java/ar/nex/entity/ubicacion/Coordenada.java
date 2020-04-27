package ar.nex.entity.ubicacion;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "ubi_coordenada")
@XmlRootElement
public class Coordenada implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id_coordenada")
    private Long idCoordenada;
    @Column(name = "info")
    private String info;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitud")
    private Double latitud;
    @Column(name = "longitud")
    private Double longitud;
    @Column(name = "poly")
    private String poly;
    
    @JoinColumn(name = "id_direccion", referencedColumnName = "id_direccion")
    @ManyToOne
    private Direccion direccion;

    private static final long serialVersionUID = 1L;

    public Coordenada() {
    }

    public Coordenada(Long idCoordenada) {
        this.idCoordenada = idCoordenada;
    }

    public Long getIdCoordenada() {
        return idCoordenada;
    }

    public void setIdCoordenada(Long idCoordenada) {
        this.idCoordenada = idCoordenada;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getPoly() {
        return poly;
    }

    public void setPoly(String poly) {
        this.poly = poly;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCoordenada != null ? idCoordenada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Coordenada)) {
            return false;
        }
        Coordenada other = (Coordenada) object;
        if ((this.idCoordenada == null && other.idCoordenada != null) || (this.idCoordenada != null && !this.idCoordenada.equals(other.idCoordenada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.ubicacion.Coordenada[ idCoordenada=" + idCoordenada + " ]";
    }
   
}
