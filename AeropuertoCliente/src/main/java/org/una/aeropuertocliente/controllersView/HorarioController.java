package org.una.aeropuertocliente.controllersView;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class HorarioController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private TableView<?> tablaHorario;
    @FXML
    private TableColumn<?, ?> tbc_diaEntrada;
    @FXML
    private TableColumn<?, ?> tbc_diaSalida;
    @FXML
    private TableColumn<?, ?> tbc_horaEntrada;
    @FXML
    private TableColumn<?, ?> tbc_horaSalida;

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
    private void tablaServiciosClic(MouseEvent event) {
    }
    
}
