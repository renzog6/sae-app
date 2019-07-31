package ar.nex.empleado;

import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empleado.Persona;
import ar.nex.login.LoginController;
import ar.nex.service.JpaService;
import ar.nex.util.DialogController;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

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
            root.setStyle("/fxml/usuario/Usuario.css");
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
    private TableColumn<Persona, String> colEmpleado;
    @FXML
    private TableColumn<?, ?> colPuesto;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colAntiguedad;
    @FXML
    private TableColumn<?, ?> colMenu;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TableColumn<?, ?> colAccion;

    private JpaService jpa;
    
    /**
     * Initializes the controller class.
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
            DialogController.showException(e);
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
            colEmpleado.setCellValueFactory(new PropertyValueFactory<>("Empleado"));
            
                    
        } catch (Exception e) {
            DialogController.showException(e);
        }
    }

    private void loadData() {
        try {
            clearAll();
            List<Empleado> lst = jpa.getEmpleado().findEmpleadoEntities();
            lst.forEach((item) -> {
                data.add(item);
            });
            table.setItems(data);
        } catch (Exception e) {
            DialogController.showException(e);
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
                if (item.getPersona().getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (item.getPersona().getNombre().toLowerCase().contains(lowerCaseFilter)) {
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/EmpleadoEdit.fxml"));
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
