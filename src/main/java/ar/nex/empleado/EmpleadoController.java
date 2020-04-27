package ar.nex.empleado;

import ar.nex.app.MainApp;
import ar.nex.util.SaeDate;
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empleado.PersonaEstado;
import ar.nex.entity.ubicacion.ContactoTipo;
import ar.nex.login.LoginController;
import ar.nex.jpa.service.JpaService;
import ar.nex.util.SaeDialog;
import ar.nex.util.SaeUtil;
import java.io.IOException;
import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
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
import javafx.scene.control.ComboBox;
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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EmpleadoController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(EmpleadoController.class.getName());

    /**
     * Controlador para la Lista de los Empleados
     */
    public EmpleadoController() {
        this.filteredData = new FilteredList<>(data);
    }

    /**
     *
     * @return un scene.Parent para mostrar
     */
    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/empleado/EmpleadoList.fxml"));
            root.setStyle("/css/empleado.css");
        } catch (IOException ex) {
            LOG.log(Level.ERROR, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpEmpleado;
    @FXML
    private TextField searchBox;
    @FXML
    private ComboBox filtroEstado;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblDomicilio;

    private final ObservableList<Empleado> data = FXCollections.observableArrayList();
    private final FilteredList<Empleado> filteredData;
    private Empleado empleadoSelect;

    @FXML
    private TableView<Empleado> table;
    @FXML
    private TableColumn<Empleado, String> colEmpleado;
    @FXML
    private TableColumn<Empleado, String> colEdad;
    @FXML
    private TableColumn<Empleado, String> colPuesto;
    @FXML
    private TableColumn<Empleado, String> colFechaAlta;
    @FXML
    private TableColumn<Empleado, String> colAntiguedad;
    @FXML
    private TableColumn<Empleado, String> colCategoria;
    @FXML
    private TableColumn<Empleado, String> colCelular;
    @FXML
    private TableColumn<Empleado, String> colDomicilio;
    @FXML
    private TableColumn<?, ?> colEstado;

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOG.info(EmpleadoController.class.toGenericString());        
        try {            
            btnAdd.setOnAction(e -> add());
            btnEdit.setOnAction(e -> edit());
            btnDelete.setOnAction(e -> baja());
            initFiltroEstado();
            startTask();            
        } catch (Exception e) {
            e.printStackTrace();
           // SaeDialog.showException(e);
        }
    }

    private void startTask() throws Exception {
        
        // Create a Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                jpa = new JpaService();
                initTable();
                loadData(EmpleadoFiltro.ACTIVO);
            }
        };
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    /**
     *
     */
    private void clearAll() {
        data.clear();
        searchBox.clear();
        empleadoSelect = null;
    }

    private void initTable() {
        try {
            table.setTableMenuButtonVisible(true);

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
                        Integer antiguedad = SaeDate.getEdad(param.getValue().getFechaAlta());
                        return new SimpleStringProperty(antiguedad.toString());
                    } catch (Exception e) {
                        return new SimpleStringProperty("XX");
                    }
                }
            });

            colCelular.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        String celular = SaeUtil.getContactoDato(param.getValue().getContactoList(), ContactoTipo.CELULAR);
                        return new SimpleStringProperty(celular);
                    } catch (Exception e) {
                        return new SimpleStringProperty("SD");
                    }
                }
            });

            colDomicilio.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        return new SimpleStringProperty(param.getValue().getDomicilio().toString());
                    } catch (Exception e) {
                        return new SimpleStringProperty("SD");
                    }
                }
            });

            colFechaAlta.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        String date = "";
                        if (PersonaEstado.ACTIVO == param.getValue().getEstado().ACTIVO) {
                            date = SaeDate.getDateString(param.getValue().getFechaAlta());
                        } else {
                            date = SaeDate.getAntiguedad(param.getValue().getFechaAlta(), param.getValue().getFechaBaja()).toString();
                        }
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
            SaeDialog.showException(e);
        }
    }

    private void loadData(EmpleadoFiltro estado) {
        try {
            clearAll();
            EntityManager em = jpa.getFactory().createEntityManager();

            String query_string = "SELECT c FROM Empleado c";
            switch (estado) {
                case ACTIVO:
                    query_string += " WHERE c.estado LIKE '0'";
                    break;
                case BAJA:
                    query_string += " WHERE c.estado LIKE '1'";
                    break;
                case OTRO:
                    query_string += " WHERE c.estado LIKE '2'";
                    break;
            }
            query_string += " ORDER BY c.apellido ASC";

            TypedQuery<Empleado> query = em.createQuery(query_string, Empleado.class);

//            TypedQuery<Empleado> query
//                    = em.createQuery("SELECT c FROM Empleado c"
//                            + " WHERE c.estado LIKE '0'"
//                            + " ORDER BY c.apellido ASC", Empleado.class);
            List<Empleado> lst = query.getResultList();
            lst.forEach((item) -> {
                data.add(item);
            });
            table.setItems(data);
            table.requestFocus();
            table.getSelectionModel().selectFirst();
        } catch (Exception e) {
            SaeDialog.showException(e);
        }
    }

    private void initFiltroEstado() {
        try {
            if (filtroEstado.getItems().isEmpty()) {
                filtroEstado.getItems().addAll((ObservableList) FXCollections.observableArrayList(EmpleadoFiltro.values()));
                filtroEstado.getSelectionModel().select(EmpleadoFiltro.ACTIVO);
                filtroEstado.setOnAction(e -> filtroEstadoAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtroEstadoAction() {
        loadData((EmpleadoFiltro) filtroEstado.getSelectionModel().getSelectedItem());
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
        if (empleadoSelect.getDomicilio() != null) {
            lblDomicilio.setText("Domicilio: " + empleadoSelect.getDomicilio().toString());
        } else {
            lblDomicilio.setText("Domicilio:");
        }
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
            filtroEstadoAction();

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    /**
     *Llama a EmpleadoBajaController()
     */
    private void baja() {
        try {
            if (empleadoSelect != null) {
                if (empleadoSelect.getEstado() != PersonaEstado.BAJA) {
                    Stage dialog = new Stage();
                    dialog.setTitle("Baja de Empleado");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empleado/EmpleadoBaja.fxml"));
                    EmpleadoBajaController controller = new EmpleadoBajaController(empleadoSelect);
                    loader.setController(controller);

                    Scene scene = new Scene(loader.load());

                    dialog.setScene(scene);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.resizableProperty().setValue(Boolean.FALSE);

                    dialog.showAndWait();
                    filtroEstadoAction();
                } else {
                    SaeDialog.confirmDialog(empleadoSelect.getNombreCompleto() + " Ya esta dado de BAJA.");
                }
            } else {
                SaeDialog.confirmDialog("Debe selecionar un empleado.");
            }
        } catch (IOException e) {
            System.err.print(e);
        }
    }
}
