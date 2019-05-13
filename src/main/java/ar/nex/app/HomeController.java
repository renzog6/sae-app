package ar.nex.app;

import ar.nex.empresa.EmpresaController;
import ar.nex.equipo.EquipoController;
import ar.nex.login.LoginController;
import ar.nex.pedido.PedidoController;
import ar.nex.repuesto.RepuestoController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class HomeController implements Initializable {

    public HomeController() {
    }

    @FXML
    private Button btnEmpresa;
    @FXML
    private Button btnEquipo;
    @FXML
    private Button btnPedido;
    @FXML
    private Button btnRepuesto;

    @FXML
    private BorderPane bpHome;

    private Stage stage;

    private LoginController loginController;

    private EmpresaController empresaController;

    private EquipoController equipoController;

    private PedidoController pedidoController;

    private RepuestoController repuestoController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initControllers();

            showLogin();
            if (!MainApp.getInstance().isLogin()) {
                stage.close();
            }

            btnEmpresa.setOnAction(e -> showEmpresa());
            btnEquipo.setOnAction(e -> showEquipo());
            btnPedido.setOnAction(e -> showPedido());
            btnRepuesto.setOnAction(e -> showRepuesto());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initControllers() {
        loginController = null;
        empresaController = null;
        equipoController = null;
        pedidoController = null;
        repuestoController = null;
    }

    public void show(Parent root) {
        bpHome.getStylesheets().add(root.getStyle());
        bpHome.setCenter(root);
    }

    public void showLogin() {
        try {
            if (loginController == null) {
                loginController = new LoginController();
            }
            Scene scene = new Scene(loginController.getRoot());
            stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
        }
    }

    public void showEmpresa() {
        if (empresaController == null) {
            empresaController = new EmpresaController();
        }
        show(empresaController.getRoot());
    }

    public void showEquipo() {
        if (equipoController == null) {
            equipoController = new EquipoController();
        }
        show(equipoController.getRoot());
    }

    public void showPedido() {
        if (pedidoController == null) {
            pedidoController = new PedidoController();
        }
        show(pedidoController.getRoot());
    }

    public void showRepuesto() {
        if (repuestoController == null) {
            repuestoController = new RepuestoController();
        }
        show(repuestoController.getRoot());
    }
}
