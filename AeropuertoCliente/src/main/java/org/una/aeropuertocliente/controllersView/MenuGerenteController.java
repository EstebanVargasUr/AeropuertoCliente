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
public class MenuGerenteController extends Controller implements Initializable {

    @FXML
    private StackPane root;
    @FXML
    private JFXButton btnAccion;
    @FXML
    private JFXButton btnListado;
    @FXML
    private JFXButton btnAlerta;
    @FXML
    private Label lbl_ultimaHora;
    @FXML
    private ImageView cargando;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
    }  
    
    @Override
    public void initialize() {
       
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
    private void marcarEntrada(MouseEvent event) {
    }

    @FXML
    private void marcarSalida(MouseEvent event) {
    }

    @FXML
    private void horario(MouseEvent event) {
        FlowController.getInstance().goView("Horario");
    }

    @FXML
    private void accionesGestores(MouseEvent event) {
    }

    @FXML
    private void listadosGerenciales(MouseEvent event) {
    }

    @FXML
    private void alertasNotificaciones(MouseEvent event) {
    }
    
}
