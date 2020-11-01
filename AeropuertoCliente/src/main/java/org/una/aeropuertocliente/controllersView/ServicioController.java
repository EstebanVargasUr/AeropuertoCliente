package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXComboBox;
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
public class ServicioController extends Controller implements  Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField txt_buscar;
    @FXML
    private JFXComboBox<?> cb_filtro;

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

    @FXML
    private void nuevo(MouseEvent event) {
        FlowController.getInstance().goView("CreacionServicio");
    }
    
}
