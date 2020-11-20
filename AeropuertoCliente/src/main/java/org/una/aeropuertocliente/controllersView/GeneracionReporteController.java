/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuertocliente.controllersView;

/**
 *
 * @author Daniel
 */
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javax.swing.WindowConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;
import org.una.aeropuertocliente.DataSource.DataRecorridosAviones;
import org.una.aeropuertocliente.DataSource.DataServiciosFecha;
import org.una.aeropuertocliente.utility.FlowController;

public class GeneracionReporteController extends Controller implements  Initializable {

    @FXML
    private StackPane root;

    @FXML
    private DatePicker datePFechaInicial;

    @FXML
    private JFXComboBox<String> cb_tipoServicio;

    @FXML
    private DatePicker datePFechaFinal;

    @FXML
    private JFXComboBox<String> cb_tipoReporte;

    @FXML
    private JFXButton btnGenerarReporte;

    @FXML
    private ImageView cargando;
    
     @FXML
    private JFXComboBox<String> cb_Zona;

    @FXML
    private JFXTextField txtAerolinea;

    String token;
    String AreaTrabajo;
    private List<UsuarioAreaTrabajoDTO> usuarioAreaTrabajo;
    String rol=FlowController.getInstance().authenticationResponse.getUsuario().getRol().getNombre();
    long idTipoServicio;
    long idZona;
    @Override
    public void initialize() {
    }
    @Override
    public Node getRoot() {
       return root;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        token = FlowController.getInstance().authenticationResponse.getJwt();
        try {
            initComboBox();
        } catch (InterruptedException ex) {
            Logger.getLogger(GeneracionReporteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(GeneracionReporteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneracionReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
     
     private void CargaLogicaGuardar(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            CargaLogica();
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    } 
     
    private void initComboBox() throws InterruptedException, ExecutionException, IOException{
       usuarioAreaTrabajo= FlowController.getInstance().areaTrabajo;
        cb_tipoReporte.getItems().add("  ");
        for(UsuarioAreaTrabajoDTO usuarioAreas:usuarioAreaTrabajo){
            AreaTrabajo = usuarioAreas.getAreaTrabajo().getNombreArea();
            if(rol.equals("Gerente")&&AreaTrabajo.equals("Auditoria")){
                cb_tipoReporte.getItems().add("Recorrido de aviones entre fechas, por aerolinea y por zonas");
                cb_tipoReporte.getItems().add("Recaudacion de Servicios entre fechas y por Tipo de Servicio");
                cb_tipoReporte.getItems().add("Horas Laboradas contra horario, por empleado, area y entre fechas");
            }
            if(rol.equals("Auditor")||rol.equals("Gerente")&&AreaTrabajo.equals("Torre de Control")){
                 cb_tipoReporte.getItems().add("Recorrido de aviones entre fechas, por aerolinea y por zonas");
            }
            if(rol.equals("Auditor")||rol.equals("Gerente")&&AreaTrabajo.equals("Servicios")){
                cb_tipoReporte.getItems().add("Recaudacion de Servicios entre fechas y por Tipo de Servicio");
            }
            if(rol.equals("Auditor")||rol.equals("Gerente")&&AreaTrabajo.equals("Recursos Humanos")){
                cb_tipoReporte.getItems().add("Horas Laboradas contra horario, por empleado, area y entre fechas");
            }
        }
        cb_tipoServicio.getItems().add("Carga de combustible");
        cb_tipoServicio.getItems().add("Uso de hangares");
        cb_tipoServicio.getItems().add("Mantenimiento preventivo");
        cb_tipoServicio.getItems().add("Correctivos de aeronaves");
        
        cb_Zona.getItems().add("Descarga");
        cb_Zona.getItems().add("Hangar");
        cb_Zona.getItems().add("Mantenimiento");
        cb_Zona.getItems().add("Carga de Combustible");
     }
     
    @FXML
    void cb_tipoReporteOnAction(ActionEvent event) {
        datePFechaInicial.setPrefWidth(0);  datePFechaInicial.setVisible(false);
        datePFechaFinal.setPrefWidth(0);    datePFechaFinal.setVisible(false);
        cb_tipoServicio.setPrefWidth(0);    cb_tipoServicio.setVisible(false);
        cb_Zona.setVisible(false);
        txtAerolinea.setVisible(false);
        if (cb_tipoReporte.getValue().equals("Recaudacion de Servicios entre fechas y por Tipo de Servicio")) {
            cb_tipoServicio.setPrefWidth(250);
            cb_tipoServicio.setVisible(true);
            datePFechaInicial.setPrefWidth(130);
            datePFechaInicial.setVisible(true);
            datePFechaFinal.setPrefWidth(130);
            datePFechaFinal.setVisible(true);
        }  
        if(cb_tipoReporte.getValue().equals("Recorrido de aviones entre fechas, por aerolinea y por zonas")){
            datePFechaInicial.setPrefWidth(130);
            datePFechaInicial.setVisible(true);
            datePFechaFinal.setPrefWidth(130);
            datePFechaFinal.setVisible(true);
            cb_Zona.setVisible(true);
            txtAerolinea.setVisible(true);
        }
    }
    
    @FXML
    void cb_tipoServicioOnAction(ActionEvent event) {
        if(cb_tipoServicio.getValue().toString().equals("Carga de combustible")){
            idTipoServicio=1;
        }
        if(cb_tipoServicio.getValue().toString().equals("Uso de hangares")){
            idTipoServicio=2;
        }
        if(cb_tipoServicio.getValue().toString().equals("Mantenimiento preventivo")){
            idTipoServicio=3;
        }
        if(cb_tipoServicio.getValue().toString().equals("Correctivos de aeronaves")){
            idTipoServicio=4;
        }
        
    }
    
    @FXML
    void cb_ZonaOnAction(ActionEvent event) {
        if(cb_Zona.getValue().toString().equals("Descarga")){
            idZona=5;
        }
        if(cb_Zona.getValue().toString().equals("Hangar")){
            idZona=6;
        }
        if(cb_Zona.getValue().toString().equals("Mantenimiento")){
            idZona=7;
        }
        if(cb_Zona.getValue().toString().equals("Carga de Combustible")){
            idZona=8;
        }
    }
    
    @FXML
    void generarReporte(MouseEvent event) {
      
        CargaLogicaGuardar();
    }
    
    private void CargaLogica(){
        
        if (cb_tipoReporte.getValue().equals("Recorrido de aviones entre fechas, por aerolinea y por zonas")){
         
          LocalDate localDate = datePFechaInicial.getValue();
          Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
          Date inicio = Date.from(instant);
          FlowController.getInstance().FechaIni=inicio;
          LocalDate localDate2 = datePFechaFinal.getValue();
          Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
          Date fin = Date.from(instant2);
          FlowController.getInstance().FechaFinal=fin;
          FlowController.getInstance().idZona=idZona;
          FlowController.getInstance().idAerolinea= Long.parseLong(txtAerolinea.getText());
        
            try{
                JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/org/una/aeropuertocliente/reports/recorridos_aviones_fechas.jasper"));
                JasperPrint jprint = JasperFillManager.fillReport(report, null, DataRecorridosAviones.getDatasSource());
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                view.setVisible(true);
            }catch(JRException ex){
                ex.getMessage();} catch (InterruptedException ex) {
                Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (cb_tipoReporte.getValue().equals("Recaudacion de Servicios entre fechas y por Tipo de Servicio")){
            
            FlowController.getInstance().idTipoServicio=idTipoServicio;
            LocalDate localDate = datePFechaInicial.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date inicio = Date.from(instant);
            FlowController.getInstance().FechaIni=inicio;
            LocalDate localDate2 = datePFechaFinal.getValue();
            Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
            Date fin = Date.from(instant2);
            FlowController.getInstance().FechaFinal=fin;
            try{
                JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/org/una/aeropuertocliente/reports/recaudacion_servicios_fechas.jasper"));
                JasperPrint jprint = JasperFillManager.fillReport(report, null, DataServiciosFecha.getDatasSource());
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                view.setVisible(true);
            }catch(JRException ex){
                ex.getMessage();} catch (InterruptedException ex) {
                Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
    }
}


        


