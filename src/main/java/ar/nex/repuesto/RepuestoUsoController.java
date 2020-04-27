package ar.nex.repuesto;

import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.RepuestoStockDetalle;
import ar.nex.equipo.EquipoController;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RepuestoUsoController implements Initializable {

    public RepuestoUsoController() {
        this.filteredEquipo = new FilteredList<>(dataEquipo);
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/repuesto/RepuestoUso.fxml"));
            root.setStyle("/fxml/repuesto/RepuestoUso.css");
        } catch (IOException ex) {
            Logger.getLogger(EquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bRepuestoUso;

    @FXML
    private TextField boxBuscar;
    @FXML
    private Button btnMas;
    @FXML
    private Button btnEditar;

    @FXML
    private Label lblEquipoSelect;
    @FXML
    private Label lblRepuestoSelect;

    ObservableList<Equipo> dataEquipo = FXCollections.observableArrayList();
    FilteredList<Equipo> filteredEquipo = new FilteredList<>(dataEquipo);
    Equipo equipoSelect;
    @FXML
    private TableView<Equipo> tableEquipo;
    @FXML
    private TableColumn<?, ?> colEquipo;
    @FXML
    private TableColumn<?, ?> colModelo;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colInfo;

    ObservableList<RepuestoStockDetalle> dataStockDetalle = FXCollections.observableArrayList();
    // FilteredList<RepuestoStockDetalle> filteredStockDetalle = new FilteredList<>(dataStockDetalle);
    RepuestoStockDetalle stockDetalleSelect;
    @FXML
    private TableView<RepuestoStockDetalle> tableStockDetalle;
    @FXML
    private TableColumn<?, ?> colRepuesto;
    @FXML
    private TableColumn<?, ?> colCantidad;
    @FXML
    private TableColumn<RepuestoStockDetalle, Date> colFecha;
    @FXML
    private TableColumn<?, ?> colEInfo;

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        clearAll();
        initSevice();
        initTableEquipo();
        loadDataEquipo();
        initTableStockDetalle();

        btnMas.setOnAction(e -> this.add());
        btnEditar.setOnAction(e -> this.edit());
    }

    private void clearAll() {
        System.out.println("ar.nex.equipo.EquipoController.clearAll()");
        dataEquipo.clear();
        boxBuscar.clear();
        equipoSelect = null;
    }

    private void initSevice() {
        jpa = new JpaService();
    }

    private void initTableEquipo() {
        colEquipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("otro"));
    }

    private void loadDataEquipo() {
        try {
            clearAll();
            List<Equipo> lst = jpa.getEquipo().findEquipoEntities();
            lst.forEach((item) -> {
                dataEquipo.add(item);
            });
            tableEquipo.setItems(dataEquipo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataStockDetalle(List<RepuestoStockDetalle> listStockDetalle) {
        try {
            dataStockDetalle.clear();
            for (RepuestoStockDetalle item : listStockDetalle) {
                dataStockDetalle.add(item);
            }
            tableStockDetalle.setItems(dataStockDetalle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTableStockDetalle() {
        colRepuesto.setCellValueFactory(new PropertyValueFactory<>("repuesto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        //colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
        initCellFecha();
    }

    private void initCellFecha() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        Callback<TableColumn<RepuestoStockDetalle, Date>, TableCell<RepuestoStockDetalle, Date>> cellFactory
                = //
                (final TableColumn<RepuestoStockDetalle, Date> param) -> {
                    final TableCell<RepuestoStockDetalle, Date> cell = new TableCell<RepuestoStockDetalle, Date>() {

                @Override
                public void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || (item == null)) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
                        setText(fd.format(item));
                        setGraphic(null);
                    }
                }
            };
                    return cell;
                };
        colFecha.setCellFactory(cellFactory);
    }

    @FXML
    private void Search() {
        boxBuscar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredEquipo.setPredicate((Predicate<? super Equipo>) item -> {
                try {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (item.getModelo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (item.getTipo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (item.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            });
        });
        SortedList<Equipo> sortedData = new SortedList<>(filteredEquipo);
        sortedData.comparatorProperty().bind(tableEquipo.comparatorProperty());
        tableEquipo.setItems(sortedData);
    }

    @FXML
    private void onTableEquipo(MouseEvent event) {
        try {
            //Equipo item = (Equipo) tableEquipo.getSelectionModel().getSelectedItem();
            equipoSelect = (Equipo) tableEquipo.getSelectionModel().getSelectedItem(); //jpaEquipo.getEquipo().findEquipo(item.getIdEquipo());
            loadDataStockDetalle(equipoSelect.getRepuestoStockDetalleList());
            lblEquipoSelect.setText(equipoSelect.getTipo().getNombre() + " "
                    + equipoSelect.getMarca().getNombre() + " "
                    + equipoSelect.getModelo().getNombre() + " - "
                    + equipoSelect.getAnio());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void add() {
        try {
            if (equipoSelect == null) {
                UtilDialog.errorDialog("Uso Repuesto", "Debe selecionar un Equipo");
            } else {
                Stage dialog = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoUsoDialog.fxml"));
                RepuestoUsoAddController controller = new RepuestoUsoAddController(equipoSelect);
                loader.setController(controller);

                Scene scene = new Scene(loader.load());

                dialog.setScene(scene);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.resizableProperty().setValue(Boolean.FALSE);

                dialog.showAndWait();
                loadDataEquipo();
                loadDataStockDetalle(dataStockDetalle);
            }
        } catch (IOException e) {
            System.err.print(e);
            e.printStackTrace();
        }
    }

    @FXML
    private void onTableRepuesto(MouseEvent event) {
        stockDetalleSelect = (RepuestoStockDetalle) tableStockDetalle.getSelectionModel().getSelectedItem();
        lblRepuestoSelect.setText("Repuesto: "
                + stockDetalleSelect.getRepuesto().toString() + " [ Stock: "
                + stockDetalleSelect.getRepuesto().getStock() + " ]");
    }

    private void edit() {
        try {
            if (stockDetalleSelect == null) {
                UtilDialog.errorDialog("Uso Repuesto", "Debe selecionar un Repuesto");
            } else {
                Stage dialog = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoUsoEdit.fxml"));
                RepuestoUsoEditController controller = new RepuestoUsoEditController(stockDetalleSelect);
                loader.setController(controller);

                Scene scene = new Scene(loader.load());

                dialog.setScene(scene);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.resizableProperty().setValue(Boolean.FALSE);

                dialog.showAndWait();
                loadDataEquipo();
                loadDataStockDetalle(dataStockDetalle);
            }
        } catch (IOException e) {
            System.err.print(e);
            e.printStackTrace();
        }
    }
}
