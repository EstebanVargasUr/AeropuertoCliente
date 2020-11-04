package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class UsuarioController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField txt_buscar;
    @FXML
    private JFXComboBox<?> cb_filtro;
    @FXML
    private TableView<?> tablaServicios;
    @FXML
    private TableColumn<?, ?> tbc_id;
    @FXML
    private TableColumn<?, ?> tbc_cedula;
    @FXML
    private TableColumn<?, ?> tbc_nombre;
    @FXML
    private TableColumn<?, ?> tbc_areaTrabajo;
    @FXML
    private TableColumn<?, ?> tbc_jefe;
    @FXML
    private TableColumn<?, ?> tbc_rol;
    @FXML
    private TableColumn<?, ?> tbc_fechaRegistro;
    @FXML
    private TableColumn<?, ?> tbc_fechaModificacion;
    @FXML
    private TableColumn<?, ?> tbc_estado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    

    @Override
    public void initialize() {
        root.styleProperty().set("-fx-background-color: #4AB19D");
    }

    @Override
    public Node getRoot() {
        return root;
    }        

    @FXML
    private void buscar(KeyEvent event) {
    }

    @FXML
    private void buscar(MouseEvent event) {
    }

    @FXML
    private void volver(MouseEvent event) {
    }

    @FXML
    private void guardar(MouseEvent event) {
    }
    
}
