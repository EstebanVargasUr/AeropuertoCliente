package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.WebService.AerolineaWebService;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.UsuarioWebService;
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

    private AerolineaWebService autenticationWebService ;
    
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
    private void btnIniciaSesion(MouseEvent event) throws InterruptedException, ExecutionException, IOException {
        //FlowController.getInstance().goViewLeft("DashboardGerente");
       // FlowController.getInstance().goView("MenuGestion");
       String token = AutenticationWebService.login("admin", "Una2020");
       UsuarioWebService.getAllUsuarios(token);
       //UsuarioWebService.getUsuarioById(1, token);
    }

    @FXML
    private void btnRegistrarse(MouseEvent event) {
        FlowController.getInstance().goView("RegistroUsuario");
    }
    
}
