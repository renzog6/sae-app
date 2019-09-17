package ar.nex.usuario;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class UsuarioEditController implements Initializable {

    @FXML
    private TextField boxEmpleado;
    @FXML
    private TextField boxUser;
    @FXML
    private TextField boxPass;
    @FXML
    private TextField boxEmail;
    @FXML
    private TextField boxGrupo;
    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
