package ar.nex.app;

import ar.nex.exceptions.ExceptionUtil;
import ar.nex.login.LoginController;
import ar.nex.util.SaeDate;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp extends Application {

    private final static Logger LOGGER = LogManager.getLogger(MainApp.class.getName());

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("SAE-App");
        Parent root = new LoginController().getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

//        Sync s = new Sync();
//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        s.check();
//                        Thread.sleep(5000);
//                    }
//                } catch (InterruptedException ex) {
//                    java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//                }
//            }
//        };        
//        Thread backgroundThread = new Thread(task);
//        backgroundThread.setDaemon(true);
//        backgroundThread.start();

        new Thread(() -> {
          ExceptionUtil.init();          
        }).start();
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
