package ar.nex.empleado;

import ar.nex.app.SaeUtils;
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empleado.EmpleadoCategoria;
import ar.nex.entity.empleado.EmpleadoPuesto;
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.equipo.gasto.GasoilMovimiento;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.DialogController;
import ar.nex.pedido.EstadoPedido;
import ar.nex.service.JpaService;
import ar.nex.ubicacion.DireccionEditController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EmpleadoEditController implements Initializable {

    public EmpleadoEditController(Empleado empleado) {
        this.empleado = empleado;
    }

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tbPersonal;
    @FXML
    private Tab tbLaboral;

    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxApellido;
    @FXML
    private TextField boxDni;
    @FXML
    private TextField boxDireccion;
    @FXML
    private TextField boxContacto;
    @FXML
    private TextField boxHijos;
    @FXML
    private TextField boxEmpresa;
    @FXML
    private TextField boxCuil;
    @FXML
    private TextField boxContactoLaboral;
    @FXML
    private TextField boxInfo;

    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private DatePicker dpFechaAlta;

    @FXML
    private ComboBox<?> cbSexo;
    @FXML
    private ComboBox<?> cbEstadoCivil;
    @FXML
    private ComboBox<EmpleadoCategoria> cbCategoria;
    @FXML
    private ComboBox<EmpleadoPuesto> cbPuesto;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnAtras;
    @FXML
    private Button btnDireccion;
    @FXML
    private Button btnContactoPersonal;
    @FXML
    private Button btnContactoLaboral;

    private  ObservableList<EmpleadoPuesto> dataPuesto = FXCollections.observableArrayList();
    private  ObservableList<EmpleadoCategoria> dataCategoria = FXCollections.observableArrayList();

    private JpaService jpa;
    private Empleado empleado;
    private Direccion direccion;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            jpa = new JpaService();
            
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            tabPane = new TabPane(tbPersonal, tbLaboral);
            //tabPane.getTabs().addAll(tbPersonal, tbLaboral);
            btnSiguiente.setOnAction(e -> tabPane.getSelectionModel().select(tbLaboral));
            btnAtras.setOnAction(e -> tabPane.getSelectionModel().select(tbPersonal));

            btnDireccion.setOnAction(e -> addDirecion());

            loadDataCatogoria();
            loadDataPuesto();
            initControls();

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    private void initControls() {
        try {
            if (empleado != null) {
                boxNombre.setText(empleado.getNombre());
                boxApellido.setText(empleado.getApellido());

                boxDni.setText(empleado.getDni());
                boxDni.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*")) {
                            boxDni.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }
                });

                boxHijos.setText(empleado.getHijo().toString());
                boxHijos.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*")) {
                            boxHijos.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }
                });

                dpFechaNacimiento.setValue(DateUtils.convertToLocalDateViaSqlDate(empleado.getNacimiento()));
                dpFechaAlta.setValue(DateUtils.convertToLocalDateViaSqlDate(empleado.getFechaAlta()));
                
                cbPuesto.getSelectionModel().select(empleado.getPuesto());                
                cbCategoria.getSelectionModel().select(empleado.getCategoria());
            } else {
                empleado = new Empleado();
                dpFechaNacimiento.setValue(LocalDate.now());
                dpFechaAlta.setValue(LocalDate.now());
                cbPuesto.getSelectionModel().select(dataPuesto.get(8));
                cbCategoria.getSelectionModel().select(dataCategoria.get(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataPuesto() {
        try {
            List<EmpleadoPuesto> lst = jpa.getEmpleadoPuesto().findEmpleadoPuestoEntities();
            lst.forEach((item) -> {
                dataPuesto.add(item);
            });
            cbPuesto.getItems().addAll(dataPuesto);
        } catch (Exception e) {
            ar.nex.util.DialogController.showException(e);
        }
    }

    private void loadDataCatogoria() {
        try {
            List<EmpleadoCategoria> lst = jpa.getEmpleadoCategoria().findEmpleadoCategoriaEntities();
            lst.forEach((item) -> {
                dataCategoria.add(item);
            });
            cbCategoria.getItems().addAll(dataCategoria);
        } catch (Exception e) {
            ar.nex.util.DialogController.showException(e);
        }
    }

    private void addDirecion() {
        try {
            Stage dialog = new Stage();
            dialog.setTitle("Direccion Empleado");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubicacion/DireccionEdit.fxml"));
            DireccionEditController controller = new DireccionEditController(null);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();
            if (controller.getDireccion() != null) {
                direccion = controller.getDireccion();
                boxDireccion.setText(direccion.toString());
            }

        } catch (IOException e) {
            System.err.print(e);

        }
    }

    private boolean isEmptytBox() {
        if (boxNombre.getText().trim().isEmpty()) {
            DialogController.errorDialog("Requiere valor", "Nombre es necesario");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxNombre.requestFocus();
                }
            });
            return true;
        } else if (boxApellido.getText().trim().isEmpty()) {
            DialogController.errorDialog("Requiere valor", "Apellido es necesario");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxApellido.requestFocus();
                }
            });
            return true;
        }

        return false;
    }

    private void guardar(ActionEvent e) {
        try {            
            if (!isEmptytBox()) {
                System.out.println("ar.nex.empleado.EmpleadoEditController.guardar()::: " + SaeUtils.capitailizeString(boxNombre.getText()));
                empleado.setNombre(SaeUtils.capitailizeString(boxNombre.getText()));
                empleado.setApellido(SaeUtils.capitailizeString(boxApellido.getText()));

                empleado.setHijo(Integer.parseInt(boxHijos.getText()));

                empleado.setNacimiento(SaeUtils.convertToDateViaSqlDate(dpFechaNacimiento.getValue()));
                empleado.setFechaAlta(SaeUtils.convertToDateViaSqlDate(dpFechaAlta.getValue()));

                empleado.setPuesto(cbPuesto.getValue());
                empleado.setCategoria(cbCategoria.getValue());
                if (empleado.getIdPersona() != null) {
                    jpa.getPersona().edit(empleado);
                } else {
                    jpa.getEmpleado().create(empleado);
                }

                ((Node) (e.getSource())).getScene().getWindow().hide();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            e.consume();
        }
    }
}
