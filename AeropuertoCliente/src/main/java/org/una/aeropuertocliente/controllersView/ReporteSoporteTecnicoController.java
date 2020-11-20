package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.TransaccionDTO;
import org.una.aeropuertocliente.WebService.TransaccionWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class ReporteSoporteTecnicoController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private DatePicker datePFechaInicial;
    @FXML private DatePicker datePFechaFinal;
    @FXML private JFXComboBox<String> cb_filtro;
    @FXML private JFXComboBox<String> cb_filtroEstado;
    @FXML private TableView<ReporteSoporteC> tablaReporteSoporte;
    @FXML private TableColumn<ReporteSoporteC, String> tbc_id;
    @FXML private TableColumn<ReporteSoporteC, String> tbc_descripcion;
    @FXML private TableColumn<ReporteSoporteC, String> tbc_fechaRegistro;
    @FXML private TableColumn<ReporteSoporteC, String> tbc_nombre;
    @FXML private TableColumn<ReporteSoporteC, String> tbc_cedula;
    @FXML private TableColumn<ReporteSoporteC, String> tbc_rol;
    @FXML private TableColumn<ReporteSoporteC, String> tbc_estado;
    @FXML private TableColumn<ReporteSoporteC, String> tbc_modificarEstado;
    @FXML private ImageView cargando;

    public static ObservableList<ReporteSoporteC> DatosReportesSoporte;
    private AuthenticationResponse authenticationResponse;
    String EstadoReporteSoporte, AccionModificar;
    
    @Override
    public void initialize() {
        authenticationResponse = FlowController.getInstance().authenticationResponse;
        ModoDesarrollador();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        initTabla();
        initComboBox();  
    }    
    
    @Override
    public Node getRoot() {
        return root;
    }   

    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V10-G-RPA");
        else
            FlowController.getInstance().titulo("Reportes de Averias");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    private void initTabla(){
        initColumnas();
        tablaReporteSoporte.setEditable(true);
    }
    
    private void initColumnas(){
        
        tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tbc_descripcion.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        tbc_fechaRegistro.setCellValueFactory(new PropertyValueFactory<>("FechaRegistro"));
        tbc_nombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tbc_cedula.setCellValueFactory(new PropertyValueFactory<>("Cedula"));
        tbc_rol.setCellValueFactory(new PropertyValueFactory<>("Rol"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        tbc_modificarEstado.setCellValueFactory(new PropertyValueFactory<>("ModificarEstado"));
        
    }
    
    private void initComboBox(){
        
        cb_filtro.getItems().add("Estado");
        cb_filtro.getItems().add("Fecha de Registro");
        
        cb_filtroEstado.getItems().add("Activo");
        cb_filtroEstado.getItems().add("Inactivo");

    }
    
    @FXML
    private void cb_filtroAction(ActionEvent event) {
        
        datePFechaInicial.setPrefWidth(0);
        datePFechaInicial.setVisible(false);
        datePFechaFinal.setPrefWidth(0);
        datePFechaFinal.setVisible(false);
        cb_filtroEstado.setPrefWidth(0);    
        cb_filtroEstado.setVisible(false);
        
        if(cb_filtro.getValue().equals("Estado")){
            cb_filtroEstado.setPrefWidth(100);
            cb_filtroEstado.setVisible(true);
        }  
        if(cb_filtro.getValue().equals("Fecha de Registro")){
            datePFechaInicial.setPrefWidth(130);
            datePFechaInicial.setVisible(true);
            datePFechaFinal.setPrefWidth(130);
            datePFechaFinal.setVisible(true);
        }
    }

    @FXML
    private void btnBuscar(KeyEvent event) {
    }

    @FXML
    private void buscar(MouseEvent event) {
        CargaLogicaBusqueda();
    }

    private void RealizarBusqueda() {
        LimpiaDatos();
        DatosReportesSoporte = FXCollections.observableArrayList();
        try{
            busquedaLista();

            tablaReporteSoporte.setItems(DatosReportesSoporte);
        
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(ReporteSoporteTecnicoController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void busquedaLista() throws InterruptedException, IOException, ExecutionException {
        List<TransaccionDTO> ListaTransacciones = null;
        switch(cb_filtro.getValue()){

            case "Fecha de Registro":
                LocalDate localDate = datePFechaInicial.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date inicio = Date.from(instant);

                LocalDate localDate2 = datePFechaFinal.getValue();
                Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
                Date fin = Date.from(instant2);

                ListaTransacciones = TransaccionWebService.getTransaccionByFechaRegistroBetweenAndTipo(inicio,fin,"Soporte", authenticationResponse.getJwt());
            break;

            case "Estado": 
                Boolean Estado;
                if (cb_filtroEstado.getValue().equals("Activo")) 
                    Estado = true;
                else
                    Estado = false;
                ListaTransacciones = TransaccionWebService.getTransaccionByEstadoAndTipo(Estado,"Soporte", authenticationResponse.getJwt());
            break;

            default:
                break;
        }
        for(TransaccionDTO transaccion : ListaTransacciones){
            if(transaccion.getEstado()){
                EstadoReporteSoporte = "Activo";
                AccionModificar = "Inactivar";
            }
            else{
                EstadoReporteSoporte = "Inactivo";
                AccionModificar = "Activar";
            }
            
            ReporteSoporteC transaccion1 = new ReporteSoporteC(transaccion.getId(),transaccion.getInformacion(),
                transaccion.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                    EstadoReporteSoporte, transaccion.getUsuario().getNombreCompleto(), transaccion.getUsuario().getCedula(),
                        transaccion.getUsuario().getRol().getNombre(), new JFXButton(AccionModificar));

            DatosReportesSoporte.add(transaccion1);
        }
    }
    
    private void LimpiaDatos(){
        ObservableList items = FXCollections.observableArrayList(); 
        tablaReporteSoporte.setItems(items);    
    }

     private void CargaLogicaBusqueda(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            
            RealizarBusqueda(); 
            
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    }

    public class ReporteSoporteC {
    
        Long Id;  
        String Descripcion;
        String FechaRegistro;
        String Estado;
        String Nombre;
        String Cedula;
        String Rol;
        JFXButton ModificarEstado;

        public ReporteSoporteC(Long Id, String Descripcion, String FechaRegistro, String Estado,
                String Nombre, String Cedula, String Rol, JFXButton ModificarEstado) {
            this.Id = Id;
            this.Descripcion = Descripcion;
            this.FechaRegistro = FechaRegistro;
            this.Estado = Estado;
            this.Nombre = Nombre;
            this.Cedula = Cedula;
            this.Rol = Rol;
            this.ModificarEstado = ModificarEstado;

            ModificarEstado.getStyleClass().add("jfx-buttonStandard1");
            
            ModificarEstado.setOnAction(e -> {
                for(ReporteSoporteC reporte : DatosReportesSoporte){
                    if(reporte.getModificarEstado() == ModificarEstado){
                        Thread t = new Thread(new Runnable(){
                        public void run(){
                            cargando.setVisible(true);
                            root.setDisable(true);
                            try{
                                TransaccionDTO transaccion = TransaccionWebService.getTransaccionById(reporte.getId(), authenticationResponse.getJwt());
                                if(transaccion.getEstado())
                                    transaccion.setEstado(false);
                                else
                                    transaccion.setEstado(true);
                                TransaccionWebService.updateTransaccion(transaccion, transaccion.getId(), authenticationResponse.getJwt());
                                CargaLogicaBusqueda();
                            } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(ReporteSoporteTecnicoController.class.getName()).log(Level.SEVERE, null, ex);}
                            cargando.setVisible(false);
                            root.setDisable(false);
                        }
                        });
                        t.start();
                    }
                }

            }); 
        }

        public ReporteSoporteC() {
        }

        public Long getId() {
            return Id;
        }

        public String getDescripcion() {
            return Descripcion;
        }

        public String getFechaRegistro() {
            return FechaRegistro;
        }

        public String getEstado() {
            return Estado;
        }

        public String getNombre() {
            return Nombre;
        }

        public String getCedula() {
            return Cedula;
        }

        public String getRol() {
            return Rol;
        }

        public JFXButton getModificarEstado() {
            return ModificarEstado;
        }

    }
}
