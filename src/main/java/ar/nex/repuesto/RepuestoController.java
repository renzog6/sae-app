package ar.nex.repuesto;

import ar.nex.entity.equipo.EquipoModelo;
import ar.nex.entity.equipo.Repuesto;
import ar.nex.equipo.EquipoController;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RepuestoController implements Initializable {

    public RepuestoController() {
        this.filteredData = new FilteredList<>(data);
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/repuesto/Repuesto.fxml"));
            root.setStyle("/css/repuesto.css");
        } catch (IOException ex) {
            Logger.getLogger(EquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    private UtilDialog dlg;

    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAddModelo;

    private final ObservableList<Repuesto> data = FXCollections.observableArrayList();
    private final FilteredList<Repuesto> filteredData;
    private Repuesto selectRepuesto;

    @FXML
    private TableView<Repuesto> table;
    @FXML
    private TableColumn<Repuesto, String> colEquipos;
    @FXML
    private TableColumn<?, ?> colCodigo;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colParte;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colStock;
    @FXML
    private TableColumn<Repuesto, String> colId;

    @FXML
    private Label lblModelo;
    @FXML
    private Label lblPedido;
    @FXML
    private Label lblCompra;

    private JpaService jpa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchBox.requestFocus();

        btnAdd.setOnAction(e -> this.add());
        btnEdit.setOnAction(e -> this.edit());
        btnAddModelo.setOnAction(e -> this.addModelo());

        jpa = new JpaService();
        initTable();
        loadData();
    }

    private void clearAll() {
        data.clear();
        searchBox.clear();
        selectRepuesto = null;
    }

    private void initTable() {
        System.out.println("ar.nex.repuesto.RepuestoController.initTable()");
        try {
            colId.setCellValueFactory(new Callback<CellDataFeatures<Repuesto, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<Repuesto, String> data) {
                    return new SimpleStringProperty(data.getValue().getIdRepuesto().toString());
                }
            }
            );

            colEquipos.setCellValueFactory(new Callback<CellDataFeatures<Repuesto, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<Repuesto, String> data) {
                    return new SimpleStringProperty(listaModelo(data.getValue()));
                }
            }
            );

            colCodigo.setCellValueFactory(
                    new PropertyValueFactory<>("codigo"));
            colDescripcion.setCellValueFactory(
                    new PropertyValueFactory<>("descripcion"));
            colParte.setCellValueFactory(
                    new PropertyValueFactory<>("parte"));
            colInfo.setCellValueFactory(
                    new PropertyValueFactory<>("info"));
            colMarca.setCellValueFactory(
                    new PropertyValueFactory<>("marca"));
            colStock.setCellValueFactory(
                    new PropertyValueFactory<>("stock"));

            table.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        OneClick();
                        if (mouseEvent.getClickCount() == 2) {
                            addToPedido();
                        }
                    }
                }
            });

            table.setOnKeyPressed(event -> {
                if (event.getCode().isArrowKey()) {
                    OneClick();
                    event.consume();
                }
                if (event.getCode() == KeyCode.ENTER) {
                    addToPedido();
                    event.consume();
                }
            });

            // initCellAccion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * public void initCellAccion() { try {
     *
     * colAccion.setCellValueFactory(new PropertyValueFactory<>("Accion"));
     *
     * Callback<TableColumn<Repuesto, String>, TableCell<Repuesto, String>>
     * cellFactory = // (final TableColumn<Repuesto, String> param) -> { final
     * TableCell<Repuesto, String> cell = new TableCell<Repuesto, String>() {
     *
     * final Button btn = new Button("+");
     *
     * @Override public void updateItem(String item, boolean empty) {
     * super.updateItem(item, empty); if (empty) { setGraphic(null);
     * setText(null); } else { btn.setOnAction(event -> { selectRepuesto =
     * getTableView().getItems().get(getIndex()); addToPedido(); });
     * setGraphic(btn); setText(null); } } }; return cell; };
     * colAccion.setCellFactory(cellFactory); } catch (Exception e) {
     *
     * e.printStackTrace(); } }
     */
    public void loadData() {
        try {
            clearAll();
            selectRepuesto = null;
            List<Repuesto> lst = jpa.getRepuesto().findRepuestoEntities();
            lst.forEach((item) -> {
                data.add(item);
            });
            table.setItems(data);
        } catch (Exception e) {
            System.out.println("ar.nex.repuesto.RepuestoController.loadData()");
            e.printStackTrace();
        }
    }

    @FXML
    private void Search() {
        try {
            searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Repuesto>) user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (user.getCodigo().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (user.getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (user.getParte().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Repuesto> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);

            searchBox.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    table.requestFocus();
                    table.getSelectionModel().selectFirst();
                    table.getFocusModel().focus(0);
                    OneClick();
                    event.consume();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goSignOut(ActionEvent event) {
        Stage stage = (Stage) signOut.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Delete(ActionEvent event) {
        try {
            if (dlg.confirmDialog("Confimar Eliminar : " + selectRepuesto.toString() + "???")) {
                jpa.getRepuesto().destroy(selectRepuesto.getIdRepuesto());
                dlg.showSuccess("Se Elimino Correctamente!!!");
            }
//            } else {
//                boxCodigo.clear();
//                boxDescripcion.clear();
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
        loadData();
    }

    @FXML
    private void selectRepuestoMarca(ActionEvent event) {
        // boxMarca.setText(dialog.selectRepuestoMarca());
    }

    @FXML
    private void OneClick() {
        try {
            Repuesto item = (Repuesto) table.getSelectionModel().getSelectedItem();
            selectRepuesto = jpa.getRepuesto().findRepuesto(item.getIdRepuesto());

            lblModelo.setText(listaModelo(selectRepuesto));
            //  lblPedido.setText(listaProvedor(selectRepuesto));
            if (selectRepuesto.getPedidoList().size() >= 1) {
                lblCompra.setText(ultimaCompra(selectRepuesto.getPedidoList().size() - 1));
            } else {
                lblCompra.setText("Ultima compra: sin registro");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String ultimaCompra(int index) {
        String ultima = "Ultima compra: ";
        try {
            if (index >= 0) {
                ultima += DateUtils.getDateString(selectRepuesto.getPedidoList().get(index).getFechaFin());
                ultima += " - " + selectRepuesto.getPedidoList().get(index).getCantidad();
                ultima += " - " + selectRepuesto.getPedidoList().get(index).getEmpresa().getNombre();
            }
            return ultima;
        } catch (Exception e) {
            return ultima;
        }
    }

    private String listaModelo(Repuesto r) {
        String list = null;
        try {
            if (!r.getModeloList().isEmpty()) {
                for (EquipoModelo item : r.getModeloList()) {
                    if (list == null) {
                        list = item.getNombre();
                    } else {
                        list = list + " / " + item.getNombre();
                    }
                }
            }
        } catch (Exception e) {
            list = "";
        }
        return list;
    }

    @FXML
    private void addToPedido() {
        System.out.println("ar.nex.pedido.RepuestoController.AddToPedido()");
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoPedidoDialog.fxml"));
            RepuestoPedidoDialogController controller = new RepuestoPedidoDialogController(selectRepuesto);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);

            stage.showAndWait();
            this.loadData();
            searchBox.requestFocus();

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    public void add() {
        selectRepuesto = new Repuesto();
        edit();
    }

    public void edit() {
        System.out.println("ar.nex.pedido.RepuestoController.edit()");
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoDialog.fxml"));
            RepuestoDialogController controller = new RepuestoDialogController(selectRepuesto);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);

            stage.showAndWait();
            this.loadData();

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    /**
     * Agrega un Modelo mas que usa este repuesto.
     */
    public void addModelo() {
        System.out.println("ar.nex.repuesto.RepuestoController.addModelo()");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoModeloSelect.fxml"));
            RepuestoModeloSelectController controller = new RepuestoModeloSelectController(selectRepuesto.getModeloList());
            loader.setController(controller);
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Seleccionar Modelos de Equipos");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.showAndWait();

            if (controller.getEquipoModeloList() != null) {
                selectRepuesto.setModeloList(controller.getEquipoModeloList());
                jpa.getRepuesto().edit(selectRepuesto);
            }

            this.loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
