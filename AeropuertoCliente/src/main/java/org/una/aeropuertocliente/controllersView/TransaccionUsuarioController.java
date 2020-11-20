package org.una.aeropuertocliente.controllersView;

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
import javafx.application.Platform;
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
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.WebService.TransaccionWebService;
import org.una.aeropuertocliente.WebService.UsuarioWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class TransaccionUsuarioController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private DatePicker datePFechaInicial;
    @FXML private DatePicker datePFechaFinal;
    @FXML private JFXTextField txt_buscarCedula;
    @FXML private JFXComboBox<String> cb_filtro;
    @FXML private TableView<TransaccionU> tablaReporteSoporte;
    @FXML private TableColumn<TransaccionU, String> tbc_id;
    @FXML private TableColumn<TransaccionU, String> tbc_descripcion;
    @FXML private TableColumn<TransaccionU, String> tbc_fechaRegistro;
    @FXML private TableColumn<TransaccionU, String> tbc_nombre;
    @FXML private TableColumn<TransaccionU, String> tbc_cedula;
    @FXML private TableColumn<TransaccionU, String> tbc_rol;
    @FXML private ImageView cargando;
    
    public static ObservableList<TransaccionU> DatosTransacciones;
    private AuthenticationResponse authenticationResponse;
    private Mensaje msg = new Mensaje();
    
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
           FlowController.getInstance().titulo("V14-G-TRU");
        else
            FlowController.getInstance().titulo("Transacciones de Usuarios");
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
        
    }
    
    private void initComboBox(){
        
        cb_filtro.getItems().add("Cédula de Empleado");
        cb_filtro.getItems().add("Fecha de Registro");

    }
    
    @FXML
    private void cb_filtroAction(ActionEvent event) {
        
        datePFechaInicial.setPrefWidth(0);
        datePFechaInicial.setVisible(false);
        datePFechaFinal.setPrefWidth(0);
        datePFechaFinal.setVisible(false);
        txt_buscarCedula.setPrefWidth(0);
        
        if(cb_filtro.getValue().equals("Fecha de Registro")){
            datePFechaInicial.setPrefWidth(130);
            datePFechaInicial.setVisible(true);
            datePFechaFinal.setPrefWidth(130);
            datePFechaFinal.setVisible(true);
        }
        if(cb_filtro.getValue().equals("Cédula de Empleado")){
            txt_buscarCedula.setPrefWidth(200);
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
        DatosTransacciones = FXCollections.observableArrayList();
        try{
            if(authenticationResponse.getRoles().getNombre().equals("Gestor"))
                busquedaListaPorJefe();
            else
                busquedaLista();

            tablaReporteSoporte.setItems(DatosTransacciones);
        
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(ReporteSoporteTecnicoController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void busquedaLista() throws InterruptedException, IOException, ExecutionException {
        List<TransaccionDTO> ListaTransacciones = null;
        boolean accion = false;
        switch(cb_filtro.getValue()){

            case "Fecha de Registro":
                if(datePFechaInicial.getValue() == null || datePFechaFinal.getValue() == null)
                    CargaGraficaMsg("Por favor complete los campos respectivos");
                else{
                    LocalDate localDate = datePFechaInicial.getValue();
                    Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                    Date inicio = Date.from(instant);

                    LocalDate localDate2 = datePFechaFinal.getValue();
                    Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
                    Date fin = Date.from(instant2);

                    ListaTransacciones = TransaccionWebService.getTransaccionByFechaRegistroBetweenAndTipo(inicio,fin,"Transacción", authenticationResponse.getJwt());
                    accion = true;
                }
            break;

            case "Cédula de Empleado": 
                if(txt_buscarCedula.getText().equals(""))
                    CargaGraficaMsg("Por favor complete los campos respectivos");
                else{
                    UsuarioDTO usuario = UsuarioWebService.getUsuarioByCedulaAproximate(txt_buscarCedula.getText(), authenticationResponse.getJwt()).get(0);
                    if(usuario != null){
                        ListaTransacciones = TransaccionWebService.getTransaccionByUsuarioAndTipo(usuario.getId(),"Transacción", authenticationResponse.getJwt());
                        accion = true;
                    }
                }
            break;

            default:
                break;
        }
        if(accion)
            if(ListaTransacciones.toArray().length != 0){
                for(TransaccionDTO transaccion : ListaTransacciones){
                    TransaccionU transaccion1 = new TransaccionU(transaccion.getId(),transaccion.getInformacion(),
                        transaccion.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                            transaccion.getUsuario().getNombreCompleto(), transaccion.getUsuario().getCedula(),
                                transaccion.getUsuario().getRol().getNombre());

                    DatosTransacciones.add(transaccion1);
                }
            }
            else
                CargaGraficaMsg("No se encontraron resultados");
    }
    
    private void busquedaListaPorJefe() throws InterruptedException, IOException, ExecutionException {
        List<TransaccionDTO> ListaTransacciones = null;
        boolean accion = false;
        switch(cb_filtro.getValue()){

            case "Fecha de Registro":
                if(datePFechaInicial.getValue() == null || datePFechaFinal.getValue() == null)
                    CargaGraficaMsg("Por favor complete los campos respectivos");
                else{
                    LocalDate localDate = datePFechaInicial.getValue();
                    Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                    Date inicio = Date.from(instant);

                    LocalDate localDate2 = datePFechaFinal.getValue();
                    Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
                    Date fin = Date.from(instant2);

                    ListaTransacciones = TransaccionWebService.getTransaccionByFechaRegistroBetweenAndTipoAndUsuarioUsuarioJefeId
                    (inicio,fin,"Transacción",authenticationResponse.getUsuario().getId(), authenticationResponse.getJwt());
                    accion = true;
                }
            break;

            case "Cédula de Empleado": 
                if(txt_buscarCedula.getText().equals(""))
                    CargaGraficaMsg("Por favor complete los campos respectivos");
                else{
                    UsuarioDTO usuario = UsuarioWebService.getUsuarioByCedulaAproximate(txt_buscarCedula.getText(), authenticationResponse.getJwt()).get(0);
                    if(usuario != null){
                        ListaTransacciones = TransaccionWebService.getTransaccionByUsuarioAndTipoAndUsuarioUsuarioJefeId
                        (usuario.getId(),"Transacción",authenticationResponse.getUsuario().getId(), authenticationResponse.getJwt());
                        accion = true;
                    }
                }
            break;

            default:
                break;
        }
        if(accion)
            if(ListaTransacciones.toArray().length != 0){
                for(TransaccionDTO transaccion : ListaTransacciones){
                    TransaccionU transaccion1 = new TransaccionU(transaccion.getId(),transaccion.getInformacion(),
                        transaccion.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                            transaccion.getUsuario().getNombreCompleto(), transaccion.getUsuario().getCedula(),
                                transaccion.getUsuario().getRol().getNombre());

                    DatosTransacciones.add(transaccion1);
                }
            }
            else
                CargaGraficaMsg("No se encontraron resultados");
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
     
    private void CargaGraficaMsg(String cuerpo){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
            msg.alerta(root, "Alerta", cuerpo);
        }
        });
    }
     
    public class TransaccionU {
    
        Long Id;  
        String Descripcion;
        String FechaRegistro;
        String Nombre;
        String Cedula;
        String Rol;

        public TransaccionU(Long Id, String Descripcion, String FechaRegistro,
                String Nombre, String Cedula, String Rol) {
            this.Id = Id;
            this.Descripcion = Descripcion;
            this.FechaRegistro = FechaRegistro; 
            this.Nombre = Nombre;
            this.Cedula = Cedula;
            this.Rol = Rol;
        }

        public TransaccionU() {
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

        public String getNombre() {
            return Nombre;
        }

        public String getCedula() {
            return Cedula;
        }

        public String getRol() {
            return Rol;
        }
    }
}
