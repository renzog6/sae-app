package ar.nex.equipo.gasto;

import ar.nex.entity.equipo.Transporte;
import ar.nex.entity.equipo.gasto.Gasoil;
import ar.nex.equipo.transporte.TransporteController;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.ExportToExel;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class GasoilTransporteController implements Initializable {

    public GasoilTransporteController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/gasoil/GasoilTransporte.fxml"));
            root.setStyle("/css/gasoil.css");
        } catch (IOException ex) {
            Logger.getLogger(TransporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpGasoil;
    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private Button btnUpdate;

    private final ObservableList<String[]> data = FXCollections.observableArrayList();

    @FXML
    private TableView<String[]> table;
    @FXML
    private TableColumn<String[], String> colEquipo;
    @FXML
    private TableColumn<String[], String> colUltima;
    @FXML
    private TableColumn<String[], String> colKms;
    @FXML
    private TableColumn<String[], String> colCargas;
    @FXML
    private TableColumn<String[], String> colLitros;
    @FXML
    private TableColumn<String[], String> colPromedio;
    @FXML
    private TableColumn colAccion;

    private JpaService jpa;
    private EntityManager em;
    private final DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat formatter = new DecimalFormat("#,###.00");
    private double totalLitros = 0.0;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            btnUpdate.setOnAction(e -> loadData());
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
                em = jpa.getFactory().createEntityManager();
                initFecha();
                initTable();
                loadData();
            }
        };
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void initFecha() {
        LocalDate hoy = LocalDate.now();
        dpDesde.setValue(hoy.plusDays(-30));
        dpHasta.setValue(hoy);

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (dpDesde.getValue().compareTo(dpHasta.getValue()) > 0) {
                    dpDesde.setValue(dpHasta.getValue());
                }
            }
        };

        dpDesde.setOnAction(event);
        dpHasta.setOnAction(event);
    }

    private void initTable() {
        try {
            colEquipo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                    return new SimpleStringProperty((param.getValue()[0]));
                }
            });

            colUltima.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                    return new SimpleStringProperty(param.getValue()[1]);
                }
            });

            colKms.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                    return new SimpleStringProperty(param.getValue()[2]);
                }
            });

            colCargas.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                    return new SimpleStringProperty(param.getValue()[3]);
                }
            });

            colLitros.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                    return new SimpleStringProperty(param.getValue()[4]);
                }
            });

            colPromedio.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                    return new SimpleStringProperty(param.getValue()[5]);
                }
            });

            initCellAccion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initCellAccion() {
        colAccion.setCellValueFactory(new PropertyValueFactory<>("Accion"));
        Callback<TableColumn<String[], String>, TableCell<String[], String>> cellFactory
                = //
                (final TableColumn<String[], String> param) -> {
                    final TableCell<String[], String> cell = new TableCell<String[], String>() {
                final Button btn = new Button("+");

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            String[] select = getTableView().getItems().get(getIndex());
                            export(listGasoil(Long.parseLong(select[6])));
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

    private void loadData() {
        try {
            clearAll();
            List<Transporte> transportes = listTransporte();

            for (Transporte t : transportes) {
                List<Gasoil> listGasoil = listGasoil(t.getCamion().getIdEquipo());
                String[] item = {t.getCamion().getPatente(), "", "", "", "", "", t.getCamion().getIdEquipo().toString()};
                if (listGasoil != null) {
                    item[1] = fd.format(listGasoil.get(listGasoil.size() - 1).getFecha());
                    item[2] = formatter.format(listGasoil.get(listGasoil.size() - 1).getKms());
                    item[3] = listGasoil.size() + "";
                    item[4] = totalLitros(listGasoil);
                    item[5] = promedioGasoil(listGasoil);
                }
                totalLitros = 0.0;
                data.add(item);
            }
            table.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Transporte> listTransporte() {
        TypedQuery<Transporte> query
                = em.createQuery("SELECT t FROM Transporte t ORDER BY t.idTransporte ASC", Transporte.class);
        List<Transporte> results = query.getResultList();
        if (!results.isEmpty()) {
            return results;
        } else {
            return null;
        }
    }

    private List<Gasoil> listGasoil(Long camion) {
        try {
            Date desde = (Date) DateUtils.convertToDateViaSqlDate(dpDesde.getValue());
            Date hasta = (Date) DateUtils.convertToDateViaSqlDate(dpHasta.getValue());
            TypedQuery<Gasoil> query
                    = em.createQuery("SELECT c FROM Gasoil c"
                            + " WHERE c.equipo.idEquipo LIKE :camion"
                            + " AND c.fecha BETWEEN :start AND :end"
                            + " ORDER BY c.fecha ASC", Gasoil.class)
                            .setParameter("camion", camion)
                            .setParameter("start", desde)
                            .setParameter("end", hasta);
            List<Gasoil> results = query.getResultList();
            if (!results.isEmpty()) {
                return results;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String totalLitros(List<Gasoil> list) {
        double total = 0.0;
        for (Gasoil g : list) {
            total += g.getLitros();
        }
        totalLitros = total;
        return formatter.format(total);//total.toString();
    }

    private String promedioGasoil(List<Gasoil> list) {
        double promedio = 0.0;
        double kms = 0.0;
        if (list.size() > 1) {
            kms = list.get(list.size() - 1).getKms() - list.get(0).getKms();
            promedio = kms / (totalLitros - list.get(0).getLitros());
        }
        return formatter.format(promedio);
    }

    private void export(List<Gasoil> list) {
        new ExportToExel().exportGasoilTransporte(list);
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        event.consume();
    }

    private void clearAll() {
        data.clear();
    }

}
