package ar.nex.entity.equipo;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "ped_repuesto")
@XmlRootElement
public class Repuesto implements Serializable {  
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_repuesto")
    private Long idRepuesto;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stock")
    private Double stock;
    
    @Column(name = "marca")
    private String marca;
    
    @Column(name = "info")
    private String info;
    @Column(name = "parte")
    private String parte;

    @ManyToMany(mappedBy = "repuestoList")
    private List<Equipo> equipoList;

    @ManyToMany(mappedBy = "repuestoList")
    private List<EquipoModelo> modeloList;

    @OneToMany(mappedBy = "repuesto")
    private List<RepuestoStockDetalle> repuestoStockDetalleList;

    @OneToMany(mappedBy = "repuesto")
    private List<Pedido> pedidoList;

    public Repuesto() {
    }

    public Repuesto(Long idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public Long getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(Long idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
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

    @XmlTransient
    public List<RepuestoStockDetalle> getRepuestoStockDetalleList() {
        return repuestoStockDetalleList;
    }

    public void setRepuestoStockDetalleList(List<RepuestoStockDetalle> repuestoStockDetalleList) {
        this.repuestoStockDetalleList = repuestoStockDetalleList;
    }

    @XmlTransient
    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRepuesto != null ? idRepuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Repuesto)) {
            return false;
        }
        Repuesto other = (Repuesto) object;
        if ((this.idRepuesto == null && other.idRepuesto != null) || (this.idRepuesto != null && !this.idRepuesto.equals(other.idRepuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.codigo + " - " + this.getDescripcion();
    }

}
