package ar.nex.app;

import ar.nex.empresa.EmpresaController;
import ar.nex.equipo.EquipoController;
import ar.nex.login.LoginController;
import ar.nex.pedido.PedidoController;
import ar.nex.repuesto.RepuestoController;
import ar.nex.repuesto.RepuestoUsoController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
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
    private BorderPane bpHome;
    @FXML
    private MenuButton mbEmpresa;
    @FXML
    private MenuButton mbEquipo;

    private Stage stage;

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
            initMenuEmpresa();
            initMenuEquipo();

            showLogin();
            if (!MainApp.getInstance().isLogin()) {
                stage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    public void show(Parent root) {
        bpHome.getStylesheets().add(root.getStyle());
        bpHome.setCenter(root);
    }

    public void showLogin() {
        try {
            Scene scene = new Scene(new LoginController().getRoot());
            stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
        }
    }

}
