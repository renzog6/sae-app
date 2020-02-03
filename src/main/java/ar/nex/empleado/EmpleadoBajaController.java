package ar.nex.empleado;

import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empleado.PersonaEstado;
import ar.nex.service.JpaService;
import ar.nex.util.SaeDate;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EmpleadoBajaController implements Initializable {

    public EmpleadoBajaController(Empleado empleado) {
        this.empleado = empleado;
    }

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<String> cbRazon;
    @FXML
    private Label lblEmpleado;
    @FXML
    private TextArea txtComentario;

    private Empleado empleado;

    private JpaService jpa;

    private final String listRazon[] = {"Despido", "Renuncia", "Otro"};

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

            lblEmpleado.setText(empleado.getNombreCompleto());

            dpFecha.setValue(LocalDate.now());

            cbRazon.getItems().addAll(FXCollections.observableArrayList(listRazon));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void guardar(ActionEvent e) {
        try {
            System.out.println("ar.nex.empleado.EmpleadoBajaController.guardar()");
            empleado.setEstado(PersonaEstado.BAJA);
            System.out.println("ar.nex.empleado.EmpleadoBajaController.guardar()" + empleado.getEstado().getValue());
            empleado.setFechaBaja(SaeDate.convertToDateViaSqlDate(dpFecha.getValue()));
            empleado.setInfo(txtComentario.getText());
            jpa.getPersona().edit(empleado);
            ((Node) (e.getSource())).getScene().getWindow().hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            e.consume();
        }
    }
}
