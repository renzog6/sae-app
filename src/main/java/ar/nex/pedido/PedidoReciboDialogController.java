package ar.nex.pedido;

import ar.nex.repuesto.RepuestoStockController;
import ar.nex.entity.equipo.Pedido;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.PedidoJpaController;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class PedidoReciboDialogController implements Initializable {

    public PedidoReciboDialogController(Pedido p) {
        this.pedido = p;
    }

    @FXML
    private Label lblCodigo;

    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField boxCantidad;
    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<?> filtroEstado;

    private final Pedido pedido;

    /**
     * Initializes the controller
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCancelar.setOnAction(e -> cancelar(e));
        btnGuardar.setOnAction(e -> guardar(e));

        initControls();
    }

    private void initControls() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxCantidad.requestFocus();
                }
            });

            lblCodigo.setText("Codigo: " + pedido.getRepuesto().toString() + " ( Cantidad Pedida: " + pedido.getCantidad() + " )");

            dpFecha.setValue(LocalDate.now());

            boxCantidad.setText(pedido.getCantidad().toString());
            boxCantidad.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if ((!newValue.matches("\\d*")) || (Double.valueOf(newValue) < pedido.getCantidad())) {
                        boxCantidad.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (!newValue.isEmpty()) {
                        if (Double.valueOf(newValue) < pedido.getCantidad()) {
                            filtroEstado.getSelectionModel().select(EstadoPedido.PENDIENTE.getValue());
                        }
                        if (Objects.equals(Double.valueOf(newValue), pedido.getCantidad())) {
                            filtroEstado.getSelectionModel().select(EstadoPedido.COMPLETO.getValue());
                        }
                    }
                }
            });

            boxInfo.setText(pedido.getInfo());

            ObservableList list = FXCollections.observableArrayList(EstadoPedido.values());
            filtroEstado.getItems().addAll(list);
            filtroEstado.getSelectionModel().select(EstadoPedido.COMPLETO.getValue());
            System.out.println("ar.nex.pedido.PedidoReciboDialogController.initControls() :" + pedido.getRepuesto().getStock());

        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        try {
            PedidoJpaController jpaPedido = new PedidoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            
            if (filtroEstado.getSelectionModel().getSelectedIndex() == EstadoPedido.PENDIENTE.getValue()) {
                Pedido p = new Pedido();
                p.setFechaInicio(pedido.getFechaInicio());
                p.setRepuesto(pedido.getRepuesto());
                p.setEmpresa(pedido.getEmpresa());
                p.setEstado(EstadoPedido.PENDIENTE.getValue());
                p.setCantidad(pedido.getCantidad() - Double.valueOf(boxCantidad.getText()));
                p.setInfo(DateUtils.convertToDateViaSqlDate(dpFecha.getValue()) + " llegaron " + boxCantidad.getText());
                jpaPedido.create(p);
            }

            pedido.setFechaFin(DateUtils.convertToDateViaSqlDate(dpFecha.getValue()));

            pedido.setCantidad(Double.valueOf(boxCantidad.getText()));
            pedido.setInfo(boxInfo.getText());
            pedido.setEstado(EstadoPedido.COMPLETO.getValue());
            //pedido.setEstado(filtroEstado.getSelectionModel().getSelectedIndex());

            if (pedido.getIdPedido() != null) {
                jpaPedido.edit(pedido);
                System.out.println("ar.nex.pedido.PedidoReciboDialogController.guardar() : " + pedido.getRepuesto().getStock());
                new RepuestoStockController().inStock(pedido);
            } else {
                //jpaPedido.create(pedido);
                System.out.println("ar.nex.pedido.PedidoReciboDialogController.guardar() : VERRRRRRRRRRRRRRRRRRRRRRR");
            }
            cancelar(event);
        } catch (Exception e) {
            UtilDialog.showException(e);
        }

    }

    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

}