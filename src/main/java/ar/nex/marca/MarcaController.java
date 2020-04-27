package ar.nex.marca;

import ar.nex.entity.Marca;
import ar.nex.equipo.gasto.GasoilController;
import ar.nex.jpa.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class MarcaController implements Initializable {

    public MarcaController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/marca/MarcaList.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(GasoilController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpMarca;
    @FXML
    private TextField searchBox;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblSelect;

    private final ObservableList<Marca> data = FXCollections.observableArrayList();
    private final FilteredList<Marca> filteredData = new FilteredList<>(data);
    private Marca select;

    @FXML
    private TableView<Marca> table;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colInfo;

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

        btnAdd.setOnAction(e -> add());
        btnEdit.setOnAction(e -> edit());

        initTable();
        loadData();
    }

    public void clearAll() {
        data.clear();
        searchBox.clear();
        select = null;
    }

    public void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMarca"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    public void loadData() {
        try {
            clearAll();
            List<Marca> lst = jpa.getMarca().findMarcaEntities();
            lst.forEach((item) -> {
                data.add(item);
            });
            table.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Marca>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Marca> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        select = table.getSelectionModel().getSelectedItem();
        lblSelect.setText(select.getNombre());
    }

    private void add() {
        select = null;
        edit();
    }

    private void edit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/marca/MarcaEdit.fxml"));
            MarcaDialogController controller = new MarcaDialogController(select);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());
            Stage dialog = new Stage();
            dialog.setTitle("Marca.");
            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);
            dialog.showAndWait();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
