package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class MenuAdministradorController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private JFXButton btnAutorizar;
    @FXML private JFXButton btnDesarrollo;
    @FXML private Label lbl_ultimaHora;
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
    private void autorizarRoles(MouseEvent event) {
    }

    @FXML
    private void modoDesarrollo(MouseEvent event) {   
    }

    @FXML
    private void marcarEntrada(MouseEvent event) {
    }

    @FXML
    private void marcarSalida(MouseEvent event) {
    }

    @FXML
    private void horario(MouseEvent event) {
        FlowController.getInstance().goView("Horario");
    }
    
}
