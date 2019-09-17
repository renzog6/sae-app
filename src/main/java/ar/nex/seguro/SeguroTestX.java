package ar.nex.seguro;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Renzo
 */
public class SeguroTestX extends Application {

    //   private final static Logger LOGGER = LogManager.getLogger(EquipoSeguroTestX.class.getName());
    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/seguro/SeguroEquipo.fxml"));
            
            Scene scene = new Scene(loader.load());
            Stage dialog = new Stage();
            dialog.setTitle("Seguro");
            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

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
        //LOGGER.log(Level.INFO, "SAE-App launched on {}", DateUtils.formatDateTimeString(startTime));
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Long exitTime = System.currentTimeMillis();
                //LOGGER.log(Level.INFO, "SAE-App is closing on {}. Used for {} ms", DateUtils.formatDateTimeString(startTime), exitTime);
            }
        });
    }
}
