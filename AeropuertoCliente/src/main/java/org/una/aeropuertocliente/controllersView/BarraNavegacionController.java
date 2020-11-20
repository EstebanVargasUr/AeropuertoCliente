package org.una.aeropuertocliente.controllersView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class BarraNavegacionController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private Label lbl_areaTrabajo;
    @FXML private Label lbl_rol;
    @FXML private Label lbl_usuario;

    private AuthenticationResponse authenticationResponse;
    private String AreaTrabajo;
    private List<UsuarioAreaTrabajoDTO> usuarioAreaTrabajo;
    
    @Override
    public void initialize() {

        root.setVisible(true);
        root.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        authenticationResponse = FlowController.getInstance().authenticationResponse;
        usuarioAreaTrabajo = FlowController.getInstance().areaTrabajo;
        AreaTrabajo="";
        
        lbl_usuario.setText(authenticationResponse.getUsuario().getNombreCompleto());
        lbl_rol.setText(authenticationResponse.getRoles().getNombre());
        for (UsuarioAreaTrabajoDTO usuarioTrabajo : usuarioAreaTrabajo) 
            AreaTrabajo+= usuarioTrabajo.getAreaTrabajo().getNombreArea()+",  ";   
        
        lbl_areaTrabajo.setText(AreaTrabajo);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @Override
    public Node getRoot() {
        return root;
    }      

    @FXML
    private void inicio(MouseEvent event) {
        
        if(authenticationResponse.getRoles().getNombre().equals("Gestor")){
            FlowController.getInstance().goView("MenuGestor");
        }
        else if(authenticationResponse.getRoles().getNombre().equals("Gerente")){
            FlowController.getInstance().goView("MenuGerente");
        }
        else if(authenticationResponse.getRoles().getNombre().equals("Administrador")){
            FlowController.getInstance().goView("MenuAdministrador");
        }
        else if(authenticationResponse.getRoles().getNombre().equals("Auditor")){
            FlowController.getInstance().goView("MenuAuditor");
        }
        else{
            FlowController.getInstance().goView("MenuEmpleado");
        }
    }

    @FXML
    private void perfil(MouseEvent event) {
        FlowController.getInstance().goView("PerfilUsuario");
    }

    @FXML
    private void salir(MouseEvent event) {
        FlowController.getInstance().authenticationResponse = null;
        FlowController.getInstance().modoDesarrollo = false;
        FlowController.getInstance().initialize();
        FlowController.getInstance().goView("login");
        root.setVisible(false);
        root.setPrefSize(0, 0);
    }
    
}
