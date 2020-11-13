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
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.UsuarioAreaTrabajoWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class LoginController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField txtCedula;
    @FXML
    private JFXPasswordField txtPassword;

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
    private void btnIniciaSesion(MouseEvent event) throws InterruptedException, ExecutionException, IOException {
         AuthenticationResponse authenticationResponse = AutenticationWebService.login(txtCedula.getText(), txtPassword.getText());
       
       if(authenticationResponse != null)
       {
           FlowController.getInstance().areaTrabajo = UsuarioAreaTrabajoWebService.getUsuarioAreaTrabajoByUsuarioId
            (authenticationResponse.getUsuario().getId(), authenticationResponse.getJwt());
           FlowController.getInstance().authenticationResponse = authenticationResponse;
           
           switch (authenticationResponse.getRoles().getNombre()){
               case "Gestor":
                   FlowController.getInstance().goView("MenuGestor");
                   FlowController.getInstance().goViewTop("BarraNavegacion");
                   break;
                        case "Gerente":
                            FlowController.getInstance().goView("MenuGerente");
                            FlowController.getInstance().goViewTop("BarraNavegacion");
                            break;
                        
                            case "Administrador":
                                FlowController.getInstance().goView("MenuAdministrador");
                                FlowController.getInstance().goViewTop("BarraNavegacion");
                                break;
                   
                                case "Auditor":
                                    FlowController.getInstance().goView("MenuAuditor");
                                    FlowController.getInstance().goViewTop("BarraNavegacion");
                                    break;
                   
                                    default :
                                        FlowController.getInstance().goView("MenuEmpleado");
                                        FlowController.getInstance().goViewTop("BarraNavegacion");
                                        break;
           }
            
       }
    }
    
}
