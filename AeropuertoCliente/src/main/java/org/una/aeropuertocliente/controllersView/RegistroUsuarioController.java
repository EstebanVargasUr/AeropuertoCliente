package org.una.aeropuertocliente.controllersView;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.una.aeropuertocliente.WebService.UsuarioWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class RegistroUsuarioController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXButton btnGuardar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    

    @Override
    public void initialize() {
        root.styleProperty().set("-fx-background-color: #4AB19D");
    }

    @Override
    public Node getRoot() {
        return root;
    } 

    @FXML
    private void onActionBtnCancelar(ActionEvent event) {
        FlowController.getInstance().goView("login");
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) throws InterruptedException, ExecutionException, JsonMappingException, IOException {
       //  UsuarioWebService.getAllUsuarios();
    }
    
}
