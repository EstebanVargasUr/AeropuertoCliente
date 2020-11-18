 package org.una.aeropuertocliente.controllersView;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.una.aeropuertocliente.DTOs.HoraMarcajeDTO;
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.WebService.HoraMarcajeWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class MenuGestorController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private Label lbl_ultimaHora;
    @FXML private ImageView cargando;
    @FXML private Label lbl_ultimaHora2;
    
    private final UsuarioDTO usuario = FlowController.getInstance().authenticationResponse.getUsuario();
    private final String token = FlowController.getInstance().authenticationResponse.getJwt();
    private final Mensaje mensaje = new Mensaje();
    
    @Override
    public void initialize() {
        ModoDesarrollador();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        CargaLogicaMenuGestor();
    }    
    
    @Override
    public Node getRoot() {
        return root;
    } 
    
    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V07-M-GES");
        else
            FlowController.getInstance().titulo("Menu Gestor");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    private void CargaLogicaMenuGestor(){
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
            
        try {
            
            MostrarUltimaHoraMarcaje();
            
        } catch (InterruptedException | ExecutionException | IOException ex) {
            Logger.getLogger(MenuGestorController.class.getName()).log(Level.SEVERE, null, ex);
        }     
       }
       });
    }
    
    @FXML
    private void averia(MouseEvent event) {
        mensaje.reporteAveria(root, cargando);
    }
    
    @FXML
    private void servicios(MouseEvent event) {
        FlowController.getInstance().goView("Servicio");
    }

    @FXML
    private void aerolineas(MouseEvent event) {
        FlowController.getInstance().goView("Aerolinea");
    }

    @FXML
    private void vuelos(MouseEvent event) {
        FlowController.getInstance().goView("Vuelo");
    }
    
    @FXML
    private void usuarios(MouseEvent event) {
        FlowController.getInstance().goView("Usuario");
    }
    
    @FXML
    private void horario(MouseEvent event) {
        FlowController.getInstance().goView("Horario");
    }
    
    @FXML
    private void marcarEntrada(MouseEvent event) throws InterruptedException, ExecutionException, JsonProcessingException, IOException 
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        
        HoraMarcajeDTO UltimaHoraMarcaje = new HoraMarcajeDTO();
        UltimaHoraMarcaje = HoraMarcajeWebService.getUltimaHoraMarcajeByUsuarioId(usuario.getId(), token);
        
        if (UltimaHoraMarcaje.getHoraEntrada() == null || UltimaHoraMarcaje.getHoraEntrada() != null && UltimaHoraMarcaje.getHoraSalida()!= null) 
        {
            HoraMarcajeDTO horaMarcaje = new HoraMarcajeDTO();
            horaMarcaje.setUsuario(usuario);
            HoraMarcajeWebService.createHoraMarcaje(horaMarcaje, usuario, token);
        }
        else
        {
           
            alert.setContentText("YA SE MARCÓ HORA DE ENTRADA");
            alert.showAndWait();
        }
        
        MostrarUltimaHoraMarcaje();
    }

    @FXML
    private void marcarSalida(MouseEvent event) throws InterruptedException, ExecutionException, IOException 
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
         
        HoraMarcajeDTO UltimaHoraMarcaje = new HoraMarcajeDTO();
        UltimaHoraMarcaje = HoraMarcajeWebService.getUltimaHoraMarcajeByUsuarioId(usuario.getId(), token);
        
        if (UltimaHoraMarcaje.getHoraEntrada() != null && UltimaHoraMarcaje.getHoraSalida() == null) 
        {
            HoraMarcajeWebService.updateHoraMarcaje(UltimaHoraMarcaje, UltimaHoraMarcaje.getId(), token);
        }
        else
        {
            alert.setContentText("YA SE MARCÓ HORA DE SALIDA");
            alert.showAndWait();
            
        } 
        
        MostrarUltimaHoraMarcaje();
    } 
    
    private void MostrarUltimaHoraMarcaje( ) throws InterruptedException, ExecutionException, IOException
    {
        HoraMarcajeDTO UltimaHoraMarcaje = new HoraMarcajeDTO();
        UltimaHoraMarcaje = HoraMarcajeWebService.getUltimaHoraMarcajeByUsuarioId(usuario.getId(), token);
            
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");
        lbl_ultimaHora.setText("Entrada: " + UltimaHoraMarcaje.getHoraEntrada().toInstant().atZone(ZoneId.systemDefault()).format(formatter));
        
        if (UltimaHoraMarcaje.getHoraSalida() == null) 
        {
            lbl_ultimaHora2.setText("Salida: sin registrar");
        }
        else
        {
            lbl_ultimaHora2.setText("Salida: " + UltimaHoraMarcaje.getHoraSalida().toInstant().atZone(ZoneId.systemDefault()).format(formatter));
        }
    }
}
