package ar.nex.app;

import ar.nex.empresa.EmpresaController;
import ar.nex.equipo.EquipoController;
import ar.nex.pedido.PedidoController;
import ar.nex.repuesto.RepuestoController;
import ar.nex.usuario.UsuarioController;
import ar.nex.repuesto.RepuestoUsoController;
import ar.nex.util.DialogController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class HomeController implements Initializable {

    private final static Logger LOGGER = LogManager.getLogger(HomeController.class.getName());
    
    public HomeController() {
    }

    @FXML
    private BorderPane bpHome;
    @FXML
    private MenuButton mbEmpresa;
    @FXML
    private MenuButton mbEquipo;

    @FXML
    private MenuButton mbUsuario;
            
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ar.nex.app.HomeController.initialize()");
        try {
            DialogController.showSuccess("Bienvenido, Vengador Mas Fuerte!!!");
            initMenuEmpresa();
            initMenuEquipo();
            initMenuUsuario();
        } catch (Exception e) {
            DialogController.showException(e);
        }
    }

    private void initMenuEmpresa() {
        mbEmpresa.getItems().get(0).setOnAction(e -> show(new EmpresaController().getRoot()));
    }

    private void initMenuEquipo() {
        mbEquipo.getItems().get(0).setOnAction(e -> show(new EquipoController().getRoot()));
        mbEquipo.getItems().get(1).setOnAction(e -> show(new RepuestoController().getRoot()));
        mbEquipo.getItems().get(2).setOnAction(e -> show(new PedidoController().getRoot()));
        mbEquipo.getItems().get(3).setOnAction(e -> show(new RepuestoUsoController().getRoot()));
    }

        private void initMenuUsuario() {
        mbUsuario.getItems().get(0).setOnAction(e -> show(new UsuarioController().getRoot()));
//        mbEquipo.getItems().get(1).setOnAction(e -> show(new RepuestoController().getRoot()));
//        mbEquipo.getItems().get(2).setOnAction(e -> show(new PedidoController().getRoot()));
//        mbEquipo.getItems().get(3).setOnAction(e -> show(new RepuestoUsoController().getRoot()));
    }
        
    public void show(Parent root) {
        bpHome.getStylesheets().add(root.getStyle());
        bpHome.setCenter(root);
    }

}
