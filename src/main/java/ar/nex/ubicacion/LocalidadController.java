package ar.nex.ubicacion;

import ar.nex.empresa.EmpresaController;
import ar.nex.entity.ubicacion.Localidad;
import ar.nex.jpa.service.JpaService;
import ar.nex.util.SaeDialog;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class LocalidadController implements Initializable {

    public LocalidadController() {
        this.filteredData = new FilteredList<>(data);
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/ubicacion/LocalidadList.fxml"));
            root.setStyle("/css/localidad.css");
        } catch (IOException ex) {
            Logger.getLogger(EmpresaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpLocalidad;
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
    private Label lblSelect;

    private final ObservableList<Localidad> data = FXCollections.observableArrayList();
    private final FilteredList<Localidad> filteredData;
    private Localidad localidadSelect;

    @FXML
    private TableView<Localidad> table;
    @FXML
    private TableColumn<?, ?> colLocalidad;
    @FXML
    private TableColumn<?, ?> colCPostal;
    @FXML
    private TableColumn<?, ?> colProvincia;
    @FXML
    private TableColumn<Localidad, String> colPCodigo;
    @FXML
    private TableColumn<?, ?> colInfo;

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
            SaeDialog.showException(e);
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
        localidadSelect = null;
    }

    private void initTable() {
        try {
            colLocalidad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colCPostal.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));

            colProvincia.setCellValueFactory(new PropertyValueFactory<>("provincia"));
            colPCodigo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Localidad, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Localidad, String> param) {
                    StringProperty str = new SimpleStringProperty(param.getValue().getProvincia().getCodigo());
                    return str;
                }
            });
            colInfo.setCellValueFactory(new PropertyValueFactory<>("idLocalidad"));

        } catch (Exception e) {
            SaeDialog.showException(e);
        }
    }

    private void loadData() {
        try {
            clearAll();
            List<Localidad> lst = jpa.getLocalidad().findLocalidadEntities();
            lst.forEach((item) -> {
                data.add(item);
            });
            table.setItems(data);
        } catch (Exception e) {
            SaeDialog.showException(e);
        }
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Localidad>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (item.getProvincia().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Localidad> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        localidadSelect = (Localidad) table.getSelectionModel().getSelectedItem();
        lblSelect.setText("Select: " + localidadSelect.getNombre());
    }

    private void add() {
        localidadSelect = null;
        edit();
    }

    private void edit() {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubicacion/LocalidadEdit.fxml"));
            LocalidadEditController controller = new LocalidadEditController(localidadSelect);
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
