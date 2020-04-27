package ar.nex.equipo.transporte;

import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.Transporte;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class TransporteController implements Initializable {

    public TransporteController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/transporte/TransporteList.fxml"));
            root.setStyle("/css/transporte.css");
        } catch (IOException ex) {
            Logger.getLogger(TransporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpTransporte;
    @FXML
    private TextField searchBox;
    @FXML
    private MenuButton mbMenu;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEditChofer;
    @FXML
    private Button btnEditCamion;
    @FXML
    private Button btnEditAcoplado;

    private final ObservableList<Transporte> data = FXCollections.observableArrayList();
    private final FilteredList<Transporte> filteredData = new FilteredList<>(data);
    private Transporte transporteSelect;
    @FXML
    private TableView<Transporte> table;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<Transporte, String> colChofer;
    @FXML
    private TableColumn<Transporte, String> colCamion;
    @FXML
    private TableColumn<Transporte, String> colAcoplado;
    @FXML
    private TableColumn<?, ?> colInfo;

    @FXML
    GridPane gpDetalle;

    private JpaService jpa;
    private Equipo equipo;

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
            btnEditChofer.setOnAction(e -> editChofer());
            btnEditCamion.setOnAction(e -> editCamion());
            btnEditAcoplado.setOnAction(e -> editAcoplado());
            startTask();
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    private void startTask() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                jpa = new JpaService();
                iniTable();
                loadData();
            }
        };
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void iniTable() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        colChofer.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transporte, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transporte, String> param) {
                try {
                    String result = param.getValue().getChofer().getNombreCompleto();
                    return new SimpleStringProperty(result);
                } catch (Exception e) {
                    return new SimpleStringProperty("NNN");
                }
            }
        });

        colCamion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transporte, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transporte, String> param) {
                try {
                    String result = String.join("\n", param.getValue().getCamion().getPatente(),
                            param.getValue().getCamion().getAnio());
                    return new SimpleStringProperty(result);
                } catch (Exception e) {
                    return new SimpleStringProperty("CCC");
                }
            }
        });

        colAcoplado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transporte, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transporte, String> param) {
                try {
                    String result = String.join("\n", param.getValue().getAcoplado().getPatente(),
                            param.getValue().getAcoplado().getAnio());
                    return new SimpleStringProperty(result);
                } catch (Exception e) {
                    return new SimpleStringProperty("XXX");
                }
            }
        });

        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
    }

    private void loadData() {
        try {
            clearAll();
            List<Transporte> lst = jpa.getTransporte().findTransporteEntities();
            if (null != lst) {
                lst.forEach((item) -> {
                    data.add(item);
                });
                table.setItems(data);
            }
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        try {
            gpDetalle.getChildren().clear();
            transporteSelect = table.getSelectionModel().getSelectedItem();

            gpDetalle.add(getInfoChofer(0), 0, 0);
            gpDetalle.add(getInfoChofer(1), 0, 1);
            gpDetalle.add(getInfoChofer(2), 0, 2);;
            gpDetalle.add(getInfoChofer(3), 0, 3);

            equipo = transporteSelect.getCamion();
            gpDetalle.add(getInfoEquipo(0, equipo), 0, 6);
            gpDetalle.add(getInfoEquipo(1, equipo), 0, 7);
            gpDetalle.add(getInfoEquipo(2, equipo), 0, 8);
            gpDetalle.add(getInfoEquipo(3, equipo), 0, 9);
            gpDetalle.add(getInfoEquipo(4, equipo), 0, 10);

            equipo = transporteSelect.getAcoplado();
            gpDetalle.add(getInfoEquipo(0, equipo), 0, 12);
            gpDetalle.add(getInfoEquipo(1, equipo), 0, 13);
            gpDetalle.add(getInfoEquipo(2, equipo), 0, 14);
            gpDetalle.add(getInfoEquipo(3, equipo), 0, 15);
            gpDetalle.add(getInfoEquipo(4, equipo), 0, 16);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Label getInfoChofer(int i) {
        Label lbl = new Label();
        try {
            switch (i) {
                case 0:
                    lbl.setText(transporteSelect.getChofer().getNombreCompleto());
                    break;
                case 1:
                    lbl.setText(DateUtils.getEdad(transporteSelect.getChofer().getNacimiento()).toString());
                    break;
                case 2:
                    lbl.setText(DateUtils.getDateString(transporteSelect.getChofer().getFechaAlta()));
                    break;
                case 3:
                    lbl.setText("???");
                    break;
            }
        } catch (Exception e) {
            lbl.setText("???");
        }
        return lbl;
    }

    private Label getInfoEquipo(int i, Equipo e) {
        Label lbl = new Label();
        try {
            switch (i) {
                case 0:
                    lbl.setText(equipoDetalle(e));
                    break;
                case 1:
                    lbl.setText(DateUtils.getDateString(e.getSeguro().getHasta()));
                    break;
                case 2:
                    lbl.setText(DateUtils.getDateString(e.getDocumentacion().getRutaVto()));
                    break;
                case 3:
                    lbl.setText(DateUtils.getDateString(e.getDocumentacion().getTecnicaVto()));
                    break;
                case 4:
                    lbl.setText(DateUtils.getDateString(e.getDocumentacion().getPatenteVto()));
                    break;
            }
        } catch (Exception ex) {
            lbl.setText("???");
        }
        return lbl;
    }

    private String equipoDetalle(Equipo e) {
        return e.getMarca() + " " + e.getModelo() + " - " + e.getAnio();
    }

    private void add() {
        transporteSelect = new Transporte();
        edit();
    }

    private void edit() {
        try {
            if (transporteSelect != null) {
                Stage dialog = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transporte/TransporteEdit.fxml"));
                TransporteEditController controller = new TransporteEditController(transporteSelect);
                loader.setController(controller);

                Scene scene = new Scene(loader.load());

                dialog.setScene(scene);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.resizableProperty().setValue(Boolean.FALSE);

                dialog.showAndWait();
                loadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Transporte>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getInfo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (item.getInfo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Transporte> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    private void clearAll() {
        data.clear();
        searchBox.clear();
        transporteSelect = null;
    }

    private void editChofer() {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transporte/ChoferEditVcto.fxml"));
            ChoferEditVctoController controller = new ChoferEditVctoController(transporteSelect.getChofer());
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editCamion() {
        editEquipo(transporteSelect.getCamion());
    }

    private void editAcoplado() {
        editEquipo(transporteSelect.getAcoplado());
    }

    private void editEquipo(Equipo equipo) {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transporte/EquipoEditVcto.fxml"));
            EquipoEditVctoController controller = new EquipoEditVctoController(equipo);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
