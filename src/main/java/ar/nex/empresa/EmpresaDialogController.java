package ar.nex.empresa;

import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.service.JpaService;
import ar.nex.ubicacion.DireccionEditController;
import ar.nex.util.SaeDialog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EmpresaDialogController implements Initializable {

    public EmpresaDialogController(Empresa empresa) {
        this.empresa = empresa;
    }

    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxRSocial;
    @FXML
    private TextField boxCUIT;
    @FXML
    private TextField boxObservacion;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button addDireccion;
    @FXML
    private Button addContacto;
    @FXML
    private TextField boxRubro;
    @FXML
    private Button addRubro;
    @FXML
    private TextField boxDireccion;
    @FXML
    private TextField boxContacto;

    private Empresa empresa;

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
        initControls();
    }

    private void initControls() {
        try {
            jpa = new JpaService();

            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));
            // addDireccion.setOnAction(e -> addDirecion());
            addDireccion.setDisable(true);
            addContacto.setDisable(true);
            boxDireccion.setDisable(true);
            boxContacto.setDisable(true);

            boxNombre.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    boxRSocial.setText(newValue);
                }
            });

            addRubro.setOnAction(e -> rubroSelect());

            if (empresa != null) {
                boxNombre.setText(empresa.getNombre());
                boxRSocial.setText(empresa.getRazonSocial());
                boxCUIT.setText(empresa.getCuit());
                boxRubro.setText(new EmpesaUtil().getListaRubrosString(empresa.getRubroList()));
            } else {
                empresa = new Empresa();
                boxNombre.setText("nueva");
            }

        } catch (Exception ex) {
            SaeDialog.showException(ex);
        }
    }

    private boolean isEmptytBox() {
        if (boxNombre.getText().trim().isEmpty()) {
            SaeDialog.errorDialog("Requiere valor", "Nombre es necesario");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxNombre.requestFocus();
                }
            });
            return true;
        } else if (boxRSocial.getText().trim().isEmpty()) {
            SaeDialog.errorDialog("Requiere valor", "Razon Social es necesario");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxRSocial.requestFocus();
                }
            });
            return true;
        } else if (boxCUIT.getText().trim().isEmpty()) {
            SaeDialog.errorDialog("Requiere valor", "CUIT es necesario");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxCUIT.requestFocus();
                }
            });
            return true;
        }

        return false;
    }

    private void guardar(ActionEvent e) {
        try {
            if (!isEmptytBox()) {

                empresa.setNombre(boxNombre.getText());
                empresa.setRazonSocial(boxRSocial.getText());
                empresa.setCuit(boxCUIT.getText());

                if (empresa.getIdEmpresa() != null) {
                    jpa.getEmpresa().edit(empresa);
                } else {
                    jpa.getEmpresa().create(empresa);
                }
                ((Node) (e.getSource())).getScene().getWindow().hide();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addDirecion() {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubicacion/DireccionEdit.fxml"));
            DireccionEditController controller = new DireccionEditController(new Direccion());
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();
            empresa.getDireccionList().add(controller.getDireccion());

        } catch (IOException e) {
            System.err.print(e);

        }
    }

    private void rubroSelect() {
        try {
            Stage dialog = new Stage();
            dialog.setTitle("Selecionar Rubros");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/RubroSelect.fxml"));
            RubroSelectController controller;
            if (empresa != null) {
                controller = new RubroSelectController(empresa.getRubroList());
            } else {
                controller = new RubroSelectController(null);
            }
            loader.setController(controller);
            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);
            dialog.showAndWait();

            if (controller.getRubroList() != null) {
                boxRubro.setText(new EmpesaUtil().getListaRubrosString(controller.getRubroList()));
                empresa.setRubroList(controller.getRubroList());
            }

        } catch (IOException e) {
            System.err.print(e);
        }
    }
}
