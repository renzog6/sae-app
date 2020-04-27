package ar.nex.ubicacion;

import ar.nex.entity.ubicacion.Direccion;
import ar.nex.entity.ubicacion.Localidad;
import ar.nex.jpa.service.JpaService;
import ar.nex.util.SaeDialog;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class DireccionEditController implements Initializable {

    public DireccionEditController(Direccion direccion) {
        this.direccion = direccion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    @FXML
    private AnchorPane apDireccion;
    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxCalle;
    @FXML
    private TextField boxNumero;
    @FXML
    private TextField boxCodigo;
    @FXML
    private TextField boxInfo;
    @FXML
    private TextField boxLocalidad;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Direccion direccion;

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        jpa = new JpaService();
        initControls();
    }

    private void initControls() {
        try {
            btnCancelar.setOnAction(e -> cancelar(e));
            btnGuardar.setOnAction(e -> guardar(e));

            loadLocalidad();

            if (direccion != null) {
                boxNombre.setText(direccion.getNombre());
                boxCalle.setText(direccion.getCalle());
                boxNumero.setText(direccion.getNumero());
                boxCodigo.setText(direccion.getCodigo());
                boxInfo.setText(direccion.getInfo());
                localidadSelect = direccion.getLocalidad() != null? direccion.getLocalidad(): new Localidad("");
                boxLocalidad.setText(localidadSelect.toString());                
            } else {
                direccion = new Direccion();
                boxNombre.setText("Principal");
            }

        } catch (Exception ex) {
            SaeDialog.showException(ex);
        }
    }

    private Localidad localidadSelect;

    private final ObservableList<Localidad> dataLocalidad = FXCollections.observableArrayList();

    private void loadLocalidad() {
        try {
            this.dataLocalidad.clear();
            List<Localidad> lst = jpa.getLocalidad().findLocalidadEntities();
            lst.forEach((item) -> {
                this.dataLocalidad.add(item);
            });

            AutoCompletionBinding<Localidad> auto = TextFields.bindAutoCompletion(boxLocalidad, dataLocalidad);

            auto.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<Localidad> event) -> {
                        localidadSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void guardar(ActionEvent e) {
        try {
            String txt;
            txt = boxNombre.getText() != null ? boxNombre.getText() : "";
            direccion.setNombre(txt);
            txt = boxCalle.getText() != null ? boxCalle.getText() : "";
            direccion.setCalle(txt);
            txt = boxNumero.getText() != null ? boxNumero.getText() : "";
            direccion.setNumero(txt);
            txt = boxCodigo.getText() != null ? boxCodigo.getText() : "";
            direccion.setCodigo(txt);
            txt = boxInfo.getText() != null ? boxInfo.getText() : "";
            direccion.setInfo(txt);
            direccion.setLocalidad(localidadSelect);

            if (direccion.getIdDireccion() != null) {
                jpa.getDireccion().edit(direccion);
                direccion = null;
            } else {
                jpa.getDireccion().create(direccion);
            }
        } catch (Exception ex) {
            SaeDialog.showException(ex);
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
            e.consume();
        }
    }

    private void cancelar(ActionEvent e) {
        direccion = null;
        ((Node) (e.getSource())).getScene().getWindow().hide();
        e.consume();
    }

}
