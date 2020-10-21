package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class CreacionServicioController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXCheckBox ck_estadoCobro;
    @FXML
    private JFXTextField txt_responsable;
    @FXML
    private JFXComboBox<?> cb_tipoServicio;
    @FXML
    private JFXTextField txt_avion;
    @FXML
    private JFXTextArea txt_observaciones;

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
        FlowController.getInstance().goView("Servicio");
    }

    @FXML
    private void guardar(MouseEvent event) {
    }
    
}
