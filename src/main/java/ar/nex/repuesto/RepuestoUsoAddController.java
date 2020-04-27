package ar.nex.repuesto;

import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.Repuesto;
import ar.nex.entity.equipo.RepuestoStockDetalle;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.RepuestoJpaController;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javax.persistence.Persistence;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RepuestoUsoAddController implements Initializable {

    public RepuestoUsoAddController(Equipo equipo) {
        this.equipo = equipo;
    }

    @FXML
    private BorderPane bpUsoDialog;

    @FXML
    private DatePicker boxFecha;
    @FXML
    private TextField boxRepuesto;
    @FXML
    private TextField boxCantidad;
    @FXML
    private TextField boxInfo;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;

    @FXML
    private Repuesto stockDetalleSelect;
    private final ObservableList<RepuestoStockDetalle> dataStockDetalle = FXCollections.observableArrayList();
    @FXML
    private TableView<RepuestoStockDetalle> tableStockDetalle;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colRepuesto;
    @FXML
    private TableColumn<?, ?> colCantidad;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<RepuestoStockDetalle, Void> colAction;

    private final DateFormat fd = new SimpleDateFormat("yyyy-mm-dd");

    private final Equipo equipo;

    @FXML
    private Repuesto repuestoSelect;
    private final ObservableList<Repuesto> dataRepuesto = FXCollections.observableArrayList();
    private RepuestoJpaController jpaRepuesto;

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
        boxFecha.setValue(LocalDate.now());
        initServices();
        initTableStockDetalle();
        loadDataRepuesto();
        InitControls();
    }

    private void initServices() {
        jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
    }

    private void initTableStockDetalle() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colRepuesto.setCellValueFactory(new PropertyValueFactory<>("repuesto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
    }

    private void InitControls() {
        try {

            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            btnAdd.setOnAction(e -> addRepuesto());
            btnAdd.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER) || ke.getCode().equals(KeyCode.ESCAPE)) {
                        addRepuesto();
                    }
                }
            });

            AutoCompletionBinding<Repuesto> auto = TextFields.bindAutoCompletion(boxRepuesto, dataRepuesto);
            auto.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<Repuesto> event) -> {
                repuestoSelect = event.getCompletion();
            });

            boxCantidad.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                        String newValue) {
                    if (!newValue.matches("\\d*")) {
                        boxCantidad.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });

            addButtonToTable();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void resetControls() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxRepuesto.requestFocus();
            }
        });
        boxRepuesto.clear();
        boxCantidad.setText("");
        boxInfo.setText("");
    }

    private void loadDataRepuesto() {
        try {
            this.dataRepuesto.clear();
            List<Repuesto> lst = jpaRepuesto.findRepuestoEntities();
            lst.forEach((item) -> {
                this.dataRepuesto.add(item);
            });
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public boolean validate() {
        if (!dateUtil.validate(boxFecha.getValue())) {
            UtilDialog.showSuccess("Requiere una Fecha Valida.");
            return false;
        }
        if (boxRepuesto.getText().isEmpty()) {
            UtilDialog.showSuccess("Requiere un Repuesto.");
            return false;
        }
        if (boxCantidad.getText().isEmpty()) {
            UtilDialog.showSuccess("Requiere una Cantidad.");
            return false;
        }

        return true;
    }

    private void addRepuesto() {        
        try {
            if (validate()) {
                RepuestoStockDetalle uso = new RepuestoStockDetalle();
                uso.setEquipo(equipo);
                uso.setRepuesto(repuestoSelect);
                uso.setFecha(dateUtil.convertToDateViaSqlDate(boxFecha.getValue()));
                uso.setCantidad(Double.valueOf(boxCantidad.getText()));
                uso.setInfo(boxInfo.getText());
                uso.setDetalle("Usado en:");

                loadDataStockDetalle(uso);
                resetControls();
            }
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
    }

    private void loadDataStockDetalle(RepuestoStockDetalle rsd) {
        try {
            dataStockDetalle.add(rsd);
            tableStockDetalle.setItems(dataStockDetalle);
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
    }

    private void addButtonToTable() {
        try {
            Callback<TableColumn<RepuestoStockDetalle, Void>, TableCell<RepuestoStockDetalle, Void>> cellFactory = new Callback<TableColumn<RepuestoStockDetalle, Void>, TableCell<RepuestoStockDetalle, Void>>() {
                public TableCell<RepuestoStockDetalle, Void> call(final TableColumn<RepuestoStockDetalle, Void> param) {
                    final TableCell<RepuestoStockDetalle, Void> cell = new TableCell<RepuestoStockDetalle, Void>() {

                        private final Button btn = new Button("-");

                        {
                            btn.setOnAction((ActionEvent event) -> {
                                RepuestoStockDetalle data = getTableView().getItems().get(getIndex());
                                tableStockDetalle.getItems().remove(getIndex());
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btn);
                            }
                        }
                    };
                    return cell;
                }
            };

            colAction.setCellFactory(cellFactory);
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
    }

    private void guardar(ActionEvent event) {
        try {
            dataStockDetalle.forEach((item) -> {
                new RepuestoStockController().outStock(item);
            });
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
