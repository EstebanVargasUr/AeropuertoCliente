package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
public class DashboardGerenteController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXButton btnInformacion;
    @FXML
    private JFXButton btnDepartamentos;
    @FXML
    private JFXButton btnTipoTramite;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    

    @Override
    public void initialize() {
 
    }

    @Override
    public Node getRoot() {
        return root;
    }      

    @FXML
    private void autorizarAcciones(MouseEvent event) {
        FlowController.getInstance().goView("MenuGestion");
    }

    @FXML
    private void reportes(MouseEvent event) {
    }

    @FXML
    private void btnDepartamentosAction(ActionEvent event) {
    }

    @FXML
    private void veerificarAlertas(MouseEvent event) {
    }
    
}
