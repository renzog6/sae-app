package ar.nex.pedido;

import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.equipo.Pedido;
import ar.nex.entity.equipo.Repuesto;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class PedidoEditController implements Initializable {

    public PedidoEditController(Pedido pedido) {
        this.pedido = pedido;
    }

    private Pedido pedido;

    @FXML
    private DatePicker dpInicio;
    @FXML
    private TextField boxCantidad;
    @FXML
    private TextField boxCodigo;
    @FXML
    private TextField boxProveedor;
    @FXML
    private DatePicker dpFin;
    @FXML
    private ComboBox<EstadoPedido> filtroEstado;
    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Repuesto repuestoSelect;
    private final ObservableList<Repuesto> dataRepuesto = FXCollections.observableArrayList();
    private Empresa proveedorSelect;
    private final ObservableList<Empresa> dataProveedor = FXCollections.observableArrayList();
    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        jpa = new JpaService();
        initControls();
    }

    private void listaRepueto() {
        try {
            List<Repuesto> results = jpa.getRepuesto().findRepuestoEntities();
            if (!results.isEmpty()) {
                results.forEach((item) -> {
                    dataRepuesto.add(item);
                });
            }
        } catch (Exception e) {
            dataRepuesto.clear();
        }

    }
    
        private void listaProveedor() {
        try {
            List<Empresa> results = jpa.getEmpresa().findEmpresaEntities();
            if (!results.isEmpty()) {
                results.forEach((item) -> {
                    dataProveedor.add(item);
                });
            }
        } catch (Exception e) {
            dataProveedor.clear();
        }

    }

    /**
     * Inicializar todos los elementos a usar.
     */
    private void initControls() {
        try {
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            listaRepueto();
            AutoCompletionBinding<Repuesto> autoCodigo = TextFields.bindAutoCompletion(boxCodigo, dataRepuesto);
            autoCodigo.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<Repuesto> event) -> {
                repuestoSelect = event.getCompletion();
            }
            );
            
            listaProveedor();
             AutoCompletionBinding<Empresa> autoProvedor = TextFields.bindAutoCompletion(boxProveedor, dataProveedor);
            autoProvedor.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<Empresa> event) -> {
                proveedorSelect = event.getCompletion();
            }
            );

            boxCantidad.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*{0,7}([\\.]\\d*{0,4})?")) {
                        boxCantidad.setText(oldValue);
                    }
                }
            });

            filtroEstado.getItems().addAll((ObservableList) FXCollections.observableArrayList(EstadoPedido.values()));
            filtroEstado.getSelectionModel().select(EstadoPedido.PENDIENTE.getValue());
            filtroEstado.valueProperty().addListener(new ChangeListener<EstadoPedido>() {
                @Override
                public void changed(ObservableValue<? extends EstadoPedido> observable, EstadoPedido oldValue, EstadoPedido newValue) {
                    switch (newValue) {
                        case PENDIENTE:
                            dpFin.setDisable(true);
                            break;
                        case COMPLETO:
                            dpFin.setDisable(false);
                            dpFin.setValue(LocalDate.now());
                            break;
                        case CANCELADO:
                            dpFin.setDisable(false);
                            dpFin.setValue(LocalDate.now());
                            break;
                        case TODOS:
                            dpFin.setDisable(true);
                            filtroEstado.getSelectionModel().select(EstadoPedido.PENDIENTE.getValue());
                            break;
                    }
                }
            });

            if (pedido != null) {
                dpInicio.setValue(DateUtils.convertToLocalDateViaSqlDate(pedido.getFechaInicio()));
                boxCodigo.setText(pedido.getRepuesto().toString());
                repuestoSelect = pedido.getRepuesto();
                boxProveedor.setText(pedido.getEmpresa().toString());
                proveedorSelect = pedido.getEmpresa();
                boxCantidad.setText(pedido.getCantidad().toString());
                boxInfo.setText(pedido.getInfo());
                filtroEstado.getSelectionModel().select(pedido.getEstado());
                if (pedido.getEstado().equals(EstadoPedido.PENDIENTE.getValue())) {
                    dpFin.setDisable(true);
                } else {
                    dpFin.setValue(DateUtils.convertToLocalDateViaSqlDate(pedido.getFechaFin()));
                }
            } else {
                pedido = new Pedido();
                dpInicio.setValue(LocalDate.now());
                dpFin.setDisable(true);
            }
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }

    }

    private void guardar(ActionEvent e) {
        try {
            pedido.setFechaInicio(DateUtils.convertToDateViaSqlDate(dpInicio.getValue()));
            pedido.setCantidad(Double.valueOf(boxCantidad.getText()));
            pedido.setRepuesto(repuestoSelect);
            pedido.setEmpresa(proveedorSelect);
            pedido.setInfo(boxInfo.getText());
            pedido.setEstado(filtroEstado.getSelectionModel().getSelectedIndex());
            if (pedido.getEstado().equals(EstadoPedido.PENDIENTE.getValue())) {
                pedido.setFechaFin(null);
            } else {
                pedido.setFechaFin(DateUtils.convertToDateViaSqlDate(dpFin.getValue()));
            }
            if (pedido.getIdPedido() != null) {
                jpa.getPedido().edit(pedido);
            } else {
                jpa.getPedido().create(pedido);
            }
            ((Node) (e.getSource())).getScene().getWindow().hide();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
