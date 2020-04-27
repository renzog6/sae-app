package ar.nex.entity.equipo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "eq_documentacion")
public class EquipoDocumentacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_doc")
    private Long idDoc;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "rutaVto")
    @Temporal(TemporalType.DATE)
    private Date rutaVto;
    @Column(name = "tecnicaVto")
    @Temporal(TemporalType.DATE)
    private Date tecnicaVto;
    @Column(name = "patenteVto")
    @Temporal(TemporalType.DATE)
    private Date patenteVto;
    @Column(name = "info")
    private String info;
    @Column(name = "seguro")
    private BigInteger seguro;
    @Column(name = "historial")
    private BigInteger historial;
   
    @OneToOne(mappedBy = "documentacion")
    private Equipo equipo;

    public EquipoDocumentacion() {
    }

    public EquipoDocumentacion(Long idDoc) {
        this.idDoc = idDoc;
    }

    public Long getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(Long idDoc) {
        this.idDoc = idDoc;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getRutaVto() {
        return rutaVto;
    }

    public void setRutaVto(Date rutaVto) {
        this.rutaVto = rutaVto;
    }

    public Date getTecnicaVto() {
        return tecnicaVto;
    }

    public void setTecnicaVto(Date tecnicaVto) {
        this.tecnicaVto = tecnicaVto;
    }

    public Date getPatenteVto() {
        return patenteVto;
    }

    public void setPatenteVto(Date patenteVto) {
        this.patenteVto = patenteVto;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public BigInteger getSeguro() {
        return seguro;
    }

    public void setSeguro(BigInteger seguro) {
        this.seguro = seguro;
    }

    public BigInteger getHistorial() {
        return historial;
    }

    public void setHistorial(BigInteger historial) {
        this.historial = historial;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDoc != null ? idDoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipoDocumentacion)) {
            return false;
        }
        EquipoDocumentacion other = (EquipoDocumentacion) object;
        if ((this.idDoc == null && other.idDoc != null) || (this.idDoc != null && !this.idDoc.equals(other.idDoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.equipo.EquipoDocumentacion[ idDoc=" + idDoc + " ]";
    }
    
}
