package ar.nex.entity.equipo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "eq_tipo")
@XmlRootElement
public class EquipoTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo")
    private Long idTipo;
    
    @Column(name = "nombre")
    private String nombre;
    
    @OneToMany(mappedBy = "tipo")
    private List<Equipo> equipoList;
    
    @OneToMany(mappedBy = "tipo")
    private List<EquipoModelo> modeloList;
    
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne
    private EquipoCategoria categoria;

    public EquipoTipo() {
    }
    
    public EquipoTipo(String s) {
        this.nombre = s;
    }
    
    public EquipoTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public Long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    @XmlTransient
    public List<EquipoModelo> getModeloList() {
        return modeloList;
    }

    public void setModeloList(List<EquipoModelo> modeloList) {
        this.modeloList = modeloList;
    }

    public EquipoCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(EquipoCategoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipo != null ? idTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipoTipo)) {
            return false;
        }
        EquipoTipo other = (EquipoTipo) object;
        if ((this.idTipo == null && other.idTipo != null) || (this.idTipo != null && !this.idTipo.equals(other.idTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  this.nombre;
    }
    
}
