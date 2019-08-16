package ar.nex.empleado;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class PersonaEditController implements Initializable {

    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxApellido;
    @FXML
    private TextField boxCUIL;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField boxDNi;
    @FXML
    private TextField boxDireccion;
    @FXML
    private TextField boxContacto;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<?> cbSexo;
    @FXML
    private ComboBox<?> cbEstadoCivil;
    @FXML
    private TextField boxHijos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
