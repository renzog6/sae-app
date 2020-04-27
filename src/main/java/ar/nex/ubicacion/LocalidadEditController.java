package ar.nex.ubicacion;

import ar.nex.entity.ubicacion.Localidad;
import ar.nex.entity.ubicacion.Provincia;
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
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class LocalidadEditController implements Initializable {

    public LocalidadEditController(Localidad localidad) {
        this.localidad = localidad;
    }

    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxCPostal;
    @FXML
    private TextField boxProvincia;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Localidad localidad;

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

            loadProvincia();

            if (localidad != null) {
                boxNombre.setText(localidad.getNombre());
                boxCPostal.setText(localidad.getCodigoPostal());
                provinciaSelect = localidad.getProvincia();
                boxProvincia.setText(provinciaSelect.getNombre());
            } else {
                localidad = new Localidad();
                boxNombre.setText("nueva");
            }

        } catch (Exception ex) {
            SaeDialog.showException(ex);
        }
    }

    private void guardar(ActionEvent e) {
        try {
            
            localidad.setNombre(boxNombre.getText());
            localidad.setCodigoPostal(boxCPostal.getText());
            localidad.setProvincia(provinciaSelect);

            if (localidad.getIdLocalidad() != null) {
                jpa.getLocalidad().edit(localidad);
            } else {
                jpa.getLocalidad().create(localidad);
            }
        } catch (Exception ex) {
            SaeDialog.showException(ex);
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
            e.consume();
        }
    }

    private Provincia provinciaSelect;

    private final ObservableList<Provincia> dataProvincia = FXCollections.observableArrayList();

    private void loadProvincia() {
        try {
            this.dataProvincia.clear();
            List<Provincia> lst = jpa.getProvincia().findProvinciaEntities();
            lst.forEach((item) -> {
                this.dataProvincia.add(item);
            });

            AutoCompletionBinding<Provincia> autoCategoria = TextFields.bindAutoCompletion(boxProvincia, dataProvincia);

            autoCategoria.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<Provincia> event) -> {
                        provinciaSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
