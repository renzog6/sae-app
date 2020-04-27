package ar.nex.equipo;

import ar.nex.entity.equipo.EquipoModelo;
import ar.nex.jpa.EquipoModeloJpaController;
import ar.nex.jpa.service.JpaService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class ModeloDialogController implements Initializable {

    public ModeloDialogController(EquipoModelo modelo) {
        this.modelo = modelo;
    }

    @FXML
    private AnchorPane apModelo;
    @FXML
    private TextField boxModelo;
    @FXML
    private TextField boxDescripcion;
    @FXML
    private TextField boxAnio;
    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private final EquipoModelo modelo;
    private EquipoModeloJpaController jpaModelo;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        jpaModelo = new JpaService().getEquipoModelo();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxModelo.requestFocus();
            }
        });

        btnGuardar.setOnAction(e -> guardar(e));
        btnCancelar.setOnAction(e ->cancelar(e));
    }

    @FXML
    private void guardar(ActionEvent event) {
        try {
            modelo.setNombre(boxModelo.getText());
            modelo.setDescripcion(boxDescripcion.getText());
            modelo.setAnio(Integer.parseInt(boxAnio.getText()));
            modelo.setInfo(boxInfo.getText());

            jpaModelo.create(modelo);

            cancelar(event);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

}
