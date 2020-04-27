package ar.nex.empleado;

import ar.nex.util.SaeDate;
import ar.nex.entity.AdminEmpresa;
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empleado.EmpleadoCategoria;
import ar.nex.entity.empleado.EmpleadoPuesto;
import ar.nex.entity.empleado.EstadoCivil;
import ar.nex.entity.empleado.PersonaGenero;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.equipo.util.DateUtils;
import ar.nex.util.SaeDialog;
import ar.nex.jpa.service.JpaService;
import ar.nex.ubicacion.ContactoCardController;
import ar.nex.ubicacion.DireccionEditController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo O. Gorosito
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
    private ComboBox<PersonaGenero> cbGenero;
    @FXML
    private ComboBox<EstadoCivil> cbEstadoCivil;
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

    private final ObservableList<EmpleadoPuesto> dataPuesto = FXCollections.observableArrayList();
    private final ObservableList<EmpleadoCategoria> dataCategoria = FXCollections.observableArrayList();

    private JpaService jpa;
    private Empleado empleado;
    private Direccion direccion;
    private Empresa empresa;
    private List<Contacto> contactoPersonal, contactoLaboral;
    private final ObservableList<AdminEmpresa> dataEmpresa = FXCollections.observableArrayList();

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
            contactoPersonal = contactoLaboral = null;

            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            tabPane = new TabPane(tbPersonal, tbLaboral);
            btnSiguiente.setOnAction(e -> tabPane.getSelectionModel().select(tbLaboral));
            btnAtras.setOnAction(e -> tabPane.getSelectionModel().select(tbPersonal));

            btnDireccion.setOnAction(e -> addDirecion());
            btnContactoPersonal.setOnAction(e -> addContacto(true));
            btnContactoLaboral.setOnAction(e -> addContacto(false));

            loadDataCatogoria();
            loadDataPuesto();
            initControls();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initControls() {
        try {
            loadDataEmpresa();
            AutoCompletionBinding<AdminEmpresa> autoCodigo = TextFields.bindAutoCompletion(boxEmpresa, dataEmpresa);
            autoCodigo.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<AdminEmpresa> event) -> {
                empresa = event.getCompletion().getEmpresa();
            }
            );

            cbGenero.getItems().addAll((ObservableList) FXCollections.observableArrayList(PersonaGenero.values()));
            cbEstadoCivil.getItems().addAll((ObservableList) FXCollections.observableArrayList(EstadoCivil.values()));

            if (empleado != null) {
                //Datos personales
                System.out.println("ar.nex.empleado.EmpleadoEditController.initControls():::::" + empleado.getContactoList().size());
                boxApellido.setText(empleado.getApellido());
                boxNombre.setText(empleado.getNombre());
                dpFechaNacimiento.setValue(DateUtils.convertToLocalDateViaSqlDate(empleado.getNacimiento()));

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

                cbGenero.getSelectionModel().select(empleado.getGenero());
                cbEstadoCivil.getSelectionModel().select(empleado.getEstadoCivil());

                if (empleado.getDomicilio() != null) {
                    boxDireccion.setText(empleado.getDomicilio().toString());
                }

                //Datos laborales
                boxEmpresa.setText(empleado.getEmpresa().getNombre());
                empresa = empleado.getEmpresa();
                dpFechaAlta.setValue(DateUtils.convertToLocalDateViaSqlDate(empleado.getFechaAlta()));
                boxCuil.setText(empleado.getCuil());
                cbPuesto.getSelectionModel().select(empleado.getPuesto());
                cbCategoria.getSelectionModel().select(empleado.getCategoria());

            } else {
                empleado = new Empleado();
                direccion = null;
                boxHijos.setText("0");
                dpFechaNacimiento.setValue(LocalDate.now());
                dpFechaAlta.setValue(LocalDate.now());
                cbPuesto.getSelectionModel().select(dataPuesto.get(8));
                cbCategoria.getSelectionModel().select(dataCategoria.get(4));
                cbGenero.getSelectionModel().select(PersonaGenero.MASCULICNO);
                cbEstadoCivil.getSelectionModel().select(EstadoCivil.SOLTERO);
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
            ar.nex.util.SaeDialog.showException(e);
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
            ar.nex.util.SaeDialog.showException(e);
        }
    }

    private void loadDataEmpresa() {
        try {
            List<AdminEmpresa> lst = jpa.getAdminEmpresa().findAdminEmpresaEntities();
            lst.forEach((item) -> {
                dataEmpresa.add(item);
            });
        } catch (Exception e) {
            ar.nex.util.SaeDialog.showException(e);
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

    private void addContacto(boolean isPersonal) {//true=personal y false=laboral
        try {
            Stage dialog = new Stage();
            dialog.setTitle("Contacto");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubicacion/ContactoCard.fxml"));
            ContactoCardController controller = new ContactoCardController();

            if (isPersonal) {
                controller.setNombre("Personal");
            } else {
                controller.setNombre("Laboral");
            }

            loader.setController(controller);
            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();
            if (controller.getContactoList() != null) {
                if (isPersonal) {
                    contactoPersonal = new ArrayList<>();
                    contactoPersonal = controller.getContactoList();
                    contactoPersonal.forEach((t) -> {
                        boxContacto.setText(boxContacto.getText() + t.getTipo() + " ");
                    });
                } else {
                    contactoLaboral = new ArrayList<>();
                    contactoLaboral = controller.getContactoList();
                    contactoLaboral.forEach((t) -> {
                        boxContactoLaboral.setText(boxContactoLaboral.getText() + t.getTipo() + " ");
                    });
                }
            }
        } catch (IOException e) {
            System.err.print(e);

        }
    }

    private boolean isEmptytBox() {
        if (boxNombre.getText().trim().isEmpty()) {
            SaeDialog.errorDialog("Requiere valor", "Nombre es necesario");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxNombre.requestFocus();
                }
            });
            return true;
        } else if (boxApellido.getText().trim().isEmpty()) {
            SaeDialog.errorDialog("Requiere valor", "Apellido es necesario");
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
                //Datos Personales
                empleado.setApellido(SaeDate.capitailizeString(boxApellido.getText()));
                empleado.setNombre(SaeDate.capitailizeString(boxNombre.getText()));
                empleado.setNacimiento(SaeDate.convertToDateViaSqlDate(dpFechaNacimiento.getValue()));
                empleado.setGenero(cbGenero.getValue());
                empleado.setEstadoCivil(cbEstadoCivil.getValue());
                empleado.setHijo(Integer.parseInt(boxHijos.getText()));
                if (direccion != null) {
                    jpa.getDireccion().create(direccion);
                    empleado.setDomicilio(direccion);
                }

                if (contactoPersonal != null) {
                    Contacto c = contactoPersonal.get(0);
                    jpa.getContacto().create(c);
                    empleado.getContactoList().add(c);
                }

                //Datos Laborales
                empleado.setEmpresa(empresa);
                empleado.setFechaAlta(SaeDate.convertToDateViaSqlDate(dpFechaAlta.getValue()));
                empleado.setCuil(boxCuil.getText());
                empleado.setCategoria(cbCategoria.getValue());
                empleado.setPuesto(cbPuesto.getValue());

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
