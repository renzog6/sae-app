package ar.nex.login;

import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class LoginConfigController implements Initializable {

    @FXML
    private BorderPane bpLogin;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnCheck;

    @FXML
    private TextField boxUser;
    @FXML
    private TextField boxPass;
    @FXML
    private TextField boxUrl;
    @FXML
    private TextField boxDriver;

    @FXML
    private Label lblEstado;

    private LoginConexion conexion;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCancelar.setOnAction(e -> close(e));
        btnCheck.setOnAction(e -> check());
        btnGuardar.setOnAction(e -> guardar(e));

        conexion = new LoginConexion();
        initControls();
    }

    private void initControls() {
        try {
            conexion.readConfig();
            boxUrl.setText(conexion.getUrl());
            boxUser.setText(conexion.getUser());
            boxPass.setText(conexion.getPass());
            boxDriver.setText(conexion.getDriver());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBoxText() {
        conexion.setUrl(boxUrl.getText());
        conexion.setUser(boxUser.getText());
        conexion.setPass(boxPass.getText());
        conexion.setDriver(boxDriver.getText());
    }

    private void check() {
        Connection conn = null;
        try {
            getBoxText();
            Class.forName(conexion.getDriver());
            conn = DriverManager.getConnection(conexion.getUrl(), conexion.getUser(), conexion.getPass());
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT nombre FROM empresa");
            if (rs != null) {
                lblEstado.setText("Conexion Exitosa!!!");
                conn.close();
            }
        } catch (Exception ex) {
            lblEstado.setText("- Error Conexion -");
            Logger.getLogger(LoginConfigController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardar(ActionEvent e) {
        getBoxText();
        conexion.writeConfig(conexion);
        close(e);
    }

    private void close(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }
}
