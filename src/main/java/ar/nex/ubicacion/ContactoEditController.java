package ar.nex.ubicacion;

import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.ubicacion.ContactoTipo;
import ar.nex.jpa.service.JpaService;
import ar.nex.util.SaeDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class ContactoEditController implements Initializable {

    public ContactoEditController(Contacto contacto) {
        this.contacto = contacto;
    }

    public Contacto getContacto() {
        return contacto;
    }

    @FXML
    private AnchorPane apContacto;
    @FXML
    private Label lblTitulo;
    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxDato;

    @FXML
    private ComboBox<ContactoTipo> cmbTipo;

    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Contacto contacto;

    private ContactoTipo contactoTipo;

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
            btnCancelar.setOnAction(e -> cancelar(e));
            btnGuardar.setOnAction(e -> guardar(e));

            cmbTipo.setItems(FXCollections.observableArrayList(ContactoTipo.values()));
            cmbTipo.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<ContactoTipo>() {
                @Override
                public void changed(ObservableValue<? extends ContactoTipo> observable, ContactoTipo oldValue, ContactoTipo newValue) {
                    contactoTipo = newValue;
                }
            }
            );

            if (contacto != null) {
                boxNombre.setText(contacto.getNombre());
                boxInfo.setText(contacto.getInfo());
                boxDato.setText(contacto.getDato());

                contactoTipo = contacto.getTipo();
                cmbTipo.getSelectionModel().select(contactoTipo.getIndex());

            } else {
                contacto = new Contacto();
                boxNombre.setText("Principal");
            }

        } catch (Exception ex) {
            SaeDialog.showException(ex);
        }
    }

    private void guardar(ActionEvent e) {
        try {
            String txt;
            txt = boxNombre.getText() != null ? boxNombre.getText() : "";
            contacto.setNombre(txt);
            txt = boxInfo.getText() != null ? boxInfo.getText() : "";
            contacto.setInfo(txt);
            txt = boxDato.getText() != null ? boxDato.getText() : "";
            contacto.setDato(txt);

            contacto.setTipo(contactoTipo);

            if (contacto.getIdContacto() != null) {
                jpa.getContacto().edit(contacto);
                contacto = null;
            } else {
                jpa.getContacto().create(contacto);
            }
        } catch (Exception ex) {
            SaeDialog.showException(ex);
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
            e.consume();
        }
    }

    private void cancelar(ActionEvent e) {
        contacto = null;
        ((Node) (e.getSource())).getScene().getWindow().hide();
        e.consume();
    }

}
