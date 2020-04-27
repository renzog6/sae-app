package ar.nex.entity.equipo.gasto;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "eq_gasto_gasoil")
@PrimaryKeyJoinColumn(name = "id_gasto")
public class Gasoil extends Gasto {

    @Column(name = "movimineto")
    private GasoilMovimiento movimineto;
    
    @Column(name = "litros")
    private Double litros;
    @Column(name = "precio")
    private Double precio;
    @Column(name = "stock")
    private Double stock;
    @Column(name = "stock_update")
    private boolean stockUpdate;

    public Gasoil() {
        super();
    }

    public Gasoil(String codigo) {
        super(codigo);
    }

    public Double getLitros() {
        return litros;
    }

    public void setLitros(Double litros) {
        this.litros = litros;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public boolean isStockUpdate() {
        return stockUpdate;
    }

    public void setStockUpdate(boolean stockUpdate) {
        this.stockUpdate = stockUpdate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.litros);
        hash = 41 * hash + Objects.hashCode(this.precio);
        return hash;
    }

    public GasoilMovimiento getMovimineto() {
        return movimineto;
    }

    public void setMovimineto(GasoilMovimiento movimineto) {
        this.movimineto = movimineto;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
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
        final Gasoil other = (Gasoil) obj;
        if (!Objects.equals(this.litros, other.litros)) {
            return false;
        }
        if (!Objects.equals(this.precio, other.precio)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GastoGasoil{" + "litros=" + litros + ", precio=" + precio + '}';
    }

}
