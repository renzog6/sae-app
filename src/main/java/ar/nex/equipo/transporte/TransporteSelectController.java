package ar.nex.equipo.transporte;

import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.Transporte;
import ar.nex.jpa.service.JpaService;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class TransporteSelectController implements Initializable {

    public TransporteSelectController(int accion) {
        this.accion = accion;
    }

    @FXML
    private Button btnAceptar;
    @FXML
    private TableView table;

    private Equipo equipo;

    public Equipo getEquipo() {
        return equipo;
    }

    private Empleado chofer;

    public Empleado getChofer() {
        return chofer;
    }

    private final int accion;

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
        btnAceptar.setOnAction(e -> aceptar(e));
        //colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        jpa = new JpaService();
        loadData(accion);
    }

    private void loadData(int i) {
        switch (i) {
            case 0:
                loadCamion();
                break;
            case 1:
                loadAcoplado();
                break;
            case 2:
                loadChofer();
                break;
        }
    }

    private void loadEquipo(List<Equipo> lst) {
        try {
            ObservableList<Equipo> data = FXCollections.observableArrayList();
            for (Equipo e : lst) {
                data.add(e);
            }

            TableColumn<Equipo, String> colNombre = new TableColumn<>("#");
            colNombre.setCellValueFactory(e -> {
                return new SimpleStringProperty(e.getValue().getMarca() + " -" + e.getValue().getAnio() + "- " + e.getValue().getPatente());
            });
            table.getColumns().addAll(colNombre);
            table.setItems(data);
            table.setOnMouseClicked(event -> clickOnTable(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCamion() {
        TypedQuery<Equipo> query
                = jpa.getFactory().createEntityManager().createQuery("SELECT e FROM Equipo e WHERE e.tipo.nombre=:tipo", Equipo.class)
                        .setParameter("tipo", "Camion");
        List<Equipo> equipos = query.getResultList();

        List<Transporte> lista = jpa.getTransporte().findTransporteEntities();
        for (Transporte t : lista) {
            equipos.remove(t.getCamion());
        }

        loadEquipo(equipos);
    }

    private void loadAcoplado() {
        TypedQuery<Equipo> query
                = jpa.getFactory().createEntityManager().createQuery("SELECT e FROM Equipo e "
                        + "WHERE e.tipo.nombre=:t1 OR e.tipo.nombre=:t2", Equipo.class)
                        .setParameter("t1", "Acoplado Cerealero")
                        .setParameter("t2", "Semirremolque");
        List<Equipo> equipos = query.getResultList();

        List<Transporte> lista = jpa.getTransporte().findTransporteEntities();
        for (Transporte t : lista) {
            equipos.remove(t.getAcoplado());
        }

        loadEquipo(equipos);
    }

    private void loadChofer() {
        try {
            TypedQuery<Empleado> query
                    = jpa.getFactory().createEntityManager().createQuery("SELECT e FROM Empleado e WHERE e.puesto.nombre =:puesto", Empleado.class)
                            .setParameter("puesto", "Chofer");
            List<Empleado> empleados = query.getResultList();

            List<Transporte> lista = jpa.getTransporte().findTransporteEntities();
            for (Transporte t : lista) {
                empleados.remove(t.getChofer());
            }

            ObservableList<Empleado> data = FXCollections.observableArrayList();
            for (Empleado e : empleados) {
                data.add(e);
            }

            TableColumn<Empleado, String> colNombre = new TableColumn<>("#");
            colNombre.setCellValueFactory(e -> {
                return new SimpleStringProperty(e.getValue().getNombreCompleto());
            });
            table.getColumns().addAll(colNombre);
            table.setItems(data);
            table.setOnMouseClicked(event -> clickOnTable(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickOnTable(MouseEvent event) {
        if (accion == 0 || accion == 1) {
            equipo = (Equipo) table.getSelectionModel().getSelectedItem();
        } else {
            chofer = (Empleado) table.getSelectionModel().getSelectedItem();
        }
    }

    private void aceptar(ActionEvent e) {
        if (equipo != null || chofer != null) {
            ((Node) (e.getSource())).getScene().getWindow().hide();
        }
    }

}
