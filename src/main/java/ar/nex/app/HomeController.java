package ar.nex.app;

import ar.nex.empleado.EmpleadoController;
import ar.nex.seguro.SeguroEmpleadoController;
import ar.nex.empresa.EmpresaController;
import ar.nex.empresa.RubroController;
import ar.nex.entity.Usuario;
import ar.nex.equipo.EquipoController;
import ar.nex.equipo.gasto.GasoilController;
import ar.nex.seguro.SeguroEquipoController;
import ar.nex.equipo.transporte.TransporteController;
import ar.nex.login.LoginController;
import ar.nex.marca.MarcaController;
import ar.nex.pedido.PedidoController;
import ar.nex.repuesto.RepuestoController;
import ar.nex.usuario.UsuarioController;
import ar.nex.repuesto.RepuestoUsoController;
import ar.nex.ubicacion.LocalidadController;
import ar.nex.util.UtilDialog;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
    private MenuBar menuBar;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("initialize(URL url, ResourceBundle rb)");
        try {
            // UtilDialog.showSuccess("Bienvenido, Vengador Mas Fuerte!!!");
            initMenu(LoginController.getUsuario());
        } catch (Exception e) {
            UtilDialog.showException(e);
        }
    }

    private void initMenu(Usuario usuario) {
        switch (usuario.getGrupo().getCode()) {
            case 101:
                initMenuEmpresa();
                initMenuEquipo();
                initMenuEmpleado();
                initMenuSeguro();
                initMenuPlanta();
                initMenuConfig();                
                break;
            case 251:
                initMenuEmpresa();
                initMenuEquipo();
                initMenuEmpleado();
                initMenuSeguro();
                break;
            case 351:
                initMenuPlanta();
                break;
        }

    }

    private void initMenuEmpresa() {
        Menu menu = new Menu("Empresa");

        MenuItem item = new MenuItem("Lista de Empresas");
        item.setOnAction(e -> show(new EmpresaController().getRoot()));
        menu.getItems().add(item);

        item = new MenuItem("Lista de Localidades");
        item.setOnAction(e -> show(new LocalidadController().getRoot()));
        menu.getItems().add(item);

        item = new MenuItem("Lista de Rubros");
        item.setOnAction(e -> show(new RubroController().getRoot()));
        menu.getItems().add(item);

        menuBar.getMenus().add(menu);
    }

    private void initMenuEquipo() {
        Menu menu = new Menu("Equipo");

        MenuItem item = new MenuItem("Lista de Equipos");
        item.setOnAction(e -> show(new EquipoController().getRoot()));
        menu.getItems().add(item);

        item = new MenuItem("Lista de Repuestos");
        item.setOnAction(e -> show(new RepuestoController().getRoot()));
        menu.getItems().add(item);

        item = new MenuItem("Lista de Pedidos");
        item.setOnAction(e -> show(new PedidoController().getRoot()));
        menu.getItems().add(item);

        item = new MenuItem("Uso Repuestos");
        item.setOnAction(e -> show(new RepuestoUsoController().getRoot()));
        menu.getItems().add(item);

        SeparatorMenuItem separator = new SeparatorMenuItem();
        menu.getItems().add(separator);

        item = new MenuItem("Marca");
        item.setOnAction(e -> show(new MarcaController().getRoot()));
        menu.getItems().add(item);

        menuBar.getMenus().add(menu);
    }

    private void initMenuEmpleado() {
        Menu menu = new Menu("Empledos");

        MenuItem item = new MenuItem("Lista de Empleados");
        item.setOnAction(e -> show(new EmpleadoController().getRoot()));
        menu.getItems().add(item);

        menuBar.getMenus().add(menu);
    }

    private void initMenuSeguro() {
        Menu menu = new Menu("Seguro");

        MenuItem item = new MenuItem("Seguro Empleados");
        item.setOnAction(e -> show(new SeguroEmpleadoController().getRoot()));
        menu.getItems().add(item);

        item = new MenuItem("Seguro Equipos");
        item.setOnAction(e -> show(new SeguroEquipoController().getRoot()));
        menu.getItems().add(item);
        
        menuBar.getMenus().add(menu);
    }

    private void initMenuPlanta() {
        Menu menu = new Menu("Planta");

        MenuItem item = new MenuItem("Gas-Oil");
        item.setOnAction(e -> show(new GasoilController().getRoot()));
        menu.getItems().add(item);

        item = new MenuItem("Transportes");
        item.setOnAction(e -> show(new TransporteController().getRoot()));
        menu.getItems().add(item);

        menuBar.getMenus().add(menu);
    }

    private void initMenuConfig() {
        Menu menu = new Menu("Config");

        MenuItem item = new MenuItem("Lista de Usuarios");
        item.setOnAction(e -> show(new UsuarioController().getRoot()));
        menu.getItems().add(item);

        menuBar.getMenus().add(menu);
    }

    public void show(Parent root) {
        bpHome.getStylesheets().add(root.getStyle());
        bpHome.setCenter(root);
    }

}
