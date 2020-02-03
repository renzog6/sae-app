package ar.nex.seguro;

import ar.nex.util.SaeDate;
import ar.nex.entity.Seguro;
import ar.nex.entity.empleado.Empleado;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.service.JpaService;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class SeguroEmpleadoEditController implements Initializable {

    public SeguroEmpleadoEditController(Seguro seguro) {
        this.seguro = seguro;
    }

    @FXML
    private BorderPane bpMain;
    @FXML
    private TextField searchBox;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private ObservableList<Empleado> data = FXCollections.observableArrayList();
    private FilteredList<Empleado> filteredData = new FilteredList<>(data);
    private Empleado equipoSelect;
    private List<Empleado> listEmpleadoSelect;

    @FXML
    private TableView<Empleado> table;
    @FXML
    private TableColumn colAccion;
    @FXML
    private TableColumn<Empleado, String> colEmpleado;
    @FXML
    private TableColumn<Empleado, String> colPuesto;
    @FXML
    private TableColumn<Empleado, String> colFechaAlta;
    @FXML
    private TableColumn<Empleado, String> colAntiguedad;
    @FXML
    private TableColumn<Empleado, String> colCategoria;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TableColumn<Empleado, String> colEdad;

    private JpaService jpa;
    private final Seguro seguro;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));
            startTask();
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    private void startTask() {
        // Create a Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                jpa = new JpaService();
                listEmpleadoSelect = new ArrayList<>();
                initTable();
                initCellAccion();
                loadData(0);
            }
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void clearAll() {
        data.clear();
        searchBox.clear();
        equipoSelect = null;
    }

    private void initTable() {
        try {
            colEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> p) {
                    return new SimpleStringProperty(p.getValue().getNombreCompleto());
                }
            });

            colEdad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        Integer edad = SaeDate.getEdad(param.getValue().getNacimiento());
                        return new SimpleStringProperty(edad.toString());
                    } catch (Exception e) {
                        return new SimpleStringProperty("XX");
                    }
                }
            });

            colAntiguedad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        Integer edad = SaeDate.getEdad(param.getValue().getFechaAlta());
                        return new SimpleStringProperty(edad.toString());
                    } catch (Exception e) {
                        return new SimpleStringProperty("XX");
                    }
                }
            });

            colFechaAlta.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        String date = SaeDate.getDateString(param.getValue().getFechaAlta());
                        return new SimpleStringProperty(date);
                    } catch (Exception e) {
                        return new SimpleStringProperty("NN");
                    }
                }
            });

            colPuesto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        String puesto = param.getValue().getPuesto().getNombre();
                        return new SimpleStringProperty(puesto);
                    } catch (Exception e) {
                        return new SimpleStringProperty("NN");
                    }
                }
            });

            colCategoria.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        String puesto = param.getValue().getCategoria().getNombre();
                        return new SimpleStringProperty(puesto);
                    } catch (Exception e) {
                        return new SimpleStringProperty("NN");
                    }
                }
            });

            initCellAccion();
        } catch (Exception e) {
            ar.nex.util.SaeDialog.showException(e);
        }
    }

    private void initCellAccion() {
        colAccion.setCellValueFactory(new PropertyValueFactory<>("Accion"));
        Callback<TableColumn<Empleado, String>, TableCell<Empleado, String>> cellFactory
                = //
                (final TableColumn<Empleado, String> param) -> {
                    final TableCell<Empleado, String> cell = new TableCell<Empleado, String>() {
                final CheckBox btn = new CheckBox();
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            if (btn.isSelected()) {
                                listEmpleadoSelect.add(getTableView().getItems().get(getIndex()));
                                getTableRow().styleProperty().set("-fx-font-weight: bold;");
                            } else {
                                listEmpleadoSelect.remove(getTableView().getItems().get(getIndex()));
                                getTableRow().styleProperty().set("-fx-font-weight: normal;");
                            }
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    return cell;
                };
        colAccion.setCellFactory(cellFactory);

    }

    public void loadData(long id) {
        try {
            clearAll();
            List<Empleado> lst = jpa.getEmpleado().findEmpleadoEntities();
            for (Empleado item : lst) {
                if ((item.getEmpresa().getIdEmpresa() == id) || (id == 0)) {
                    if ((item.getSeguro() == null)) {
                        data.add(item);
                    } else {
                        Date now = new Date();
                        if (now.after(item.getSeguro().getHasta())) {
                            data.add(item);
                        }
                    }
                }
            }
            table.setItems(data);
        } catch (Exception e) {
            clearAll();
            UtilDialog.showException(e);
        }

    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Empleado>) user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Empleado> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    private void guardar(ActionEvent e) {
        try {
            if (!listEmpleadoSelect.isEmpty()) {
                listEmpleadoSelect.forEach(new Consumer<Empleado>() {
                    @Override
                    public void accept(Empleado e) {
                        seguro.getEmpleadoList().add(e);
                    }
                }
                );
                jpa.getSeguro().edit(seguro);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
        }
    }

}
