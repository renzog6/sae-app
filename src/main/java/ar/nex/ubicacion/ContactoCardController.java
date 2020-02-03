package ar.nex.ubicacion;

import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.ubicacion.ContactoTipo;
import ar.nex.service.JpaService;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
public class ContactoCardController implements Initializable {
    
    public ContactoCardController() {
        //ver
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Contacto> getContactoList() {
        return contactoList;
    }
    
    @FXML
    private AnchorPane apContactoCard;
    @FXML
    private Label lblTitulo;
    @FXML
    private TextField boxEmail;
    @FXML
    private TextField boxFijo;
    @FXML
    private TextField boxCelular;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    
    private String nombre;
    
    private List<Contacto> contactoList;
    
    private Contacto celular, email, fijo;

    //private JpaService jpa;
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
            //jpa = new JpaService();
            lblTitulo.setText("Contacto " + nombre);
            
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));
            
            celular = email = fijo = null;
            
            contactoList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void guardar(ActionEvent e) {
        try {
            contactoList = new ArrayList<>();
            
            if (!boxCelular.getText().trim().isEmpty()) {
                celular = new Contacto(nombre);
                celular.setTipo(ContactoTipo.CELULAR);
                celular.setDato(boxCelular.getText());
                contactoList.add(celular);
            }
            
            if (!boxEmail.getText().trim().isEmpty()) {
                email = new Contacto(nombre);
                email.setTipo(ContactoTipo.EMAIL);
                email.setDato(boxEmail.getText());
                contactoList.add(email);
            }
            
            if (!boxFijo.getText().trim().isEmpty()) {
                fijo = new Contacto(nombre);
                fijo.setTipo(ContactoTipo.FIJO);
                fijo.setDato(boxFijo.getText());
                contactoList.add(fijo);
            }
            
            ((Node) (e.getSource())).getScene().getWindow().hide();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            e.consume();
        }
    }
    
}
