package ar.nex.empresa;

import ar.nex.entity.empresa.Rubro;
import ar.nex.service.JpaService;
import ar.nex.util.SaeDialog;
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
import javafx.scene.control.ListView;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RubroSelectController implements Initializable {

    public RubroSelectController(List<Rubro> rubroList) {
        this.dataTarget = FXCollections.observableArrayList();
        this.rubroList = rubroList;
    }

    public List<Rubro> getRubroList() {
        return rubroList;
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/empresa/RubroSelect.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(RubroSelectController.class.getName()).log(Level.SEVERE, null, ex);
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
    private ListView<Rubro> lstSource;
    @FXML
    private ListView<Rubro> lstTarget;

    private final ObservableList<Rubro> dataSource = FXCollections.observableArrayList();
    private ObservableList<Rubro> dataTarget;

    private List<Rubro> rubroList;
    private Rubro rubroSelect;
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
        //listSelect.c
    }

    private void initControls() {
        try {
            btnCancelar.setOnAction(e -> cancelar(e));
            btnGuardar.setOnAction(e -> guardar(e));

            btnAdd.setOnAction(e -> {
                lstTarget.getItems().add(rubroSelect);
                lstSource.getItems().remove(rubroSelect);
            });

            btnDel.setOnAction(e -> {
                lstSource.getItems().add(rubroSelect);
                lstTarget.getItems().remove(rubroSelect);
            });

            lstSource.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rubro>() {
                @Override
                public void changed(ObservableValue<? extends Rubro> observable, Rubro oldValue, Rubro newValue) {
                    rubroSelect = newValue;//lstSource.getSelectionModel().getSelectedItem();                    
                }
            });
            lstTarget.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rubro>() {
                @Override
                public void changed(ObservableValue<? extends Rubro> observable, Rubro oldValue, Rubro newValue) {
                    rubroSelect = newValue;//lstTarget.getSelectionModel().getSelectedItem();                    
                }
            });

            loadData();
        } catch (Exception ex) {
            SaeDialog.showException(ex);
        }
    }

    private void loadData() {
        try {
            TypedQuery<Rubro> query
                    = jpa.getFactory().createEntityManager().createQuery("SELECT s FROM Rubro s ORDER BY s.nombre ASC", Rubro.class);
            List<Rubro> lst = query.getResultList();

            if (rubroList != null) {
                if (!rubroList.isEmpty()) {
                    for (Rubro r : rubroList) {
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
            SaeDialog.showException(e);
        }
    }

    private void guardar(ActionEvent e) {
        try {
            rubroList = new ArrayList<>();
            lstTarget.getItems().forEach((item) -> {
                rubroList.add(item);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
            e.consume();
        }
    }

    private void cancelar(ActionEvent e) {
        rubroList = null;
        ((Node) (e.getSource())).getScene().getWindow().hide();
        e.consume();
    }

}
