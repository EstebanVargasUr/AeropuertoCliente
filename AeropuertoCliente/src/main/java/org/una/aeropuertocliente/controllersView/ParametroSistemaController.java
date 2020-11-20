package org.una.aeropuertocliente.controllersView;

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
import org.una.aeropuertocliente.DTOs.ParametroSistemaDTO;
import org.una.aeropuertocliente.WebService.ParametroSistemaWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class ParametroSistemaController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private JFXTextField txt_maximaSesion;
    @FXML private JFXTextField txt_cierreInactividad;
    @FXML private ImageView cargando;

    private AuthenticationResponse authenticationResponse;
    private ParametroSistemaDTO parametroSistema;
    private Mensaje mensaje = new Mensaje();
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        
    }    

    @Override
    public void initialize() {
        authenticationResponse = FlowController.getInstance().authenticationResponse;
        root.styleProperty().set("-fx-background-color: #4AB19D"); 
        ModoDesarrollador();
        CargaLogica();
    }

    @Override
    public Node getRoot() {
        return  root;
    }
    
    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V08-G-PRS");
        else
            FlowController.getInstance().titulo("Parametros del Sistema");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    @FXML
    private void volver(MouseEvent event) {
        FlowController.getInstance().goView("MenuAdministrador");
    }

    @FXML
    private void guardar(MouseEvent event) {
        if(!parametroSistema.getValor().equals(txt_maximaSesion.getText()))
            CargaLogicaGuardar();
        else
            mensaje.alerta(root, "Parametro de tiempo maximó de sesión", "El parametro ya cuenta con el valor definido");
    }
    
    private void RealizarCargar(){
        try {
            parametroSistema = ParametroSistemaWebService.getParametroSistemaById(1, authenticationResponse.getJwt());
       } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(ParametroSistemaController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void RealizarGuardar(){
        try {
            parametroSistema.setValor(txt_maximaSesion.getText());
            ParametroSistemaWebService.updateParametroSistema(parametroSistema,1, authenticationResponse.getJwt());
       } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(ParametroSistemaController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void CargaLogica(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            
            RealizarCargar(); 
            CargaGrafica();
            
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    }
    
    private void CargaLogicaGuardar(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            
            RealizarGuardar(); 
            
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    }
    
    private void CargaGrafica(){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            txt_maximaSesion.setText(parametroSistema.getValor());
        }
        });
    }
}
