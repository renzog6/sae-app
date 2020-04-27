package ar.nex.seguro;

import ar.nex.util.SaeDate;
import ar.nex.entity.Seguro;
import ar.nex.entity.SeguroTipo;
import ar.nex.entity.empleado.Empleado;

import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class SeguroEmpleadoController implements Initializable {

    public SeguroEmpleadoController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/seguro/SeguroEmpleadoList.fxml"));
            root.setStyle("/css/seguro.css");
        } catch (IOException ex) {
            Logger.getLogger(SeguroEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private TextField searchBox;
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

    private ObservableList<Empleado> dataEmpleado = FXCollections.observableArrayList();
    private FilteredList<Empleado> filteredDataEmpleado = new FilteredList<>(dataEmpleado);
    private Empleado equipoSelect;
    @FXML
    private TableView<Empleado> tblEmpleado;
    @FXML
    private TableColumn<Empleado, String> colEmpleado;
    @FXML
    private TableColumn<Empleado, String> colPuesto;
    @FXML
    private TableColumn<Empleado, String> colFechaAlta;
    @FXML
    private TableColumn<Empleado, String> colAntiguedad;
    @FXML
    private TableColumn<Empleado, String> colCategoria;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TableColumn<Empleado, String> colEdad;

    @FXML
    private AnchorPane menuEmpleado;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblEmpleado;
    @FXML
    private AnchorPane menuSeguro;
    @FXML
    private Button btnAddSeguro;
    @FXML
    private Button btnEditSeguro;
    @FXML
    private Button btnDelSeguro;

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
            UtilDialog.showException(e);
        }
    }

    public void startTask() {
        // Create a Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                jpa = new JpaService();
                initButton();
                initTableEmpleado();
                initTableSeguro();
                loadDataSeguro();
            }
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void initButton() {
        btnAddSeguro.setOnAction(e -> addSeguro());
        btnEditSeguro.setOnAction(e -> editSeguro());
        //btnDelSeguro.setOnAction(e -> delSeguro());
        btnAdd.setOnAction(e -> addEmpleadoAlSeguro());
        btnDelete.setOnAction(e -> delEmpleadoDelSeguro());
    }

    private void initTableEmpleado() {
      try {
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
                        Integer edad = SaeDate.getEdad(param.getValue().getFechaAlta());
                        return new SimpleStringProperty(edad.toString());
                    } catch (Exception e) {
                        return new SimpleStringProperty("XX");
                    }
                }
            });

            colFechaAlta.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Empleado, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Empleado, String> param) {
                    try {
                        String date = SaeDate.getDateString(param.getValue().getFechaAlta());
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
            ar.nex.util.SaeDialog.showException(e);
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
            TypedQuery<Seguro> query
                    = jpa.getFactory().createEntityManager().createQuery("SELECT s FROM Seguro s WHERE s.tipo= :t", Seguro.class)
                            .setParameter("t", SeguroTipo.EMPLEADO);
            List<Seguro> lst = query.getResultList();

            lst.forEach((item) -> {
                dataSeguro.add(item);
            });
            tblSeguro.setItems(dataSeguro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataEmpleado(List<Empleado> list) {
        try {
            dataEmpleado.clear();
            for (Empleado e : list) {
                dataEmpleado.add(e);
            }
            tblEmpleado.setItems(dataEmpleado);
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
            loadDataEmpleado(seguroSelect.getEmpleadoList());
        } catch (Exception e) {
        }
    }

    @FXML
    private void clickEnEmpleado(MouseEvent event) {
        try {
            equipoSelect = tblEmpleado.getSelectionModel().getSelectedItem();
            lblEmpleado.setText(equipoSelect.toString());
        } catch (Exception e) {
            equipoSelect = null;
        }
    }

    private void clearAll() {
        dataEmpleado.clear();
        dataSeguro.clear();
        searchBox.clear();
        equipoSelect = null;
        seguroSelect = null;
        lblEmpleado.setText("?");
    }

    private void reloadAll() {
        try {
            jpa.getFactory().close();
            jpa = new JpaService();
            loadDataSeguro();
        } catch (Exception e) {

        }
    }

    private void addSeguro() {
        try {
            seguroSelect = new Seguro(SeguroTipo.EQUIPO);
            editSeguro();
        } catch (Exception e) {
            seguroSelect = null;
        }
    }

    private void editSeguro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/seguro/SeguroEdit.fxml"));
            SeguroEditController controller = new SeguroEditController(seguroSelect);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());
            Stage dialog = new Stage();
            dialog.setTitle("Seguro");
            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();
            clearAll();
            loadDataSeguro();
        } catch (Exception e) {
            seguroSelect = null;
        }
    }

    private void delEmpleadoDelSeguro() {
        try {
            if (UtilDialog.confirmDialog("Seguro que desea quietar???")) {
                equipoSelect.setSeguro(null);
                jpa.getEmpleado().edit(equipoSelect);
            }
            reloadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEmpleadoAlSeguro() {
        try {
            if (seguroSelect != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/seguro/SeguroEmpleadoEdit.fxml"));
                SeguroEmpleadoEditController controller = new SeguroEmpleadoEditController(seguroSelect);
                loader.setController(controller);

                Scene scene = new Scene(loader.load());
                Stage dialog = new Stage();
                dialog.setTitle("Seguro");
                dialog.setScene(scene);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.resizableProperty().setValue(Boolean.FALSE);

                dialog.showAndWait();
                clearAll();
                loadDataSeguro();
            } else {
                UtilDialog.errorDialog("Error...", "Seleccionar un Seguro!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            seguroSelect = null;
        }
    }
}
