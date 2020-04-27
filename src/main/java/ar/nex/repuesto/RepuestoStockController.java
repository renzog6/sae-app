package ar.nex.repuesto;

import ar.nex.entity.equipo.Pedido;
import ar.nex.entity.equipo.RepuestoStockDetalle;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.RepuestoJpaController;
import ar.nex.jpa.RepuestoStockDetalleJpaController;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class RepuestoStockController {

    private final RepuestoStockDetalleJpaController jpaStockDetalle;

    public RepuestoStockController() {
        jpaStockDetalle = new RepuestoStockDetalleJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
    }

    private RepuestoJpaController jpaRepuesto() {
        return new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
    }

    public void inStock(Pedido pedido) {
        try {
            pedido.getRepuesto().setStock(pedido.getCantidad() + pedido.getRepuesto().getStock());
            //RepuestoJpaController jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            jpaRepuesto().edit(pedido.getRepuesto());

            RepuestoStockDetalle stock = new RepuestoStockDetalle();
            stock.setFecha(pedido.getFechaFin());
            stock.setDetalle("Compra: " + pedido.getEmpresa().getNombre());
            stock.setCantidad(pedido.getCantidad());
            stock.setRepuesto(pedido.getRepuesto());
            stock.setEstado(1);

            jpaStockDetalle.create(stock);

        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
    }

    public void outStock(RepuestoStockDetalle stockDetalle) {
        try {
            //RepuestoJpaController jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            stockDetalle.getRepuesto().setStock(stockDetalle.getRepuesto().getStock() - stockDetalle.getCantidad());
            jpaRepuesto().edit(stockDetalle.getRepuesto());

            jpaStockDetalle.create(stockDetalle);
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
    }

    public void editarStock(RepuestoStockDetalle stockDetalle, Double nuevaCantidad) {
        try {
            if (stockDetalle.getCantidad() > nuevaCantidad) {
                stockDetalle.getRepuesto().setStock(stockDetalle.getRepuesto().getStock() + (nuevaCantidad - stockDetalle.getCantidad()));
            } else {
                stockDetalle.getRepuesto().setStock(stockDetalle.getRepuesto().getStock() + (stockDetalle.getCantidad() - nuevaCantidad));
            }
            stockDetalle.setCantidad(nuevaCantidad);
            jpaRepuesto().edit(stockDetalle.getRepuesto());
            jpaStockDetalle.edit(stockDetalle);
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
    }

    public void borrarStock(RepuestoStockDetalle stockDetalle) {
        try {
            stockDetalle.getRepuesto().setStock(stockDetalle.getCantidad() + stockDetalle.getRepuesto().getStock());
            //RepuestoJpaController jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            jpaRepuesto().edit(stockDetalle.getRepuesto());

            jpaStockDetalle.destroy(stockDetalle.getIdStock());
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
    }
}
