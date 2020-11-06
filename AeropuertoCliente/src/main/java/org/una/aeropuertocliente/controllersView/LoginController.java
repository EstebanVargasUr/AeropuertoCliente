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
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
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
    private void btnIniciaSesion(MouseEvent event) throws InterruptedException, ExecutionException, IOException {
       
       AuthenticationResponse authenticationResponse = AutenticationWebService.login(txtCedula.getText(), txtPassword.getText());
       
       if(authenticationResponse != null)
       {
            if(authenticationResponse.getRoles().getNombre().equals("Gestor")){
                FlowController.getInstance().authenticationResponse = authenticationResponse;
                FlowController.getInstance().goView("MenuGestor");
                FlowController.getInstance().goViewTop("BarraNavegacion");
                
            }

            else if(authenticationResponse.getRoles().getNombre().equals("Gerente")){
                FlowController.getInstance().authenticationResponse = authenticationResponse;
                FlowController.getInstance().goView("MenuGerente");
                FlowController.getInstance().goViewTop("BarraNavegacion");
            }

            else if(authenticationResponse.getRoles().getNombre().equals("Administrador")){
                FlowController.getInstance().authenticationResponse = authenticationResponse;
                FlowController.getInstance().goView("MenuAdministrador");
                FlowController.getInstance().goViewTop("BarraNavegacion");
            }

            else if(authenticationResponse.getRoles().getNombre().equals("Auditor")){
                FlowController.getInstance().authenticationResponse = authenticationResponse;
                FlowController.getInstance().goView("MenuAuditor");
                FlowController.getInstance().goViewTop("BarraNavegacion");
            }

            else{
                FlowController.getInstance().authenticationResponse = authenticationResponse;
                FlowController.getInstance().goView("MenuEmpleado");
                FlowController.getInstance().goViewTop("BarraNavegacion");
            }
       }
       
    }
    
}
