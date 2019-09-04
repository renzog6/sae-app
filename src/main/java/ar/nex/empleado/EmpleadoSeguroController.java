package ar.nex.empleado;

import ar.nex.entity.Seguro;
import ar.nex.entity.equipo.Equipo;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.DialogController;
import ar.nex.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EmpleadoSeguroController implements Initializable {

    public EmpleadoSeguroController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/empleado/EmpleadoSeguro.fxml"));
            root.setStyle("/css/home.css");
        } catch (IOException ex) {
            Logger.getLogger(EmpleadoSeguroController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpEquipo;
    @FXML
    private TextField searchBox;
    @FXML
    private ComboBox<?> filtroEmpresa;
    @FXML
    private MenuButton mbMenu;

    private ObservableList<Seguro> dataSeguro = FXCollections.observableArrayList();
    private FilteredList<Seguro> filteredDataSeguro = new FilteredList<>(dataSeguro);
    private Seguro seguroSelect;
    @FXML
    private TableView<Seguro> tblSeguro;
    @FXML
    private TableColumn<?, ?> colCompania;
    @FXML
    private TableColumn<?, ?> colPoliza;
    @FXML
    private TableColumn<Seguro, String> colDesde;
    @FXML
    private TableColumn<Seguro, String> colHasta;
    @FXML
    private TableColumn<?, ?> colPrima;
    @FXML
    private TableColumn<?, ?> colMonto;
    @FXML
    private TableColumn<?, ?> colReferencia;

    private ObservableList<Equipo> dataEquipo = FXCollections.observableArrayList();
    private FilteredList<Equipo> filteredDataEquipo = new FilteredList<>(dataEquipo);
    private Equipo equipoSelect;
    @FXML
    private TableView<Equipo> tblEquipo;
    @FXML
    private TableColumn<?, ?> colTipo;
    @FXML
    private TableColumn<?, ?> colModelo;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colAnio;
    @FXML
    private TableColumn<?, ?> colChasis;
    @FXML
    private TableColumn<?, ?> colMotor;
    @FXML
    private TableColumn<?, ?> colPatente;
    @FXML
    private TableColumn<?, ?> colChofer;

    @FXML
    private AnchorPane menuEquipo;
    @FXML
    private Button btnAdd1;
    @FXML
    private Button btnEdit1;
    @FXML
    private Button btnDelete1;
    @FXML
    private AnchorPane menuSeguro;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

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
                initTableEquipo();
                initTableSeguro();
                loadDataSeguro();
            }
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void initTableEquipo() {
        try {
            colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
            colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
            colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
            colChasis.setCellValueFactory(new PropertyValueFactory<>("chasis"));
            colMotor.setCellValueFactory(new PropertyValueFactory<>("motor"));
            colPatente.setCellValueFactory(new PropertyValueFactory<>("patente"));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void initTableSeguro() {
        try {
            colCompania.setCellValueFactory(new PropertyValueFactory<>("compania"));
            colPoliza.setCellValueFactory(new PropertyValueFactory<>("poliza"));

            colDesde.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Seguro, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Seguro, String> param) {
                    try {
                        String date = DateUtils.getDateString(param.getValue().getDesde());
                        return new SimpleStringProperty(date);
                    } catch (Exception e) {
                        return new SimpleStringProperty("NN");
                    }
                }
            });

            colHasta.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Seguro, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Seguro, String> param) {
                    try {
                        String date = DateUtils.getDateString(param.getValue().getHasta());
                        return new SimpleStringProperty(date);
                    } catch (Exception e) {
                        return new SimpleStringProperty("NN");
                    }
                }
            });

            colPrima.setCellValueFactory(new PropertyValueFactory<>("prima"));
            colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
            colReferencia.setCellValueFactory(new PropertyValueFactory<>("referencia"));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void loadDataSeguro() {
        try {
            clearAll();
            List<Seguro> lst = jpa.getSeguro().findSeguroEntities();
            lst.forEach((item) -> {
                dataSeguro.add(item);
            });
            tblSeguro.setItems(dataSeguro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataEquipo(List<Equipo> list) {
        try {
            dataEquipo.clear();
            for (Equipo e : list) {
                dataEquipo.add(e);
            }
            tblEquipo.setItems(dataEquipo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Search(InputMethodEvent event) {
    }

    @FXML
    private void Search(KeyEvent event) {
    }

    @FXML
    private void filtroEmpresa(ActionEvent event) {
    }

    @FXML
    private void clickEnSeguro(MouseEvent event) {
        try {
            seguroSelect = tblSeguro.getSelectionModel().getSelectedItem();
            loadDataEquipo(seguroSelect.getEquipoList());
        } catch (Exception e) {
        }
    }

    @FXML
    private void clickEnEquipo(MouseEvent event) {
        try {
            equipoSelect = tblEquipo.getSelectionModel().getSelectedItem();
        } catch (Exception e) {
        }
    }

    public void clearAll() {
        dataEquipo.clear();
        dataSeguro.clear();
        searchBox.clear();
        equipoSelect = null;
        seguroSelect = null;
    }
}
