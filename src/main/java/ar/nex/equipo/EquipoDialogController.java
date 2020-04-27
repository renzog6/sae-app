package ar.nex.equipo;

import ar.nex.marca.MarcaDialogController;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.EquipoCategoria;
import ar.nex.entity.equipo.EquipoCompraVenta;
import ar.nex.entity.equipo.EquipoModelo;
import ar.nex.entity.equipo.EquipoTipo;
import ar.nex.entity.Marca;
import ar.nex.equipo.util.DateUtils;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EquipoDialogController implements Initializable {

    public EquipoDialogController(Equipo e) {
        this.equipo = e;
    }

    @FXML
    private ComboBox filtroEmpresa;
    @FXML
    private TextField boxCategoria;
    @FXML
    private TextField boxModelo;
    @FXML
    private TextField boxColor;
    @FXML
    private TextField boxOtro;
    @FXML
    private TextField boxAnio;
    @FXML
    private TextField boxChasis;
    @FXML
    private TextField boxMotor;
    @FXML
    private TextField boxPatente;
    @FXML
    private TextField boxTipo;
    @FXML
    private TextField boxFechaCompra;
    @FXML
    private TextField boxVendedor;
    @FXML
    private TextField boxValorCompra;
    @FXML
    private TextField boxMarca;
    @FXML
    private TextField boxNombre;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAddMarca;
    @FXML
    private Button btnAddCategoria;
    @FXML
    private Button btnAddTipo;
    @FXML
    private Button btnAddModelo;
    @FXML
    private CheckBox cbUsaGasoil;

    private JpaService jpa;

    private final Equipo equipo;

    /**
     * Initializes the controll
     *
     * @param url class.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        jpa = new JpaService();

        loadDataCategoria();
        loadDataTipo();
        loadDataModelo();
        loadDataMarca();
        initFiltro();
        initControls();
    }

    public void initFiltro() {
        ObservableList list = FXCollections.observableArrayList(EmpresaSelect.values());
        list.add(0, "Elegir Empresa");
        filtroEmpresa.getItems().addAll(list);
        if (equipo.getEmpresa() != null) {
            idEmp = equipo.getEmpresa().getIdEmpresa();
            filtroEmpresa.getSelectionModel().select(equipo.getEmpresa().getIdEmpresa().intValue() + 1);
        } else {
            filtroEmpresa.getSelectionModel().select(1);
        }
    }

    private void initControls() {
        try {
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            btnAddCategoria.setOnAction(e -> addCategoria(e));
            btnAddTipo.setOnAction(e -> addTipo(e));
            btnAddModelo.setOnAction(e -> addModelo(e));
            btnAddMarca.setOnAction(e -> addMarca(e));

            categoriaSelect = equipo.getCategoria() != null ? equipo.getCategoria() : new EquipoCategoria("NN");
            boxCategoria.setText(categoriaSelect.getNombre());

            tipoSelect = equipo.getTipo() != null ? equipo.getTipo() : new EquipoTipo("NN");
            boxTipo.setText(tipoSelect.getNombre());

            modeloSelect = equipo.getModelo() != null ? equipo.getModelo() : new EquipoModelo("NN");
            boxModelo.setText(modeloSelect.getNombre());

            marcaSelect = equipo.getMarca() != null ? equipo.getMarca() : new Marca("NN");
            boxMarca.setText(marcaSelect.getNombre());

            boxColor.setText(equipo.getColor());
            boxOtro.setText(equipo.getOtro());
            boxAnio.setText(equipo.getAnio());
            boxChasis.setText(equipo.getChasis());
            boxMotor.setText(equipo.getMotor());
            boxPatente.setText(equipo.getPatente());
            boxNombre.setText(equipo.getNombre());

            if (equipo.getUsaGasoil() != null) {
                cbUsaGasoil.setSelected(equipo.getUsaGasoil());
            } else {
                cbUsaGasoil.setSelected(false);
            }

            boxFechaCompra.setText(new SimpleDateFormat("dd/MM/yyyy").format(equipo.getCompraVenta().getFechaCompra()));
            boxVendedor.setText(equipo.getCompraVenta().getVendedor());
            boxValorCompra.setText(equipo.getCompraVenta().getValorCompra().toString());

        } catch (Exception e) {
            boxFechaCompra.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            boxVendedor.setText("");
            boxValorCompra.setText("");
        }
    }

    @FXML
    private void guardar(ActionEvent e) {
        try {
            Empresa emp = jpa.getEmpresa().findEmpresa(idEmp);

            equipo.setEmpresa(emp);
            equipo.setAnio(boxAnio.getText());
            equipo.setChasis(boxChasis.getText());
            equipo.setMotor(boxMotor.getText());
            equipo.setPatente(boxPatente.getText());
            equipo.setColor(boxColor.getText());
            equipo.setOtro(boxOtro.getText());
            equipo.setNombre(boxNombre.getText());

            equipo.setCategoria(categoriaSelect);
            equipo.setTipo(tipoSelect);
            equipo.setModelo(modeloSelect);
            equipo.setMarca(marcaSelect);

            equipo.setUsaGasoil(cbUsaGasoil.isSelected());

            equipo.setCompraVenta(crearCompraVenta());

            if (equipo.getIdEquipo() != null) {
                jpa.getEquipo().edit(equipo);
            } else {
                jpa.getEquipo().create(equipo);
            }

        } catch (Exception ex) {
            UtilDialog.showException(ex);
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
        }
    }

    private EquipoCompraVenta crearCompraVenta() {
        EquipoCompraVenta cv = new EquipoCompraVenta();
        try {
            DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
            cv.setFechaCompra(fd.parse(boxFechaCompra.getText()));
            cv.setVendedor(boxVendedor.getText());
            cv.setValorCompra(Double.valueOf(boxValorCompra.getText()));
        } catch (Exception e) {
            cv.setFechaCompra(new Date());
            cv.setVendedor("-");
            cv.setValorCompra(0.0);
        }
        jpa.getEquipoCompraVenta().create(cv);
        return cv;
    }

    private EquipoCategoria categoriaSelect;

    private final ObservableList<EquipoCategoria> dataCategoria = FXCollections.observableArrayList();

    private void loadDataCategoria() {
        try {
            this.dataCategoria.clear();
            List<EquipoCategoria> lst = jpa.getEquipoCategoria().findEquipoCategoriaEntities();
            lst.forEach((item) -> {
                this.dataCategoria.add(item);
            });

            AutoCompletionBinding<EquipoCategoria> autoCategoria = TextFields.bindAutoCompletion(boxCategoria, dataCategoria);

            autoCategoria.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<EquipoCategoria> event) -> {
                        categoriaSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void addCategoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/equipo/EquipoCategoriaDialog.fxml"));
            EquipoCategoriaDialogController controller = new EquipoCategoriaDialogController(new EquipoCategoria());
            loader.setController(controller);

            showDialog(new Scene(loader.load()), 3);
        } catch (IOException e) {
            UtilDialog.showException(e);
        }
    }

    private EquipoTipo tipoSelect;

    private final ObservableList<EquipoTipo> dataTipo = FXCollections.observableArrayList();

    private void loadDataTipo() {
        try {
            this.dataTipo.clear();
            List<EquipoTipo> lst = jpa.getEquipoTipo().findEquipoTipoEntities();
            lst.forEach((item) -> {
                this.dataTipo.add(item);
            });

            AutoCompletionBinding<EquipoTipo> autoTipo = TextFields.bindAutoCompletion(boxTipo, dataTipo);

            autoTipo.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<EquipoTipo> event) -> {
                        tipoSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void addTipo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/equipo/EquipoTipoDialog.fxml"));
            EquipoTipoDialogController controller = new EquipoTipoDialogController(new EquipoTipo());
            loader.setController(controller);

            showDialog(new Scene(loader.load()), 2);
        } catch (IOException e) {
            UtilDialog.showException(e);
        }
    }

    private EquipoModelo modeloSelect;

    private final ObservableList<EquipoModelo> dataModelo = FXCollections.observableArrayList();

    private void loadDataModelo() {
        try {
            this.dataModelo.clear();
            List<EquipoModelo> lst = jpa.getEquipoModelo().findEquipoModeloEntities();
            lst.forEach((item) -> {
                this.dataModelo.add(item);
            });

            AutoCompletionBinding<EquipoModelo> autoModelo = TextFields.bindAutoCompletion(boxModelo, dataModelo);

            autoModelo.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<EquipoModelo> event) -> {
                        modeloSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    private void addModelo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/equipo/ModeloDialog.fxml"));
            ModeloDialogController controller = new ModeloDialogController(new EquipoModelo());
            loader.setController(controller);

            showDialog(new Scene(loader.load()), 1);
        } catch (IOException e) {
            UtilDialog.showException(e);
        }
    }

    private Marca marcaSelect;

    private final ObservableList<Marca> dataMarca = FXCollections.observableArrayList();

    private void loadDataMarca() {
        try {
            this.dataMarca.clear();
            List<Marca> lst = jpa.getMarca().findMarcaEntities();
            lst.forEach((item) -> {
                this.dataMarca.add(item);
            });

            AutoCompletionBinding<Marca> autoMarca = TextFields.bindAutoCompletion(boxMarca, dataMarca);

            autoMarca.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<Marca> event) -> {
                        marcaSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void addMarca(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/util/MarcaDialog.fxml"));
            MarcaDialogController controller = new MarcaDialogController(new Marca());
            loader.setController(controller);

            showDialog(new Scene(loader.load()), 4);
        } catch (IOException e) {
            UtilDialog.showException(e);
        }
    }

    private long idEmp;

    @FXML
    private void selectEmpresa() {
        EmpresaSelect empresa = (EmpresaSelect) filtroEmpresa.getSelectionModel().getSelectedItem();
        idEmp = empresa.getId();
    }

    private void showDialog(Scene scene, int i) {
        System.out.println("ar.nex.equipo.EquipoAddController.showDialog()");
        try {
            Stage dialog = new Stage();

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

            switch (i) {
                case 1:
                    loadDataModelo();
                    break;
                case 2:
                    loadDataTipo();
                    break;
                case 3:
                    loadDataCategoria();
                    break;
                case 4:
                    loadDataMarca();
                    break;
            }
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

}
