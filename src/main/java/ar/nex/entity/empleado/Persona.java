package ar.nex.entity.empleado;

import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.ubicacion.Direccion;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "rh_persona")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_persona")
    private Long idPersona;

    @OneToMany(mappedBy = "pariente")
    private List<Familia> familiaList;

    @Column(name = "dtype")
    private String dtype;
    @Column(name = "hijo")
    private Integer hijo;

    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "nacimiento")
    @Temporal(TemporalType.DATE)
    private Date nacimiento;
    @Column(name = "dni")
    private String dni;
    @Column(name = "cuil")
    private String cuil;

    @Enumerated(EnumType.ORDINAL)    
    @Column(name = "genero")
    private PersonaGenero genero;

    @Enumerated(EnumType.ORDINAL) 
    @Column(name = "estado")
    private PersonaEstado estado;
    
    @Enumerated(EnumType.ORDINAL) 
    @Column(name = "estado_civil")
    private EstadoCivil estadoCivil;
    
    @Column(name = "info")
    private String info;

    @ManyToMany(mappedBy = "personaList")
    private List<Contacto> contactoList;

    @JoinColumn(name = "domicilio", referencedColumnName = "id_direccion")
    @ManyToOne
    private Direccion domicilio;

    public Persona() {
    }

    public Persona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Persona(Long idPersona, String nombre, String apellido) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public PersonaGenero getGenero() {
        return genero;
    }

    public void setGenero(PersonaGenero genero) {
        this.genero = genero;
    }

    public PersonaEstado getEstado() {
        return estado;
    }

    public void setEstado(PersonaEstado estado) {
        this.estado = estado;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @XmlTransient
    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public void setContactoList(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    @XmlTransient
    public List<Familia> getFamiliaList() {
        return familiaList;
    }

    public void setFamiliaList(List<Familia> familiaList) {
        this.familiaList = familiaList;
    }

    public Direccion getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Direccion domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }

    public String getNombreCompleto() {
        return String.join(" ", this.apellido, this.nombre);
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public Integer getHijo() {
        return hijo;
    }

    public void setHijo(Integer hijo) {
        this.hijo = hijo;
    }
}
