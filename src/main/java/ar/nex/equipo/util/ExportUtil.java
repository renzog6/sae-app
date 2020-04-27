package ar.nex.equipo.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Renzo
 */
public class ExportUtil {

    public void save(Workbook workbook, String excelNombre) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"),
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv")
            );
            //fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
            fileChooser.setTitle("Guardar Excel en ...");
            fileChooser.setInitialFileName(excelNombre);

            File saveFile = fileChooser.showSaveDialog(new Stage());
            if (saveFile != null) {
                if (!saveFile.getPath().endsWith(".xlsx")) {
                    saveFile = new File(saveFile.getPath() + ".xlsx");
                }
                FileOutputStream outputStream = new FileOutputStream(saveFile);
                workbook.write(outputStream);
                workbook.close();

                if (UtilDialog.confirmDialog("Desea ABRIR el archivo generado?")) {                    
                    File path = new File(saveFile.toString());
                    Desktop.getDesktop().open(path);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
