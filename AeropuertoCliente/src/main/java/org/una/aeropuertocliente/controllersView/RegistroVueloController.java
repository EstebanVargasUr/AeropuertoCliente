package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class RegistroVueloController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField txt_destino;
    @FXML
    private JFXTextField txt_distancia;
    @FXML
    private JFXTextField txt_duracion;
    @FXML
    private DatePicker dpk_fechaSalida;
    @FXML
    private DatePicker dpk_fechaLlegada;
    @FXML
    private JFXTextField txt_avionMatricula;

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
    private void cancelar(MouseEvent event) {
        FlowController.getInstance().goView("Vuelo");
    }

    @FXML
    private void guardar(MouseEvent event) {
    }
    
}
