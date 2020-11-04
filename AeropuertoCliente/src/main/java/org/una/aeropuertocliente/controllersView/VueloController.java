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
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class VueloController extends Controller implements Initializable {

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
    private TableColumn<?, ?> tbc_matricula;
    @FXML
    private TableColumn<?, ?> tbc_destino;
    @FXML
    private TableColumn<?, ?> tbc_distancia;
    @FXML
    private TableColumn<?, ?> tbc_duracion;
    @FXML
    private TableColumn<?, ?> tbc_fechaSalida;
    @FXML
    private TableColumn<?, ?> tbc_fechaLlegada;
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
        FlowController.getInstance().goView("MenuGestion");
    }

    @FXML
    private void nuevo(MouseEvent event) {
        FlowController.getInstance().goView("RegistroVuelo");
    }
    
}
