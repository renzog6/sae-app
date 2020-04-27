package ar.nex.repuesto;

import ar.nex.entity.equipo.RepuestoStockDetalle;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.RepuestoStockDetalleJpaController;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RepuestoUsoEditController implements Initializable {

    public RepuestoUsoEditController(RepuestoStockDetalle stockDetalle) {
        this.stockDetalle = stockDetalle;
    }

    @FXML
    private BorderPane bpUsoDialog;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnBorrar;
    @FXML
    private DatePicker boxFecha;
    @FXML
    private TextField boxRepuesto;
    @FXML
    private TextField boxCantidad;
    @FXML
    private TextField boxInfo;

    private final RepuestoStockDetalle stockDetalle;

    private RepuestoStockDetalleJpaController jpaStockDetalle;

    private DateUtils dateUtil;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateUtil = new DateUtils();
        initControls();
    }

    private void initControls() {
        try {
            btnCancelar.setOnAction(e -> cerrar(e));
            btnBorrar.setOnAction(e -> borrar(e));
            btnGuardar.setOnAction(e -> guardar(e));

            boxRepuesto.setText(stockDetalle.getRepuesto().toString());
            boxRepuesto.setDisable(true);

            boxFecha.setValue(dateUtil.convertToLocalDateViaSqlDate(stockDetalle.getFecha()));
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }

        boxCantidad.setText(stockDetalle.getCantidad().toString());
        boxInfo.setText(stockDetalle.getInfo());
    }

    private void borrar(ActionEvent event) {
        try {
            String msg = "Seguro que desea ELIMINAR esta entrada?";
            if (UtilDialog.confirmDialog(msg)) {
                new RepuestoStockController().borrarStock(stockDetalle);
            }
        } catch (Exception ex) {
            Logger.getLogger(RepuestoUsoEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cerrar(event);
    }

    private void guardar(ActionEvent event) {
        try {
            if (!validarCambios()) {

                String msg = "Seguro que desea GUARDAR los cambios?";
                if (UtilDialog.confirmDialog(msg)) {
                    stockDetalle.setFecha(dateUtil.convertToDateViaSqlDate(boxFecha.getValue()));
                    stockDetalle.setInfo(boxInfo.getText());

                    new RepuestoStockController().editarStock(stockDetalle, Double.valueOf(boxCantidad.getText()));
                }
            } else {
                UtilDialog.showSuccess("No se modifico nada!!!");
            }
        } catch (Exception ex) {
            Logger.getLogger(RepuestoUsoEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cerrar(event);
    }

    private boolean validarCambios() {
        boolean fecha = dateUtil.compareDateToLocalDate(stockDetalle.getFecha(), boxFecha.getValue());        
        boolean cantidad = Objects.equals(stockDetalle.getCantidad(), Double.valueOf(boxCantidad.getText()));        
        boolean info = Objects.equals(stockDetalle.getInfo(), boxInfo.getText());        

        return fecha && cantidad && info;
    }

    private void cerrar(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
