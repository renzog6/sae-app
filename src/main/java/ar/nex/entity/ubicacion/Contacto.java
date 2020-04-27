package ar.nex.entity.ubicacion;

import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.empleado.Persona;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "contacto")
@XmlRootElement
public class Contacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contacto")
    private Long idContacto;

    @Column(name = "tipo")
    private ContactoTipo tipo;

    @Column(name = "dato")
    private String dato;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "info")
    private String info;

    @JoinTable(name = "rh_persona_contacto", joinColumns = {
        @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")}, inverseJoinColumns = {
        @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")})
    @ManyToMany
    private List<Persona> personaList;
    @JoinTable(name = "empresa_contacto", joinColumns = {
        @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")}, inverseJoinColumns = {
        @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")})
    @ManyToMany
    private List<Empresa> empresaList;
    @JoinTable(name = "rh_empleado_contacto", joinColumns = {
        @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")}, inverseJoinColumns = {
        @JoinColumn(name = "id_empleado", referencedColumnName = "id_persona")})
    @ManyToMany
    private List<Empleado> empleadoList;

    public Contacto() {
    }

    public Contacto(String nombre) {
        this.nombre = nombre;
    }

    public Contacto(Long idContacto) {
        this.idContacto = idContacto;
    }

    public Long getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Long idContacto) {
        this.idContacto = idContacto;
    }

    public ContactoTipo getTipo() {
        return tipo;
    }

    public void setTipo(ContactoTipo tipo) {
        this.tipo = tipo;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
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

    @XmlTransient
    public List<Persona> getPersonaList() {
        return personaList;
    }

    public void setPersonaList(List<Persona> personaList) {
        this.personaList = personaList;
    }

    @XmlTransient
    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContacto != null ? idContacto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contacto)) {
            return false;
        }
        Contacto other = (Contacto) object;
        if ((this.idContacto == null && other.idContacto != null) || (this.idContacto != null && !this.idContacto.equals(other.idContacto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " - " + tipo + " - " + dato + " (" + info + ")";
    }

}
