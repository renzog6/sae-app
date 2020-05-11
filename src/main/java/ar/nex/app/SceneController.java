package ar.nex.app;

import ar.nex.login.LoginController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author Renzo
 */
public class SceneController {

    private Scene scene;

    private Parent parent;

    public SceneController() {
        this.parent = null;
        this.scene = null;
    }

    public SceneController(Parent parent) {
        this.scene = new Scene(parent);
    }

    public SceneController(Scene scene) {
        this.scene = scene;
    }

    public Scene getLogin() {
        return makeScene(0);
    }

    private Scene makeScene(int fxml) {        
        try {
            FXMLLoader loader = null;
            switch (fxml) {
                case 0:
                    loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
                    loader.setController(new LoginController());
                default:
            this.parent = loader.load();
            }
            
            this.scene = new Scene(parent);
            return this.scene;

        } catch (IOException ex) {            
            Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
