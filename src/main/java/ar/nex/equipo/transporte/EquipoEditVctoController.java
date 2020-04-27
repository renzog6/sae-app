package ar.nex.equipo.transporte;

import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.EquipoDocumentacion;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
public class EquipoEditVctoController implements Initializable {

    public EquipoEditVctoController(Equipo equipo) {
        this.equipo = equipo;
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/transporte/EquipoEditVcto.fxml"));
            root.setStyle("/css/transporte.css");
        } catch (IOException ex) {
            Logger.getLogger(TransporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private DatePicker dpRuta;
    @FXML
    private DatePicker dpTecnica;
    @FXML
    private DatePicker dpPatente;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lblEquipo;

    private JpaService jpa;
    private final Equipo equipo;
    private EquipoDocumentacion doc;

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
            lblEquipo.setText(equipo.toString());
            jpa = new JpaService();            

            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            if (equipo.getDocumentacion() != null) {
                doc = equipo.getDocumentacion();
                dpRuta.setValue(DateUtils.convertToLocalDateViaSqlDate(doc.getRutaVto()));
                dpTecnica.setValue(DateUtils.convertToLocalDateViaSqlDate(doc.getTecnicaVto()));
                dpPatente.setValue(DateUtils.convertToLocalDateViaSqlDate(doc.getPatenteVto()));
            } else {
                doc = new EquipoDocumentacion();
                dpRuta.setValue(LocalDate.now());
                dpTecnica.setValue(LocalDate.now());
                dpPatente.setValue(LocalDate.now());
            }
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }

    }

    private void guardar(ActionEvent e) {
        try {
            doc.setRutaVto(DateUtils.convertToDateViaSqlDate(dpRuta.getValue()));
            doc.setTecnicaVto(DateUtils.convertToDateViaSqlDate(dpTecnica.getValue()));
            doc.setPatenteVto(DateUtils.convertToDateViaSqlDate(dpPatente.getValue()));
            if (doc.getIdDoc() != null) {
                jpa.getEquipoDocumentacion().edit(doc);
            } else {
                jpa.getEquipoDocumentacion().create(doc);
                equipo.setDocumentacion(doc);
                jpa.getEquipo().edit(equipo);
            }
            ((Node) (e.getSource())).getScene().getWindow().hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
