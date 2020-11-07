package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
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
public class ReporteSoporteTecnicoController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField txt_buscar;
    @FXML
    private JFXTextField txt_buscarIdAvion;
    @FXML
    private DatePicker datePFechaInicial;
    @FXML
    private DatePicker datePFechaFinal;
    @FXML
    private JFXComboBox<?> cb_filtro;
    @FXML
    private JFXComboBox<?> cb_filtroEstado;
    @FXML
    private TableView<?> tablaServicios;
    @FXML
    private TableColumn<?, ?> tbc_id;
    @FXML
    private TableColumn<?, ?> tbc_descripcion;
    @FXML
    private TableColumn<?, ?> tbc_fechaRegistro;
    @FXML
    private TableColumn<?, ?> tbc_duracion;
    @FXML
    private TableColumn<?, ?> tbc_nombre;
    @FXML
    private TableColumn<?, ?> tbc_cedula;
    @FXML
    private TableColumn<?, ?> tbc_rol;

     @Override
    public void initialize() {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @Override
    public Node getRoot() {
        return root;
    }   

    @FXML
    private void cb_filtroAction(ActionEvent event) {
    }

    @FXML
    private void btnBuscar(KeyEvent event) {
    }

    @FXML
    private void buscar(MouseEvent event) {
    }

    @FXML
    private void tablaServiciosClic(MouseEvent event) {
    }
    
}
