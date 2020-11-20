package org.una.aeropuertocliente.controllersView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.una.aeropuertocliente.DTOs.HoraMarcajeDTO;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.WebService.HoraMarcajeWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class MenuAdministradorController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private Label lbl_ultimaHora;
    @FXML private ImageView cargando;
    @FXML private Label lbl_ultimaHora2;

    UsuarioDTO usuario = FlowController.getInstance().authenticationResponse.getUsuario();
    String token = FlowController.getInstance().authenticationResponse.getJwt();
    Mensaje msg = new Mensaje();
    int TipoMarcaje = 0; // 1 = Entrada | 2 = Salida
    
    @Override
    public void initialize() {
       ModoDesarrollador();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        CargaLogicaMenuAdministrador();
    }    
    
    @Override
    public Node getRoot() {
        return root;
    }     
    
    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V05-M-ADM");
        else
            FlowController.getInstance().titulo("Menu Administrador");
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
    private void reportesAverias(MouseEvent event) {
        boolean Entrar = false;
        for(UsuarioAreaTrabajoDTO areaTrabajo : FlowController.getInstance().areaTrabajo)
            if(areaTrabajo.getAreaTrabajo().getNombreArea().equals("Departamento de Informática"))
                Entrar = true;
        if(Entrar)
            FlowController.getInstance().goView("ReporteSoporteTecnico");
        else
            msg.alerta(root, "Alerta", "Necesita pertener al Area de Departamento de Informática para ingresar\n"
                    + "a la ventana de Reportes de Averias");
    }

    @FXML
    private void parametrosSistema(MouseEvent event) {
        boolean Entrar = false;
        for(UsuarioAreaTrabajoDTO areaTrabajo : FlowController.getInstance().areaTrabajo)
            if(areaTrabajo.getAreaTrabajo().getNombreArea().equals("Departamento de Informática"))
                Entrar = true;
        if(Entrar)
            FlowController.getInstance().goView("ParametroSistema");
        else
            msg.alerta(root, "Alerta", "Necesita pertener al Area de Departamento de Informática para ingresar\n"
                    + "a la ventana de Parametros de Sistema");
        
    }
    
    @FXML
    private void averia(MouseEvent event) {
        msg.reporteAveria(root, cargando);
    }
    
    
    @FXML
    private void horario(MouseEvent event) {
        FlowController.getInstance().goView("Horario");
    }
    private void CargaLogicaMenuAdministrador(){
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
    
    private void CargaGraficaMsgConfimar(String cuerpo){
        Platform.runLater(new Runnable() {
        @Override public void run() {

            MsgConfirmarMarcaje("Confirmación", cuerpo);

       }
       });
    }

    @FXML
    private void marcarEntrada(MouseEvent event) throws InterruptedException, ExecutionException, JsonProcessingException, IOException 
    {
        TipoMarcaje = 1;
        CargaGraficaMsgConfimar("¿Desea realizar el marcaje de la entrada?");  
    }

    @FXML
    private void marcarSalida(MouseEvent event) throws InterruptedException, ExecutionException, IOException 
    {
        TipoMarcaje = 2;
        CargaGraficaMsgConfimar("¿Desea realizar el marcaje de la salida?"); 
    }

    
    
    private void MarcaEntrada() throws InterruptedException, ExecutionException, JsonProcessingException, IOException 
    {     
        HoraMarcajeDTO UltimaHoraMarcaje = new HoraMarcajeDTO();
        UltimaHoraMarcaje = HoraMarcajeWebService.getUltimaHoraMarcajeByUsuarioId(usuario.getId(), token);
        
        if (UltimaHoraMarcaje.getHoraEntrada() == null || UltimaHoraMarcaje.getHoraEntrada() != null && UltimaHoraMarcaje.getHoraSalida()!= null) 
        {
            HoraMarcajeDTO horaMarcaje = new HoraMarcajeDTO();
            horaMarcaje.setUsuario(usuario);
            HoraMarcajeWebService.createHoraMarcaje(horaMarcaje, usuario, token);
        }
        else
        {msg.alerta(root, "Información", "Ya existe una hora de entrada en el sistema");}
        
        MostrarUltimaHoraMarcaje();
    }
    
    private void MarcaSalida() throws InterruptedException, ExecutionException, IOException 
    {
        HoraMarcajeDTO UltimaHoraMarcaje = new HoraMarcajeDTO();
        UltimaHoraMarcaje = HoraMarcajeWebService.getUltimaHoraMarcajeByUsuarioId(usuario.getId(), token);
        
        if (UltimaHoraMarcaje.getHoraEntrada() != null && UltimaHoraMarcaje.getHoraSalida() == null) 
        {
            HoraMarcajeWebService.updateHoraMarcaje(UltimaHoraMarcaje, UltimaHoraMarcaje.getId(), token);
        }
        else
        {msg.alerta(root, "Información", "Ya existe una hora de salida resgistrada en el sistema");}
        
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
    
    private void MsgConfirmarMarcaje(String titulo,String cuerpo)
    {
        JFXDialogLayout contenido = new JFXDialogLayout();
        contenido.setHeading(new Text(titulo));
        contenido.setBody(new Text(cuerpo));
        JFXDialog dialogo = new JFXDialog(root, contenido, JFXDialog.DialogTransition.RIGHT);
        JFXButton botonAceptar = new JFXButton("Aceptar");
        JFXButton botonCancelar = new JFXButton("Cancelar");
        
        botonAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                
                try {
                    
                    if (TipoMarcaje == 1) {
                        MarcaEntrada();
                    }
                    if (TipoMarcaje == 2) {
                        MarcaSalida();
                    }
                    
                    TipoMarcaje = 0;
                    
                } catch (InterruptedException | ExecutionException | IOException ex) {
                    Logger.getLogger(MenuGestorController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                dialogo.close();
            }
        });
        
        botonCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                dialogo.close();
            }
        });
        contenido.setActions(botonAceptar,botonCancelar);
        dialogo.show();
    }

    @FXML
    private void modoDesarrollador(MouseEvent event) {
        if(FlowController.getInstance().modoDesarrollo == true){
            FlowController.getInstance().modoDesarrollo = false;
            FlowController.getInstance().titulo("Menu Administrador");
        }
        else{
            FlowController.getInstance().modoDesarrollo = true;
            FlowController.getInstance().titulo("V01-M-ADM");
        }
    }
}
