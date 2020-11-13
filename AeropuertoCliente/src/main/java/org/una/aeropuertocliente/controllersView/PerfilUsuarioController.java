package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.UsuarioWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class PerfilUsuarioController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private Label lbl_cedula;
    @FXML
    private Label lbl_nombre;
    @FXML
    private Label lbl_telefono;
    @FXML
    private Label lbl_jefe;
    @FXML
    private Label lbl_areaTrabajo;
    @FXML
    private Label lbl_rol;
    @FXML
    private HBox hb_password;
    @FXML
    private JFXTextField txt_passwordActual;
    @FXML
    private JFXTextField txt_passwordNueva;
    @FXML
    private JFXButton btn_operacion;
    
    private String AreaTrabajo = "Sin Asignar";
    private AuthenticationResponse authenticationResponse;
    private List<UsuarioAreaTrabajoDTO> usuarioAreaTrabajo;
           
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //TODO
    }    

    @Override
    public void initialize() {
        hb_password.setVisible(false);
        hb_password.setPrefSize(0,0);
        btn_operacion.setText("Modificar");
        authenticationResponse = FlowController.getInstance().authenticationResponse;
        usuarioAreaTrabajo = FlowController.getInstance().areaTrabajo;
  
        lbl_cedula.setText(authenticationResponse.getUsuario().getCedula());
        lbl_nombre.setText(authenticationResponse.getUsuario().getNombreCompleto());
        lbl_rol.setText(authenticationResponse.getRoles().getNombre());
        lbl_telefono.setText(authenticationResponse.getUsuario().getTelefono());
        if(authenticationResponse.getUsuario().getUsuarioJefe() != null)
            lbl_jefe.setText(authenticationResponse.getUsuario().getUsuarioJefe().getNombreCompleto());
        for (int i = 0; i < usuarioAreaTrabajo.toArray().length; i++) {
            if(usuarioAreaTrabajo.get(i).getAreaTrabajo() != null)
                AreaTrabajo = usuarioAreaTrabajo.get(i).getAreaTrabajo().getNombreArea()+"  ";   
        }
        lbl_areaTrabajo.setText(AreaTrabajo);
    }

    @Override
    public Node getRoot() {
        return root;
    } 

    @FXML
    private void volver(MouseEvent event) {
        elegirVentana();
    }

    @FXML
    private void operacion(MouseEvent event){
        if(btn_operacion.getText().equals("Modificar")){
            hb_password.setVisible(true);
            hb_password.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            btn_operacion.setText("Guardar");
        }
        else{
            if(txt_passwordActual.getText().equals("") || txt_passwordNueva.getText().equals("")){
                //implementar mensaje
            }
            else{
                try {
                    if(AutenticationWebService.login(authenticationResponse.getUsuario().getCedula(), txt_passwordActual.getText())!=null){
                        authenticationResponse.getUsuario().setPasswordEncriptado(txt_passwordNueva.getText());
                        UsuarioWebService.updateUsuario(authenticationResponse.getUsuario(),
                                authenticationResponse.getUsuario().getId(), authenticationResponse.getJwt());
                        FlowController.getInstance().authenticationResponse = null;
                        FlowController.getInstance().goView("login");
                    }
                    else{
                        //implementar mensaje
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(PerfilUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(PerfilUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(PerfilUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private void elegirVentana(){
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
