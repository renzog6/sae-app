package ar.nex.equipo;

import ar.nex.entity.equipo.Equipo;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.equipo.util.EquipoToExel;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EquipoController implements Initializable {

    public EquipoController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/equipo/Equipo.fxml"));
            root.setStyle("/css/equipo.css");
        } catch (IOException ex) {
            Logger.getLogger(EquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private ComboBox filtroEmpresa;
    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    ObservableList<Equipo> data = FXCollections.observableArrayList();
    FilteredList<Equipo> filteredData = new FilteredList<>(data);
    Equipo equipoSelect;

    @FXML
    private TableView<Equipo> table;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colCategoria;
    @FXML
    private TableColumn<?, ?> colTipo;
    @FXML
    private TableColumn<?, ?> colModelo;
    @FXML
    private TableColumn<?, ?> colAnio;
    @FXML
    private TableColumn<?, ?> colChasis;
    @FXML
    private TableColumn<?, ?> colMotor;
    @FXML
    private TableColumn<?, ?> colPatente;
    @FXML
    private TableColumn<?, ?> colOtro;

    @FXML
    private MenuButton mbMenu;

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initControls();
        initMenu();
        initTable();
        initFiltro();
        loadData(0);
    }

    private void initMenu() {
        MenuItem item = new MenuItem("[- Exportar a Excel -]");
        item.setOnAction(e -> export());
        mbMenu.getItems().add(item);
        item = new MenuItem("Pedidos        ");
        mbMenu.getItems().add(item);
        item = new MenuItem("Stock Repuestos");
        mbMenu.getItems().add(item);
    }

    private void initControls() {
        jpa = new JpaService();

        btnAdd.setOnAction(e -> this.add());
        btnEdit.setOnAction(e -> this.edit());

        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Equipo>) e -> {
                try {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (e.getModelo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (e.getTipo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (e.getCategoria().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (e.getMarca().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (e.getPatente().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                } catch (Exception ex) {
                    return false;
                }

            });
        });
    }

    private void clearAll() {
        data.clear();
        equipoSelect = null;
    }

    private void initTable() {
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colChasis.setCellValueFactory(new PropertyValueFactory<>("chasis"));
        colMotor.setCellValueFactory(new PropertyValueFactory<>("motor"));
        colPatente.setCellValueFactory(new PropertyValueFactory<>("patente"));
        colOtro.setCellValueFactory(new PropertyValueFactory<>("otro"));
    }

    private void initFiltro() {
        ObservableList list = FXCollections.observableArrayList(EmpresaSelect.values());
        filtroEmpresa.getItems().addAll(list);
        filtroEmpresa.getSelectionModel().select(0);
    }

    private void loadData(long id) {
        try {
            clearAll();
            List<Equipo> lst = jpa.getEquipo().findEquipoEntities();
            for (Equipo item : lst) {
                if ((item.getEmpresa().getIdEmpresa() == id) || (id == 0)) {
                    data.add(item);
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

        SortedList<Equipo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void goSignOut(ActionEvent event) {
        ((Stage) signOut.getScene().getWindow()).close();
    }

    private void add() {
        equipoSelect = new Equipo();
        edit();
    }

    private void edit() {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/equipo/EquipoDialog.fxml"));
            EquipoDialogController controller = new EquipoDialogController(equipoSelect);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();
            this.loadData(0);
            Search();
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    @FXML
    private void Update(ActionEvent event) {
    }

    @FXML
    private void Delete(ActionEvent event) {
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        try {
            Equipo item = (Equipo) table.getSelectionModel().getSelectedItem();
            equipoSelect = jpa.getEquipo().findEquipo(item.getIdEquipo());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void filtroEmpresa() {
        EmpresaSelect empresa = (EmpresaSelect) filtroEmpresa.getSelectionModel().getSelectedItem();
        loadData(empresa.getId());
    }

    private void export() {
        new EquipoToExel().export(data, filtroEmpresa.getSelectionModel().getSelectedItem().toString());
    }
}
