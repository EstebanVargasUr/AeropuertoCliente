package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
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

    @FXML private StackPane root;
    @FXML private JFXTextField txtCedula;
    @FXML private JFXPasswordField txtPassword;
    @FXML private ImageView cargando;
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       ModificarFormaCargando();
    }    
    
    @Override
    public Node getRoot() {
        return root;
    }    

    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    @FXML
    private void btnIniciaSesion(MouseEvent event) {
        CargaLogicaLogin();
    }      
    
    private void CargaLogicaLogin(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            try {
                AuthenticationResponse authenticationResponse = AutenticationWebService.login(txtCedula.getText(), txtPassword.getText());
                if(authenticationResponse != null)
                {
                    FlowController.getInstance().areaTrabajo = UsuarioAreaTrabajoWebService.getUsuarioAreaTrabajoByUsuarioId
                    (authenticationResponse.getUsuario().getId(), authenticationResponse.getJwt());
                    FlowController.getInstance().authenticationResponse = authenticationResponse;
                    
                    CargaGrafica();
                }   
               
            } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);}
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    }
    
    private void CargaGrafica(){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
            cargarVentanas();
       }
       });
    }
             
    private void cargarVentanas(){
        switch (FlowController.getInstance().authenticationResponse.getRoles().getNombre()){
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
