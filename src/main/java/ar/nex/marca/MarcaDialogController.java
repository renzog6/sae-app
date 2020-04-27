package ar.nex.marca;

import ar.nex.entity.Marca;
import ar.nex.equipo.gasto.GasoilController;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.MarcaJpaController;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class MarcaDialogController implements Initializable {

    public MarcaDialogController(Marca m) {
        this.marca = m;
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/marca/MarcaEdit.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(GasoilController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxInfo;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Marca marca;

    private MarcaJpaController jpaMarca;

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
        btnGuardar.setOnAction(e -> guardar(e));
        btnCancelar.setOnAction(e -> cancelar(e));
    }

    private void initControls() {
        try {
            if (marca != null) {
                boxNombre.setText(marca.getNombre());
                boxInfo.setText(marca.getDescripcion());
            }else{
                marca = new Marca();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEmptytBox() {
        if (boxNombre.getText().trim().isEmpty()) {
            UtilDialog.errorDialog("Requiere valor", "Nombre");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxNombre.requestFocus();
                }
            });
            return true;
        } else {
            return false;
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        try {
            if (!isEmptytBox()) {
                marca.setNombre(boxNombre.getText());
                marca.setDescripcion(boxInfo.getText());

                jpaMarca = new JpaService().getMarca();
                if (marca.getIdMarca() != null) {
                    jpaMarca.edit(marca);
                } else {
                    jpaMarca.create(marca);
                }
                cancelar(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

}
