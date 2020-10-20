package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXPasswordField;
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
public class LoginController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtCedula;
    @FXML
    private VBox root;
    @FXML
    private JFXPasswordField txtPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void initialize() {
       
    }

    @Override
    public Node getRoot() {
       return root;
    }

    @FXML
    private void btnIniciaSesion(MouseEvent event) {
    }

    @FXML
    private void btnRegistrarse(MouseEvent event) {
        FlowController.getInstance().goView("RegistroUsuario");
    }
    
}
