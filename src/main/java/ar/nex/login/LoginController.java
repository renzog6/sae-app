package ar.nex.login;

import ar.nex.entity.Usuario;
import ar.nex.jpa.service.JpaService;
import ar.nex.util.SaeDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class LoginController implements Initializable {

    public LoginController() {
        usuario = null;
    }

    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        LoginController.usuario = usuario;
    }

    public static boolean isLogin() {
        return usuario != null;
    }

    @FXML
    private AnchorPane apLogin;
    @FXML
    private Label Titulo;
    @FXML
    private JFXTextField boxUser;
    @FXML
    private JFXPasswordField boxPass;
    @FXML
    private JFXButton btnIniciar;
    @FXML
    private JFXButton btnCancelar;

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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxUser.requestFocus();
            }
        });

        btnCancelar.setOnAction(e -> this.close(e));
        btnIniciar.setOnAction(e -> login(e));
    }

    private void login(ActionEvent e) {
        try {
            EntityManager em = jpa.getFactory().createEntityManager();

            Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username");
            query.setParameter("username", boxUser.getText());

            Usuario usr = (Usuario) query.getSingleResult();
            if (usr == null) {
                SaeDialog.errorDialog("Login Error.", "El Usuario NO exite!!!");
            } else if (usr.getPassword().compareTo(boxPass.getText()) != 0) {
                SaeDialog.errorDialog("Login Error.", "Contraseña Incorrecta!!!");
            } else {
                setUsuario(usr);
                closeStage();
                showHome();
            }
        } catch (Exception ex) {
            SaeDialog.errorDialog("Login Error.", "El Usuario NO exite!!!");
        }

    }

    private void showHome() {
        try {
           // List<Window> open = Stage.getWindows().stream().filter(Window::isShowing);
            
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/home.css");
            stage.setScene(scene);
            stage.setTitle("SAE-App");
            stage.setMaximized(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.setOnCloseRequest(b -> {
                boolean response = SaeDialog.confirmDialog("Seguro que desea SALIR?");
                if (response) {
                    Platform.exit();
                }
                b.consume();
            });
            stage.show();
        } catch (Exception e) {
            SaeDialog.showException(e);
        }
    }

    private void closeStage() {
        ((Stage) boxUser.getScene().getWindow()).close();
    }

    public void close(ActionEvent ev) {
        setUsuario(null);
        Stage s = (Stage) ((Node) ev.getSource()).getScene().getWindow();
        s.close();
    }

}
