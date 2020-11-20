package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.UsuarioWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class PerfilUsuarioController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private Label lbl_cedula;
    @FXML private Label lbl_nombre;
    @FXML private Label lbl_telefono;
    @FXML private Label lbl_jefe;
    @FXML private Label lbl_areaTrabajo;
    @FXML private Label lbl_rol;
    @FXML private HBox hb_password;
    @FXML private JFXPasswordField txt_passwordActual;
    @FXML private JFXPasswordField txt_passwordNueva;
    @FXML private JFXButton btn_operacion;
    @FXML private ImageView cargando;
    
    private String AreaTrabajo = "Sin Asignar";
    private AuthenticationResponse authenticationResponse;
    private List<UsuarioAreaTrabajoDTO> usuarioAreaTrabajo;  
    Mensaje msg = new Mensaje();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
    }    

    @Override
    public void initialize() {
        ModoDesarrollador();
        hb_password.setVisible(false);
        hb_password.setPrefSize(0,0);
        btn_operacion.setText("Modificar contraseña");
        authenticationResponse = FlowController.getInstance().authenticationResponse;
        usuarioAreaTrabajo = FlowController.getInstance().areaTrabajo;
  
        lbl_cedula.setText(authenticationResponse.getUsuario().getCedula());
        lbl_nombre.setText(authenticationResponse.getUsuario().getNombreCompleto());
        lbl_rol.setText(authenticationResponse.getRoles().getNombre());
        lbl_telefono.setText(authenticationResponse.getUsuario().getTelefono());
        AreaTrabajo="";
        
        if(authenticationResponse.getUsuario().getUsuarioJefe() != null)
            lbl_jefe.setText(authenticationResponse.getUsuario().getUsuarioJefe().getNombreCompleto());
        
        for (int i = 0; i < usuarioAreaTrabajo.toArray().length; i++) 
            if(usuarioAreaTrabajo.get(i).getAreaTrabajo() != null)
                AreaTrabajo += usuarioAreaTrabajo.get(i).getAreaTrabajo().getNombreArea()+"\n";   
        
        lbl_areaTrabajo.setText(AreaTrabajo);
    }

    @Override
    public Node getRoot() {
        return root;
    } 

    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V09-M-PRF");
        else
            FlowController.getInstance().titulo("Perfil de Usuario");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    @FXML
    private void volver(MouseEvent event) {
        elegirVentana();
    }

    @FXML
    private void operacion(MouseEvent event){
        CargaLogicaOperacion();
    }
    
    private void RealizarOperacion(){
        if(btn_operacion.getText().equals("Modificar contraseña")){
            hb_password.setVisible(true);
            hb_password.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            btn_operacion.setText("Guardar");
        }
        else{
            if(txt_passwordActual.getText().equals("") || txt_passwordNueva.getText().equals("")){
                CargaGraficaMsg("Rellene los campos requeridos para realizar la modificación");
            }
            else{         
                try {
                    if(AutenticationWebService.login(authenticationResponse.getUsuario().getCedula(), txt_passwordActual.getText(),root)!=null){
                        authenticationResponse.getUsuario().setPasswordEncriptado(txt_passwordNueva.getText());
                        UsuarioWebService.updateUsuario(authenticationResponse.getUsuario(),
                                authenticationResponse.getUsuario().getId(), authenticationResponse.getJwt());
                        FlowController.getInstance().authenticationResponse = null;
                        FlowController.getInstance().goView("login");
                    }
                    else{
                        CargaGraficaMsg("Contraseña incorrecta");
                    }

                } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaController.class.getName()).log(Level.SEVERE, null, ex);}
            }
        }
    }
    
    private void CargaLogicaOperacion(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            
            CargaGrafica();
            
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    }
    
    private void CargaGrafica(){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            RealizarOperacion(); 
        }
        });
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
    
    private void CargaGraficaMsg(String cuerpo){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
            msg.alerta(root, "Alerta", cuerpo);
        }
        });
    }
}
