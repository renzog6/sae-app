package ar.nex.entity.empresa;

import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.Pedido;
import ar.nex.entity.equipo.Repuesto;
import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.ubicacion.Direccion;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empresa")
    private Long idEmpresa;
    @Column(name = "cuit")
    private String cuit;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "razon_social")
    private String razonSocial;
    
    @JoinTable(name = "empresa_rubro", joinColumns = {
        @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")}, inverseJoinColumns = {
        @JoinColumn(name = "id_rubro", referencedColumnName = "id_rubro")})
    @ManyToMany
    private List<Rubro> rubroList;
    
    @ManyToMany(mappedBy = "empresaList")
    private List<Contacto> contactoList;

    @OneToMany(mappedBy = "empresa")
    private List<Empleado> empleadoList;

    @OneToMany(mappedBy = "empresa")
    private List<Pedido> pedidoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<Equipo> equipoList;

    @JoinTable(name = "empresa_direccion", joinColumns = {
        @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")}, inverseJoinColumns = {
        @JoinColumn(name = "id_direccion", referencedColumnName = "id_direccion")})
    @ManyToMany
    private List<Direccion> direccionList;

    public Empresa() {
    }

    public Empresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @XmlTransient
    public List<Rubro> getRubroList() {
        return rubroList;
    }

    public void setRubroList(List<Rubro> rubroList) {
        this.rubroList = rubroList;
    }

    @XmlTransient
    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public void setContactoList(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @XmlTransient
    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

    @XmlTransient
    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    @XmlTransient
    public List<Direccion> getDireccionList() {
        return direccionList;
    }

    public void setDireccionList(List<Direccion> direccionList) {
        this.direccionList = direccionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

}
