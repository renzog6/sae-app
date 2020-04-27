package ar.nex.equipo.transporte;

import ar.nex.entity.empleado.Empleado;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class ChoferEditVctoController implements Initializable {

    public ChoferEditVctoController(Empleado empleado) {
        this.empleado = empleado;
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/transporte/ChoferEditVcto.fxml"));
            root.setStyle("/css/transporte.css");
        } catch (IOException ex) {
            Logger.getLogger(TransporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private DatePicker dpCarnet;
    @FXML
    private DatePicker dpPsicofisico;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lblChofer;

    private JpaService jpa;
    private Empleado empleado;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        initControls();
    }

    /**
     * Inicializar todos los elementos a usar.
     */
    private void initControls() {
        try {
            lblChofer.setText(empleado.getNombreCompleto());

            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }

    }

    private void guardar(ActionEvent e) {
        try {

            System.out.println("ar.nex.equipo.transporte.ChoferEditVctoController.guardar()");
            ((Node) (e.getSource())).getScene().getWindow().hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
