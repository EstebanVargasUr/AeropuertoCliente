package org.una.aeropuertocliente.controllersView;

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
public class MenuGestionController extends Controller implements Initializable {

    @FXML
    private VBox root;

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
    private void servicios(MouseEvent event) {
        FlowController.getInstance().goView("Servicio");
    }

    @FXML
    private void aviones(MouseEvent event) {
        FlowController.getInstance().goView("RegistroAvion");
    }

    @FXML
    private void vuelos(MouseEvent event) {
    }
}
