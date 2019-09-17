package ar.nex.seguro;

import ar.nex.entity.Seguro;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.empresa.Rubro;
import ar.nex.entity.equipo.gasto.GasoilMovimiento;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.service.JpaService;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class SeguroEditController implements Initializable {

    public SeguroEditController(Seguro seguro) {
        this.seguro = seguro;
    }

    @FXML
    private TextField boxCompania;
    @FXML
    private TextField boxPoliza;
    @FXML
    private TextField boxRefencia;
    @FXML
    private TextField boxPrima;
    @FXML
    private TextField boxMonto;
    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private final ObservableList<Empresa> dataCompania = FXCollections.observableArrayList();
    private Empresa companiaSelect;

    private Seguro seguro;
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
        initcontrols();
    }

    private void initcontrols() {
        try {
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));
            
            loadCompania();
            AutoCompletionBinding<Empresa> autoProvedor = TextFields.bindAutoCompletion(boxCompania, dataCompania);
            autoProvedor.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<Empresa> event) -> {
                companiaSelect = event.getCompletion();
            }
            );
            

            if (seguro.getIdSeguro() != null) {
                boxCompania.setText(seguro.getCompania().getNombre());
                companiaSelect = seguro.getCompania();
                boxPoliza.setText(seguro.getPoliza());
                boxRefencia.setText(seguro.getReferencia());
                boxPrima.setText(seguro.getPrima().toString());
                boxMonto.setText(seguro.getMonto().toString());
                dpDesde.setValue(DateUtils.convertToLocalDateViaSqlDate(seguro.getDesde()));
                dpHasta.setValue(DateUtils.convertToLocalDateViaSqlDate(seguro.getHasta()));
                boxInfo.setText(seguro.getInfo());
            } else {
                dpDesde.setValue(LocalDate.now());
                dpHasta.setValue(LocalDate.now());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga las Empresas de con rubro de Seguros.
     */
    private void loadCompania() {
        try {
            Rubro seguros = jpa.getRubro().findRubro(136L);
            List<Empresa> results = seguros.getEmpresaList();
            if (!results.isEmpty()) {
                for (Empresa e : results) {
                    dataCompania.add(e);
                }
            }
        } catch (Exception e) {
            dataCompania.clear();
        }

    }

    private boolean isValido() {
        try {
            if (!DateUtils.validate(dpDesde.getValue())) {
                UtilDialog.errorDialog("Requiere valor", "Fecha");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        dpDesde.requestFocus();
                    }
                });
                return true;
            } else if (!DateUtils.validate(dpHasta.getValue())) {
                UtilDialog.errorDialog("Requiere valor", "Fecha");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        dpDesde.requestFocus();
                    }
                });
                return true;
            } else if (boxCompania.getText().trim().isEmpty()) {
                UtilDialog.errorDialog("Requiere valor", "Compañia");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boxCompania.requestFocus();
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
                seguro.setCompania(companiaSelect);
                seguro.setPoliza(boxPoliza.getText());
                seguro.setReferencia(boxRefencia.getText());
                seguro.setPrima(Double.valueOf(boxPrima.getText()));
                seguro.setMonto(Double.valueOf(boxMonto.getText()));
                seguro.setDesde(DateUtils.convertToDateViaSqlDate(dpDesde.getValue()));
                seguro.setHasta(DateUtils.convertToDateViaSqlDate(dpHasta.getValue()));
                seguro.setInfo(boxInfo.getText());

                if (seguro.getIdSeguro() != null) {
                    jpa.getSeguro().edit(seguro);
                } else {
                    jpa.getSeguro().create(seguro);
                }
            }
            ((Node) (e.getSource())).getScene().getWindow().hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
