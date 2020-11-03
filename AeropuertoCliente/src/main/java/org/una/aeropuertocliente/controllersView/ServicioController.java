package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
public class ServicioController extends Controller implements  Initializable {

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
    private TableColumn<?, ?> tbc_tipoServicio;
    @FXML
    private TableColumn<?, ?> tbc_duracion;
    @FXML
    private TableColumn<?, ?> tbc_factura;
    @FXML
    private TableColumn<?, ?> tbc_encargado;
    @FXML
    private TableColumn<?, ?> tbc_fechaRegistro;
    @FXML
    private TableColumn<?, ?> tbc_fechaModificacion;
    @FXML
    private TableColumn<?, ?> tbc_estado;
    @FXML
    private TableColumn<?, ?> tbc_estadoCobro;
    @FXML
    private JFXTextArea txt_observaciones;
    @FXML
    private Label lbl_id;
    @FXML
    private Label lbl_matricula;
    @FXML
    private Label lbl_estado;
    @FXML
    private Label lbl_FechaRegistro;
    @FXML
    private Label lbl_aerolinea;
    @FXML
    private Label lbl_monto;
    @FXML
    private Label lbl_fecha;

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
    private void buscar(MouseEvent event) {
        FlowController.getInstance().goView("MenuGestion");
    }

    private void nuevo(MouseEvent event) {
        FlowController.getInstance().goView("CreacionServicio");
    }

    @FXML
    private void buscar(KeyEvent event) {
    }

    @FXML
    private void nuevoServicio(MouseEvent event) {
    }
    
}
