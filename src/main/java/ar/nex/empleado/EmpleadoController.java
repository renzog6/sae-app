package ar.nex.empleado;

import ar.nex.app.SaeUtils;
import ar.nex.entity.empleado.Empleado;
import ar.nex.login.LoginController;
import ar.nex.service.JpaService;
import ar.nex.util.UtilDialog;
import java.io.IOException;
import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EmpleadoController implements Initializable {

    public EmpleadoController() {
        this.filteredData = new FilteredList<>(data);
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/empleado/EmpleadoList.fxml"));
            root.setStyle("/css/empleado.css");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpEmpleado;
    @FXML
    private TextField searchBox;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnPassword;
    @FXML
    private Button btnHistorial;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblPedido;
    @FXML
    private Label lblHistorial;

    private final ObservableList<Empleado> data = FXCollections.observableArrayList();
    private final FilteredList<Empleado> filteredData;
    private Empleado empleadoSelect;

    @FXML
    private TableView<Empleado> table;
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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            btnAdd.setOnAction(e -> add());
            btnEdit.setOnAction(e -> edit());

            startTask();
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    public void startTask() {
        // Create a Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                jpa = new JpaService();
                initTable();
                loadData();
            }
        };
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void clearAll() {
        data.clear();
        searchBox.clear();
        empleadoSelect = null;
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
                        Integer edad = SaeUtils.getEdad(param.getValue().getNacimiento());
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
                        Integer edad = SaeUtils.getEdad(param.getValue().getFechaAlta());
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
                        String date = SaeUtils.getDateString(param.getValue().getFechaAlta());
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

        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    private void loadData() {
        try {
            clearAll();
            EntityManager em = jpa.getFactory().createEntityManager();
            TypedQuery<Empleado> query
                    = em.createQuery("SELECT c FROM Empleado c"                            
                            + " ORDER BY c.apellido ASC", Empleado.class);
            List<Empleado> lst = query.getResultList();
            //List<Empleado> lst = jpa.getEmpleado().findEmpleadoEntities();
            lst.forEach((item) -> {
                data.add(item);
            });
            table.setItems(data);
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Empleado>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (item.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Empleado> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        empleadoSelect = (Empleado) table.getSelectionModel().getSelectedItem();
        //lblSelect.setText("Select: " + empleadoSelect.getNombre());
    }

    private void add() {
        empleadoSelect = null;
        edit();
    }

    private void edit() {
        try {
            Stage dialog = new Stage();
            if (empleadoSelect != null) {
                dialog.setTitle("Editar Datos de Empleado");
            } else {
                dialog.setTitle("Alta de Empleado");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empleado/EmpleadoEdit.fxml"));
            EmpleadoEditController controller = new EmpleadoEditController(empleadoSelect);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();
            this.loadData();

        } catch (IOException e) {
            System.err.print(e);
        }
    }
}
