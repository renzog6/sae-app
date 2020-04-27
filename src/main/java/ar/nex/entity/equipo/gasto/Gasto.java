package ar.nex.entity.equipo.gasto;

import ar.nex.entity.equipo.Equipo;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "eq_gasto")
@Inheritance(strategy = InheritanceType.JOINED)
public class Gasto implements Serializable {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gasto", nullable = false)
    private Long idGasto;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "dtype")
    private String dtype;

    @Column(name = "info")
    private String info;

    @Column(name = "kms")
    private Double kms;

    @JoinColumn(name = "equipo", referencedColumnName = "id_equipo")
    @ManyToOne
    private Equipo equipo;

    public Gasto() {
        super();
    }

    public Gasto(String codigo) {
        super();
        this.codigo = codigo;
    }

    public Gasto(String codigo, Double kms) {
        super();
        this.codigo = codigo;
        this.kms = kms;
    }

    public Gasto(Long idGasto) {
        this.idGasto = idGasto;
    }

    public Long getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(Long idGasto) {
        this.idGasto = idGasto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public Double getKms() {
        return kms;
    }
    
    public void setKms(Double kms) {
        this.kms = kms;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.idGasto);
        hash = 17 * hash + Objects.hashCode(this.fecha);
        hash = 17 * hash + Objects.hashCode(this.equipo);
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
        final Gasto other = (Gasto) obj;
        if (!Objects.equals(this.idGasto, other.idGasto)) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        if (!Objects.equals(this.equipo, other.equipo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.equipo.gasto.Gasto[ idGasto=" + idGasto + " ]";
    }
 

}
