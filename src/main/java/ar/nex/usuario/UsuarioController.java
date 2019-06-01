package ar.nex.usuario;

import ar.nex.entity.Usuario;
import ar.nex.login.LoginController;
import ar.nex.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class UsuarioController implements Initializable {

    public UsuarioController() {
        this.filteredData = new FilteredList<>(data);
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/usuario/Usuario.fxml"));
            root.setStyle("/fxml/usuario/Usuario.css");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpUsuario;
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
    private Button btnPassword;
    @FXML
    private Button btnHistorial;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblPedido;
    @FXML
    private Label lblHistorial;

    private final ObservableList<Usuario> data = FXCollections.observableArrayList();
    private final FilteredList<Usuario> filteredData;
    private Usuario selectUsuario;
    @FXML
    private TableView<Usuario> table;
    @FXML
    private TableColumn<?, ?> colEmpleado;
    @FXML
    private TableColumn<?, ?> colUsuario;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colGrupo;
    @FXML
    private TableColumn<?, ?> colMenu;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TableColumn<?, ?> colAccion;

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
        initTable();
        loadData();
    }

    private void initTable() {
        //colEmpleado
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        // colGrupo.setCellValueFactory(new PropertyValueFactory<>("username"));
        //colMenu.setCellValueFactory(new PropertyValueFactory<>("username"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        //colAccion;
    }

    private void loadData() {
        //clearAll();
        selectUsuario = null;
        List<Usuario> lst = jpa.getUsuario().findUsuarioEntities();
        lst.forEach((item) -> {
            data.add(item);
        });
        table.setItems(data);
    }

    @FXML
    private void Search(InputMethodEvent event) {
    }

    @FXML
    private void Search(KeyEvent event) {
    }

    @FXML
    private void showOnClick(MouseEvent event) {
    }

}
