package ar.nex.seguro;

import ar.nex.entity.Seguro;
import ar.nex.entity.equipo.Equipo;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
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
public class SeguroEquipoEditController implements Initializable {

    public SeguroEquipoEditController(Seguro seguro) {
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

    private ObservableList<Equipo> data = FXCollections.observableArrayList();
    private FilteredList<Equipo> filteredData = new FilteredList<>(data);
    private Equipo equipoSelect;
    private List<Equipo> listEquipoSelect;

    @FXML
    private TableView<Equipo> table;
    @FXML
    private TableColumn colAccion;
    @FXML
    private TableColumn<?, ?> colCategoria;
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
    private TableColumn<?, ?> colPatente;

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

    public void startTask() {
        // Create a Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                jpa = new JpaService();
                listEquipoSelect = new ArrayList<>();
                initTable();
                initCellAccion();
                loadData(0);
            }
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    public void clearAll() {
        data.clear();
        searchBox.clear();
        equipoSelect = null;
    }

    public void initTable() {
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colChasis.setCellValueFactory(new PropertyValueFactory<>("chasis"));
        colPatente.setCellValueFactory(new PropertyValueFactory<>("patente"));
        initCellAccion();
    }

    public void initCellAccion() {
        colAccion.setCellValueFactory(new PropertyValueFactory<>("Accion"));
        Callback<TableColumn<Equipo, String>, TableCell<Equipo, String>> cellFactory
                = //
                (final TableColumn<Equipo, String> param) -> {
                    final TableCell<Equipo, String> cell = new TableCell<Equipo, String>() {

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
                                listEquipoSelect.add(getTableView().getItems().get(getIndex()));
                                getTableRow().styleProperty().set("-fx-font-weight: bold;");
                            } else {
                                listEquipoSelect.remove(getTableView().getItems().get(getIndex()));
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
            List<Equipo> lst = jpa.getEquipo().findEquipoEntities();
            for (Equipo item : lst) {
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
            filteredData.setPredicate((Predicate<? super Equipo>) user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getModelo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getTipo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getCategoria().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Equipo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    private void guardar(ActionEvent e) {
        try {
            if (!listEquipoSelect.isEmpty()) {
                listEquipoSelect.forEach(new Consumer<Equipo>() {
                    @Override
                    public void accept(Equipo e) {                        
                        seguro.getEquipoList().add(e);
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
