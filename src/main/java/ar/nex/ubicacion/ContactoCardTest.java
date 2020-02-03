package ar.nex.ubicacion;

import ar.nex.entity.ubicacion.Contacto;
import ar.nex.util.SaeDate;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactoCardTest extends Application {

    private final static Logger LOGGER = LogManager.getLogger(ContactoCardTest.class.getName());

    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.setTitle("SAE-App");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubicacion/ContactoCard.fxml"));
            ContactoCardController controller = new ContactoCardController();
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "SAE-App launched on {}", SaeDate.formatDateTimeString(startTime));
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Long exitTime = System.currentTimeMillis();
                LOGGER.log(Level.INFO, "SAE-App is closing on {}. Used for {} ms", SaeDate.formatDateTimeString(startTime), exitTime);
            }
        });
    }
}
