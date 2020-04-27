package ar.nex.entity;

import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empresa.Empresa;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "doc_seguro")
public class Seguro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_seguro")
    private Long idSeguro;

    @JoinColumn(name = "empresa", referencedColumnName = "id_empresa")
    @OneToOne
    private Empresa empresa;
    @JoinColumn(name = "compania", referencedColumnName = "id_empresa")
    @OneToOne
    private Empresa compania;

    @Column(name = "poliza")
    private String poliza;
    @Column(name = "desde")
    @Temporal(TemporalType.DATE)
    private Date desde;
    @Column(name = "hasta")
    @Temporal(TemporalType.DATE)
    private Date hasta;
    @Column(name = "referencia")
    private String referencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prima")
    private Double prima;
    @Column(name = "monto")
    private Double monto;
    @Column(name = "tipo")
    private SeguroTipo tipo;
    @Column(name = "info")
    private String info;

    @OneToMany(mappedBy = "seguro")
    private List<Equipo> equipoList;

    @OneToMany(mappedBy = "seguro")
    private List<Empleado> empleadoList;

    public Seguro() {
    }

    public Seguro(Long idSeguro) {
        this.idSeguro = idSeguro;
    }
    
    /**
     * Constructor()
     * 
     * Se usa para los nuevos seguros: Equiupos, Empleado, etc...
     * @param tipo 
     */
    public Seguro(SeguroTipo tipo) {
        this.tipo = tipo;
    }

    public Long getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(Long idSeguro) {
        this.idSeguro = idSeguro;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empresa getCompania() {
        return compania;
    }

    public void setCompania(Empresa compania) {
        this.compania = compania;
    }

    public String getPoliza() {
        return poliza;
    }

    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Double getPrima() {
        return prima;
    }

    public void setPrima(Double prima) {
        this.prima = prima;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public SeguroTipo getTipo() {
        return tipo;
    }

    public void setTipo(SeguroTipo tipo) {
        this.tipo = tipo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSeguro != null ? idSeguro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seguro)) {
            return false;
        }
        Seguro other = (Seguro) object;
        if ((this.idSeguro == null && other.idSeguro != null) || (this.idSeguro != null && !this.idSeguro.equals(other.idSeguro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.Seguro[ idSeguro=" + idSeguro + " ]";
    }

}
