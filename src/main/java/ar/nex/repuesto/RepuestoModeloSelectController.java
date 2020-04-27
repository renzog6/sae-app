package ar.nex.repuesto;

import ar.nex.entity.equipo.EquipoModelo;
import ar.nex.entity.equipo.gasto.Gasoil;
import ar.nex.equipo.util.UtilDialog;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RepuestoModeloSelectController implements Initializable {

    public RepuestoModeloSelectController(List<EquipoModelo> list) {
        this.dataTarget = FXCollections.observableArrayList();
        this.modeloList = list;
    }

    public List<EquipoModelo> getEquipoModeloList() {
        return modeloList;
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/repuesto/RepuestoModeloSelect.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(RepuestoModeloSelectController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDel;

    @FXML
    private ListView<EquipoModelo> lstSource;
    @FXML
    private ListView<EquipoModelo> lstTarget;

    private final ObservableList<EquipoModelo> dataSource = FXCollections.observableArrayList();
    private final ObservableList<EquipoModelo> dataTarget;

    private List<EquipoModelo> modeloList;
    private EquipoModelo modeloSelect;
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
            lstSource.setCellFactory(new Callback<ListView<EquipoModelo>, ListCell<EquipoModelo>>() {
                @Override
                public ListCell<EquipoModelo> call(ListView<EquipoModelo> param) {
                    ListCell<EquipoModelo> cell = new ListCell<EquipoModelo>() {
                        @Override
                        protected void updateItem(EquipoModelo item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item != null) {
                                setText(item.getStringTipoModelo());
                            } else {
                                setText("");
                            }
                        }
                    };
                    return cell;
                }
            });

            lstTarget.setCellFactory(new Callback<ListView<EquipoModelo>, ListCell<EquipoModelo>>() {
                @Override
                public ListCell<EquipoModelo> call(ListView<EquipoModelo> param) {
                    ListCell<EquipoModelo> cell = new ListCell<EquipoModelo>() {
                        @Override
                        protected void updateItem(EquipoModelo item, boolean empty) {
                            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item != null) {
                                setText(item.getStringTipoModelo());
                            } else {
                                setText("");
                            }
                        }
                    };
                    return cell;
                }
            });

            btnCancelar.setOnAction(e -> cancelar(e));
            btnGuardar.setOnAction(e -> guardar(e));

            btnAdd.setOnAction(e -> {
                lstTarget.getItems().add(modeloSelect);
                lstSource.getItems().remove(modeloSelect);
            });

            btnDel.setOnAction(e -> {
                lstSource.getItems().add(modeloSelect);
                lstTarget.getItems().remove(modeloSelect);
            });

            lstSource.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EquipoModelo>() {
                @Override
                public void changed(ObservableValue<? extends EquipoModelo> observable, EquipoModelo oldValue, EquipoModelo newValue) {
                    modeloSelect = newValue;//lstSource.getSelectionModel().getSelectedItem();                    
                }
            });
            lstTarget.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EquipoModelo>() {
                @Override
                public void changed(ObservableValue<? extends EquipoModelo> observable, EquipoModelo oldValue, EquipoModelo newValue) {
                    modeloSelect = newValue;//lstTarget.getSelectionModel().getSelectedItem();                    
                }
            });

            loadData();
        } catch (Exception ex) {
            UtilDialog.showException(ex);
        }
    }

    private void loadData() {
        try {
            EntityManager em = jpa.getFactory().createEntityManager();
            TypedQuery<EquipoModelo> query
                    = em.createQuery("SELECT c FROM EquipoModelo c"
                            + " ORDER BY c.tipo, c.nombre ASC", EquipoModelo.class);
            List<EquipoModelo> lst = query.getResultList();

            if (modeloList != null) {
                if (!modeloList.isEmpty()) {
                    for (EquipoModelo r : modeloList) {
                        lst.remove(r);
                        dataTarget.add(r);
                    }
                    lstTarget.getItems().addAll(dataTarget);
                }
            }

            lst.forEach((item) -> {
                dataSource.add(item);
            });

            lstSource.getItems().addAll(dataSource);
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    private void guardar(ActionEvent e) {
        try {
            modeloList = new ArrayList<>();
            lstTarget.getItems().forEach((item) -> {
                modeloList.add(item);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
            e.consume();
        }
    }

    private void cancelar(ActionEvent e) {
        modeloList = null;
        ((Node) (e.getSource())).getScene().getWindow().hide();
        e.consume();
    }

}
