package ar.nex.empresa;

import ar.nex.entity.empresa.Rubro;
import ar.nex.jpa.service.JpaService;
import ar.nex.util.SaeDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RubroEditController implements Initializable {

    @FXML
    private AnchorPane apRubro;
    @FXML
    private Label lblTitulo;
    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxDescripcion;
    @FXML
    private TextField boxCodigo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Rubro rubro;

    /**
     *
     * @param rubro
     */
    public RubroEditController(Rubro rubro) {
        this.rubro = rubro;
    }

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        jpa = new JpaService();
        initControls();
    }

    private void initControls() {
        try {
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            if (rubro != null) {
                boxNombre.setText(rubro.getNombre());
                String str = rubro.getCodigo() != null? Integer.toString(rubro.getCodigo()) : "0";
                boxCodigo.setText(str);
                boxDescripcion.setText(rubro.getDescripcion());
            } else {
                rubro = new Rubro();
                boxNombre.setText("nuevo");
            }

        } catch (Exception ex) {
            SaeDialog.showException(ex);
        }
    }

    private void guardar(ActionEvent e) {
        try {
            rubro.setNombre(boxNombre.getText());
            rubro.setCodigo(Integer.parseInt(boxCodigo.getText()));
            rubro.setDescripcion(boxDescripcion.getText());

            if (rubro.getIdRubro() != null) {
                jpa.getRubro().edit(rubro);
            } else {
                jpa.getRubro().create(rubro);
            }
        } catch (Exception ex) {
            SaeDialog.showException(ex);
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
            e.consume();
        }
    }

}
