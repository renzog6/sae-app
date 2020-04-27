package ar.nex.equipo.transporte;

import ar.nex.entity.equipo.Transporte;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class TransporteEditController implements Initializable {

    public TransporteEditController(Transporte transporte) {
        this.transporte = transporte;
    }

    @FXML
    private Label lblTitulo;
    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxCamion;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField boxAcoplado;
    @FXML
    private TextField boxChofer;
    @FXML
    private Button btnCamion;
    @FXML
    private Button btnAcoplado;
    @FXML
    private Button btnChofer;

    private JpaService jpa;
    private Transporte transporte;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
        btnGuardar.setOnAction(e -> guardar(e));

        btnCamion.setOnAction(e -> select(0));
        btnAcoplado.setOnAction(e -> select(1));
        btnChofer.setOnAction(e -> select(2));

        initControls();
    }

    private void initControls() {
        try {
            if (transporte.getIdTransporte() != null) {
                lblTitulo.setText("Editar Transporte");
                boxNombre.setText(transporte.getNombre());
                boxCamion.setText(transporte.getCamion().toString());
                btnCamion.setDisable(true);
                boxAcoplado.setText(transporte.getAcoplado().toString());
                boxChofer.setText(transporte.getChofer().getNombreCompleto());
            } else {
                lblTitulo.setText("Nuevo Transporte");
                transporte = new Transporte();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValido() {
        try {
            if (boxNombre.getText().trim().isEmpty()) {
                UtilDialog.errorDialog("Requiere valor", "Nombre de Equipo");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boxNombre.requestFocus();
                    }
                });
                return true;
            } else if (boxCamion.getText().trim().isEmpty()) {
                UtilDialog.errorDialog("Requiere valor", "Seleccionar Camion");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boxCamion.requestFocus();
                    }
                });
                return true;
            } else if (boxAcoplado.getText().trim().isEmpty()) {
                UtilDialog.errorDialog("Requiere valor", "Seleccionar Acoplado");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boxAcoplado.requestFocus();
                    }
                });
                return true;
            } else if (boxChofer.getText().trim().isEmpty()) {
                UtilDialog.errorDialog("Requiere valor", "Selecionar Chofer");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boxChofer.requestFocus();
                    }
                });
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void guardar(ActionEvent e) {
        try {
            if (!isValido()) {
                transporte.setNombre(boxNombre.getText());
                jpa = new JpaService();
                if (transporte.getIdTransporte() != null) {
                    jpa.getTransporte().edit(transporte);
                } else {
                    jpa.getTransporte().create(transporte);
                }
                ((Node) (e.getSource())).getScene().getWindow().hide();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void select(int tipo) {
        try {
            Stage dialog = new Stage();
            dialog.setTitle("Seleccionar...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transporte/TransporteSelect.fxml"));
            TransporteSelectController controller = new TransporteSelectController(tipo);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                }
            });
            dialog.showAndWait();

            switch (tipo) {
                case 0:
                    transporte.setCamion(controller.getEquipo());
                    boxCamion.setText(transporte.getCamion().toString());
                    break;
                case 1:
                    transporte.setAcoplado(controller.getEquipo());
                    boxAcoplado.setText(transporte.getAcoplado().toString());
                    break;
                case 2:
                    transporte.setChofer(controller.getChofer());
                    boxChofer.setText(transporte.getChofer().getNombreCompleto());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
