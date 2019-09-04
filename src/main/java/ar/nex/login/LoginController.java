package ar.nex.login;

import ar.nex.entity.Usuario;
import ar.nex.service.JpaService;
import ar.nex.util.DialogController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/login/Login.fxml"));
            root.setStyle("/css/login.css");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpLogin;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnConfig;
    @FXML
    private TextField boxUser;
    @FXML
    private PasswordField boxPass;

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
        btnConfig.setOnAction(e -> config(e));
        btnIniciar.setOnAction(e -> login(e));
    }

    private void config(ActionEvent e) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/LoginConfig.fxml"));
            LoginConfigController controller = new LoginConfigController();
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);

            stage.showAndWait();
        } catch (IOException ex) {
            System.err.print(ex);
        }
    }

        private void login(ActionEvent e) {
        try {
            EntityManager em = jpa.getFactory().createEntityManager();
            Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username");
            query.setParameter("username", boxUser.getText());

            Usuario usr = (Usuario) query.getSingleResult();
            if (usr == null) {
                DialogController.errorDialog("Login Error.", "El Usuario NO exite!!!");
            } else if (usr.getPassword().compareTo(boxPass.getText()) != 0) {
                DialogController.errorDialog("Login Error.", "Contraseña Incorrecta!!!");
            } else {
                setUsuario(usr);
                closeStage();
                showHome();
            }
        } catch (Exception ex) {
            DialogController.errorDialog("Login Error.", "El Usuario NO exite!!!");
        }

    }
    
    private void showHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/fxml/Home.css");
            stage.setScene(scene);
            stage.setTitle("SAE-App");
            stage.setMaximized(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.setOnCloseRequest(b -> {
                boolean response = DialogController.confirmDialog("Seguro que desea SALIR?");
                if (response) {
                    Platform.exit();
                }
                b.consume();
            });
            stage.show();
        } catch (Exception e) {
            DialogController.showException(e);
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
