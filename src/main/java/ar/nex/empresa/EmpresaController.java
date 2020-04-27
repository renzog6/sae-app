package ar.nex.empresa;

import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.jpa.service.JpaService;
import ar.nex.ubicacion.ContactoEditController;
import ar.nex.ubicacion.DireccionEditController;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
public class EmpresaController implements Initializable {

    private static final Logger LOG = Logger.getLogger(EmpresaController.class.getName());

    public EmpresaController() {
        this.filteredData = new FilteredList<>(data);
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/empresa/Empresa.fxml"));
            root.setStyle("/css/empresa.css");
        } catch (IOException ex) {
            Logger.getLogger(EmpresaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private ContextMenu cmDireccion;
    private Direccion direccionSelect;
    @FXML
    private ContextMenu cmContacto;
    private Contacto contactoSelect;

    @FXML
    private BorderPane bpEmpresa;
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
    private Label lblModelo;
    @FXML
    private Label lblPedido;

    private final ObservableList<Empresa> data = FXCollections.observableArrayList();
    private final FilteredList<Empresa> filteredData;
    private Empresa empresaSelect;

    @FXML
    private TableView<Empresa> table;
    @FXML
    private TableColumn<?, ?> colEmpresa;
    @FXML
    private TableColumn<?, ?> colCuit;
    @FXML
    private TableColumn<Empresa, String> colLocalidad;
    @FXML
    private TableColumn<Empresa, String> colRubro;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colAccion;

    @FXML
    private ListView<Direccion> lvDireccion;
    private ObservableList<Direccion> dataDireccion = FXCollections.observableArrayList();

    @FXML
    private ListView<Contacto> lvContacto;
    private ObservableList<Contacto> dataContacto = FXCollections.observableArrayList();

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOG.info("initialize(URL url, ResourceBundle rb)");
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
                initContextMenu();
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
        empresaSelect = null;
    }

    public void initTable() {
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCuit.setCellValueFactory(new PropertyValueFactory<>("cuit"));
        //colLocalidad.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colLocalidad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empresa, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Empresa, String> data) {
                StringProperty str = new SimpleStringProperty("NN");
                try {
                    if ((data.getValue().getDireccionList() != null) && (data.getValue().getDireccionList().size() >= 1)) {
                        str = new SimpleStringProperty(data.getValue().getDireccionList().get(0).getLocalidad().getNombre());
                    }
                    return str;
                } catch (Exception e) {
                    return str;
                }
            }
        });
        //colRubro.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        colRubro.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empresa, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Empresa, String> param) {
                StringProperty str = new SimpleStringProperty(new EmpesaUtil().getListaRubrosString(param.getValue().getRubroList()));
                return str;
            }
        });

        colInfo.setCellValueFactory(new PropertyValueFactory<>("observacion"));
        //colAccion.setCellValueFactory(new PropertyValueFactory<>("codigo"));
    }

    public void initContextMenu() {

        lvDireccion.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Direccion>() {
            @Override
            public void changed(ObservableValue<? extends Direccion> observable, Direccion oldValue, Direccion newValue) {
                direccionSelect = newValue;
            }
        });

        MenuItem item = new MenuItem("Nueva Direccion");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addDireccion();
            }
        });
        cmDireccion.getItems().add(item);

        item = new MenuItem("Editar");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (direccionSelect != null) {
                    editDireccion();
                }
            }
        });
        cmDireccion.getItems().add(item);

        lvContacto.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contacto>() {
            @Override
            public void changed(ObservableValue<? extends Contacto> observable, Contacto oldValue, Contacto newValue) {
                contactoSelect = newValue;
            }
        });

        item = new MenuItem("Nuevo Contacto");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addContacto();
            }
        });
        cmContacto.getItems().addAll(item);

        item = new MenuItem("Editar");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (contactoSelect != null) {
                    editContacto();
                }
            }
        });
        cmContacto.getItems().addAll(item);
    }

    public void loadData() {
        try {
            clearAll();
            List<Empresa> lst = jpa.getEmpresa().findEmpresaEntities();
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
            filteredData.setPredicate((Predicate<? super Empresa>) user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getCuit().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Empresa> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        empresaSelect = (Empresa) table.getSelectionModel().getSelectedItem();
        loadDataDireccion(empresaSelect.getDireccionList());
        loadDataContacto(empresaSelect.getContactoList());
        event.consume();
    }

    private void loadDataDireccion(List<Direccion> list) {
        dataDireccion = null;
        dataDireccion = new EmpesaUtil().getListaDirecciones(list);
        lvDireccion.setItems(dataDireccion);
        lvDireccion.refresh();
    }

    private void loadDataContacto(List<Contacto> list) {
        dataContacto = null;
        dataContacto = new EmpesaUtil().getListaContactos(list);
        lvContacto.setItems(dataContacto);
        lvContacto.refresh();
    }

    private void add() {
        empresaSelect = null;
        edit();
    }

    private void edit() {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/EmpresaDialog.fxml"));
            EmpresaDialogController controller = new EmpresaDialogController(empresaSelect);
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

    private void addDireccion() {
        direccionSelect = null;
        editDireccion();
    }

    private void editDireccion() {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubicacion/DireccionEdit.fxml"));
            DireccionEditController controller = new DireccionEditController(direccionSelect);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

            if (controller.getDireccion() != null) {
                empresaSelect.getDireccionList().add(controller.getDireccion());
                jpa.getEmpresa().edit(empresaSelect);
            }
            loadDataDireccion(empresaSelect.getDireccionList());
            loadData();
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    private void addContacto() {
        contactoSelect = null;
        editContacto();
    }

    private void editContacto() {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubicacion/ContactoEdit.fxml"));
            ContactoEditController controller = new ContactoEditController(contactoSelect);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

            if (controller.getContacto() != null) {
                empresaSelect.getContactoList().add(controller.getContacto());
                jpa.getEmpresa().edit(empresaSelect);
            }
            loadDataContacto(empresaSelect.getContactoList());
        } catch (Exception e) {
            System.err.print(e);
        }
    }
}
