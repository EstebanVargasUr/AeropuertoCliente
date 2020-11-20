package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.*;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import lombok.*;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.PrecioDTO;
import org.una.aeropuertocliente.WebService.ServicioWebService;
import org.una.aeropuertocliente.DTOs.ServicioDTO;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.AvionWebService;
import org.una.aeropuertocliente.WebService.PrecioWebService;
import org.una.aeropuertocliente.WebService.TipoServicioWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Adrian
 */
public class ServicioController extends Controller implements  Initializable {

    @FXML private StackPane root;
    @FXML private JFXTextField txt_buscar;
    @FXML private JFXTextField txt_buscarIdAvion;
    @FXML private JFXTextArea txt_observaciones;
    @FXML private JFXComboBox<String> cb_filtro;
    @FXML private JFXComboBox<String> cb_filtroEstado;
    @FXML private TableView<ServicioP> tablaServicios;
    @FXML private TableColumn<ServicioP, Long> tbc_id;
    @FXML private TableColumn<ServicioP, String>tbc_tipoServicio;
    @FXML private TableColumn<ServicioP, String>tbc_duracion;
    @FXML private TableColumn<ServicioP, String> tbc_factura;
    @FXML private TableColumn<ServicioP, String> tbc_encargado;
    @FXML private TableColumn<ServicioP, String> tbc_fechaRegistro;
    @FXML private TableColumn<ServicioP, String> tbc_fechaModificacion;
    @FXML private TableColumn<ServicioP, String> tbc_estado;
    @FXML private TableColumn<ServicioP, String> tbc_estadoCobro;    
    @FXML private DatePicker datePFechaInicial;
    @FXML private DatePicker datePFechaFinal;
    @FXML private Label lbl_id;
    @FXML private Label lbl_matricula;
    @FXML private Label lbl_estado;
    @FXML private Label lbl_FechaRegistro;
    @FXML private Label lbl_aerolinea;
    @FXML private Label lbl_monto;
    @FXML private Label lbl_fecha;   
    @FXML private JFXButton btn_modificar;
    @FXML private JFXButton btn_nuevo;
    @FXML private VBox vb_barraInferior;
    @FXML private JFXTextField txt_factura;
    @FXML private JFXRadioButton rb_ESActivo;
    @FXML private JFXRadioButton rb_ESInactivo;
    @FXML private JFXRadioButton rb_ECActivo;
    @FXML private JFXRadioButton rb_ECInactivo;
    @FXML private JFXTextField txt_responsable;
    @FXML private JFXComboBox<String> cb_tipoServicio;
    @FXML private JFXComboBox<String> cb_buscarAvion;
    @FXML private JFXTextField txt_avion;
    @FXML private JFXTextArea txt_observacionesInferior;
    @FXML private ImageView cargando;
    
    final ToggleGroup GrEstadoServicio = new ToggleGroup();
    final ToggleGroup GrEstadoCobro = new ToggleGroup();       
    public static ObservableList<ServicioP> DatosServicios;
    List<ServicioDTO> servicio;
    ServicioDTO ServicioSeleccionado = new ServicioDTO();
    boolean BotonGuardar = false;
    private JFXDialogLayout contenido = new JFXDialogLayout();
    private JFXDialog dialogo;
    private JFXTextField cedula;
    private JFXPasswordField password;
    Mensaje msg = new Mensaje();
    String EstadoServicio;
    String EstadoCobro; 
    String token;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        
        token = FlowController.getInstance().authenticationResponse.getJwt();
        
        initTabla();
        initComboBox();
        initRadioButton();
    }    

    @Override
    public void initialize() {
        root.styleProperty().set("-fx-background-color: #4AB19D");
        ModoDesarrollador();
    }

    @Override
    public Node getRoot() {
        return root;
    }     

    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V11-G-SER");
        else
            FlowController.getInstance().titulo("Gestión de Servicios");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    private void initTabla(){
        initColumnas();
        tablaServicios.setEditable(true);
    }
    
    private void initColumnas(){
        
        tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tbc_tipoServicio.setCellValueFactory(new PropertyValueFactory("NombreTipoServicio"));
        tbc_duracion.setCellValueFactory(new PropertyValueFactory("DuracionTipoServicio"));
        tbc_factura.setCellValueFactory(new PropertyValueFactory("Factura"));
        tbc_encargado.setCellValueFactory(new PropertyValueFactory("Encargado"));
        tbc_fechaRegistro.setCellValueFactory(new PropertyValueFactory("FechaRegistro"));
        tbc_fechaModificacion.setCellValueFactory(new PropertyValueFactory("FechaModificacion"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory("Estado"));
        tbc_estadoCobro.setCellValueFactory(new PropertyValueFactory("EstadoCobro"));
        
    }
     
    private void initComboBox(){
        
        cb_filtro.getItems().add("Id");
        cb_filtro.getItems().add("Estado");
        cb_filtro.getItems().add("Estado del cobro");
        cb_filtro.getItems().add("Fecha de registro");
        cb_filtro.getItems().add("Id del avion"); 
        cb_filtro.getItems().add("Id del tipo de servicio");
        cb_filtro.getItems().add("Id del tipo de servicio y Id del avion"); 
        
        cb_filtroEstado.getItems().add("Activo"); 
        cb_filtroEstado.getItems().add("Inactivo");  
        
        cb_buscarAvion.getItems().add("Por id");  
        cb_buscarAvion.getItems().add("Por matricula");  
        
        cb_tipoServicio.getItems().add("Carga de combustible");    
        cb_tipoServicio.getItems().add("Uso de hangares");    
        cb_tipoServicio.getItems().add("Mantenimiento preventivo");    
        cb_tipoServicio.getItems().add("Correctivos de aeronaves");    
        
    }
    
     private void initRadioButton(){
        rb_ESActivo.setToggleGroup(GrEstadoServicio);
        rb_ESInactivo.setToggleGroup(GrEstadoServicio);
        rb_ECActivo.setToggleGroup(GrEstadoCobro);
        rb_ECInactivo.setToggleGroup(GrEstadoCobro);
     }
     
    @FXML
    private void buscar(MouseEvent event) {
        CargaLogicaBusqueda();
    }

    private void RealizarBusqueda() {
        LimpiaDatos();
        DatosServicios = FXCollections.observableArrayList();
        EstadoServicio = "-";
        EstadoCobro = "-"; 
        try{
            if(cb_filtro.getValue() == null) 
            {
                CargaGraficaMsg("Por favor seleccione un filtro");
            }
            else
            {
                if(cb_filtro.getValue().equals("Id"))      
                {   
                    if (txt_buscar.getText().equals("")) 
                    {CargaGraficaMsg("Por favor complete los campos respectivos");}
                    else
                    {busquedaIndividual();}
                }
                else
                {
                    if (cb_filtro.getValue().equals("Id del avion") || cb_filtro.getValue().equals("Id del tipo de servicio")) {
                        if (txt_buscar.getText().equals("")) 
                        {CargaGraficaMsg("Por favor complete los campos respectivos");}
                        else
                        {busquedaLista();}
                    }
                    if (cb_filtro.getValue().equals("Id del tipo de servicio y Id del avion")) {
                        if (txt_buscar.getText().equals("") || txt_buscarIdAvion.getText().equals("")) 
                        {CargaGraficaMsg("Por favor complete los campos respectivos");}
                        else
                        {busquedaLista();}
                    }
                    if (cb_filtro.getValue().equals("Estado")||cb_filtro.getValue().equals("Estado del cobro")) {
                        if (cb_filtroEstado.getValue()==null) 
                        {CargaGraficaMsg("Por favor complete los campos respectivos");}
                        else
                        {busquedaLista();}
                    }
                    if (cb_filtro.getValue().equals("Fecha de registro")) {
                        if (datePFechaFinal.getValue() == null || datePFechaInicial.getValue() == null) 
                        CargaGraficaMsg("Por favor complete los campos respectivos");
                        else
                        {busquedaLista();}  
                    }
                }
            tablaServicios.setItems(DatosServicios);
            }
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void busquedaIndividual() throws InterruptedException, IOException, ExecutionException {
        long Id = Long.parseLong(txt_buscar.getText());
        ServicioDTO servicio_ = ServicioWebService.getServicioById(Id, token);
        
        if (servicio_.getId() != null) {
            if (servicio_.getEstado().toString().equals("true")) 
            EstadoServicio = "Activo";
            else EstadoServicio = "Inactivo";
            if (servicio_.getEstadoCobro().toString().equals("true")) 
            EstadoCobro = "Activo";
            else EstadoCobro = "Inactivo";

            ServicioP servicio1 = new ServicioP(servicio_.getId(),servicio_.getTipoServicio().getNombre(),servicio_.getTipoServicio().getDuracion(),
            servicio_.getFactura(),servicio_.getNombreResponsable(),servicio_.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            servicio_.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoServicio,EstadoCobro);

            DatosServicios.add(servicio1);
        }
        else
        {CargaGraficaMsg("No se encontraron servicios");}    
    }
    
    private void busquedaLista() throws InterruptedException, IOException, ExecutionException {
            servicio = null;
 
            if (cb_filtro.getValue().equals("Estado")) {
            Boolean Estado = false;
            if (cb_filtroEstado.getValue().equals("Activo")) 
            {Estado = true;}
            else
            {Estado = false;}

            servicio = ServicioWebService.getServicioByEstado(Estado, token);
            }
            
            if (cb_filtro.getValue().equals("Estado del cobro")) {
                Boolean Estado = false;
                if (cb_filtroEstado.getValue().equals("Activo")) 
                {Estado = true;}
                else
                {Estado = false;}

                servicio = ServicioWebService.getServicioByEstadoCobro(Estado, token);
            }
            
            filtroBusqueda();
            
            if (servicio.toArray().length != 0) {
                
            for (int i = 0; i < servicio.toArray().length; i++) 
            {
                if (servicio.get(i).getEstado().toString().equals("true")) 
                EstadoServicio = "Activo";
                else EstadoServicio = "Inactivo";
                if (servicio.get(i).getEstadoCobro().toString().equals("true")) 
                EstadoCobro = "Activo";
                else EstadoCobro = "Inactivo";

                ServicioP servicio1 = new ServicioP(servicio.get(i).getId(),servicio.get(i).getTipoServicio().getNombre(),servicio.get(i).getTipoServicio().getDuracion(),
                servicio.get(i).getFactura(),servicio.get(i).getNombreResponsable(),servicio.get(i).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                servicio.get(i).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoServicio,EstadoCobro);

                DatosServicios.add(servicio1);
            }
        }
        else 
        {CargaGraficaMsg("No se encontraron servicios");}  
    }
    
    private void filtroBusqueda() throws InterruptedException, IOException, ExecutionException{
         switch (cb_filtro.getValue()) {
            case "Id del tipo de servicio":
            {
                long Id = Long.parseLong(txt_buscar.getText());
                servicio = ServicioWebService.getServicioByTipoServicioId(Id, token);
                break;
            }
            case "Id del avion":
            {
                long Id = Long.parseLong(txt_buscar.getText());
                servicio = ServicioWebService.getServicioByAvionId(Id, token);
                break;
            }
            case "Id del tipo de servicio y Id del avion":
            {
                long IdTipo = Long.parseLong(txt_buscar.getText());
                long IdAvion = Long.parseLong(txt_buscarIdAvion.getText());
                servicio = ServicioWebService.getServicioByTipoServicioIdAndAvionId(IdTipo, IdAvion, token);
                break;
            }
            case "Fecha de registro":
            {
                LocalDate localDate = datePFechaInicial.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date inicio = Date.from(instant);
                LocalDate localDate2 = datePFechaFinal.getValue();
                Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
                Date fin = Date.from(instant2);
                servicio = ServicioWebService.getServicioByFechaRegistroBetween(inicio,fin, token);
                break;
            }
            default:
                break;
        }
    }
    
    @FXML
    private void cb_filtroAction(ActionEvent event) {
        txt_buscar.clear();
        txt_buscarIdAvion.clear();
        datePFechaInicial.setPrefWidth(0);  datePFechaInicial.setVisible(false);
        datePFechaFinal.setPrefWidth(0);    datePFechaFinal.setVisible(false);
        cb_filtroEstado.setPrefWidth(0);    cb_filtroEstado.setVisible(false);
        txt_buscar.setDisable(false);       txt_buscar.setPromptText("Digite lo que desea buscar");
        txt_buscarIdAvion.setPrefWidth(0);
        
        if (cb_filtro.getValue().equals("Estado") || cb_filtro.getValue().equals("Estado del cobro")) {
            cb_filtroEstado.setPrefWidth(100);
            cb_filtroEstado.setVisible(true);
            txt_buscar.setDisable(true);
        }  
        if(cb_filtro.getValue().equals("Fecha de registro")){
            datePFechaInicial.setPrefWidth(130);
            datePFechaInicial.setVisible(true);
            datePFechaFinal.setPrefWidth(130);
            datePFechaFinal.setVisible(true);
            txt_buscar.setDisable(true); 
        }
        if(cb_filtro.getValue().equals("Id del tipo de servicio y Id del avion")){
            txt_buscarIdAvion.setPrefWidth(397);
            txt_buscar.setPromptText("Id del tipo de servicio");
        }
    }

    @FXML
    private void tablaServiciosClic(MouseEvent event) {
        if(tablaServicios.getSelectionModel().getSelectedItem() != null)
            CargaLogicaAvionPrecio(); 
    }
    
    private void RealizarBusquedaAvionPrecio(){
        try{
            long Id = tablaServicios.getSelectionModel().selectedItemProperty().get().Id;
            ServicioDTO servicio = ServicioWebService.getServicioById(Id, token);
            List<PrecioDTO> precio = PrecioWebService.getPrecioByTipoServicioId(servicio.getTipoServicio().getId(), token);
            ServicioSeleccionado = servicio;

            Platform.runLater(new Runnable() {
            @Override public void run() {
                String EstadoPrecio = "-";
                if (servicio.getAvion().getEstado().toString().equals("true")) 
                EstadoPrecio = "Activo";
                else EstadoPrecio = "Inactivo";

                lbl_estado.setText(EstadoPrecio);
                txt_observaciones.setText(servicio.getObservacion());
                lbl_id.setText(servicio.getAvion().getId().toString());
                lbl_matricula.setText(servicio.getAvion().getMatricula());
                lbl_FechaRegistro.setText(servicio.getAvion().getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                lbl_FechaRegistro.setText(servicio.getAvion().getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                lbl_aerolinea.setText(servicio.getAvion().getAerolinea().getNombreAerolinea());

                for (int i = 0; i < precio.toArray().length; i++) 

                if (precio.get(i).getEstado().toString().equals("true")) {
                    lbl_monto.setText(precio.get(i).getMonto().toString());
                    lbl_fecha.setText(precio.get(i).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                    break;
                } 
               tablaServicios.getSelectionModel().clearSelection();
            }
            });
         
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);}
    }

    @FXML
    private void btnBuscar(KeyEvent event) {}

    private void LimpiaDatos() {
        ObservableList items = FXCollections.observableArrayList(); 
        tablaServicios.setItems(items);    
        txt_observaciones.setText("-");
        lbl_id.setText("-");
        lbl_matricula.setText("-");
        lbl_estado.setText("-");
        lbl_FechaRegistro.setText("-");
        lbl_FechaRegistro.setText("-");
        lbl_aerolinea.setText("-");  
        lbl_monto.setText("-");
        lbl_fecha.setText("-");
    }

    @FXML
    private void nuevoServicio(MouseEvent event) {     
        LimpiaBarraInferior();
        BotonGuardar = false;
        vb_barraInferior.setPrefHeight(200);
        vb_barraInferior.setVisible(true);
    }
    @FXML
    private void modificarServicio(MouseEvent event) {
        if (ServicioSeleccionado.getId() == null) {
            CargaGraficaMsg("Por favor seleccione un servicio para modificar");
        }
        else{
            LimpiaBarraInferior();
            BotonGuardar = true;
            vb_barraInferior.setPrefHeight(200);
            vb_barraInferior.setVisible(true);

            txt_factura.setText(ServicioSeleccionado.getFactura());
            txt_responsable.setText(ServicioSeleccionado.getNombreResponsable());
            txt_observacionesInferior.setText(ServicioSeleccionado.getObservacion());

            if (ServicioSeleccionado.getEstado().toString().equals("true")) 
                rb_ESActivo.setSelected(true);
            else
                rb_ESInactivo.setSelected(true);

            if (ServicioSeleccionado.getEstadoCobro().toString().equals("true")) 
                rb_ECActivo.setSelected(true);
            else
                rb_ECInactivo.setSelected(true);

            cb_tipoServicio.setValue(ServicioSeleccionado.getTipoServicio().getNombre());
            cb_buscarAvion.setValue("Por id");  txt_avion.setText(ServicioSeleccionado.getId().toString());
        } 
    }
 
    
    @FXML
    private void cancelar(MouseEvent event) {
        LimpiaBarraInferior();
        vb_barraInferior.setPrefHeight(0);
        vb_barraInferior.setVisible(false); 
    }

    @FXML
    private void guardar(MouseEvent event) {
        
        if (!rb_ECInactivo.isSelected() && !rb_ECActivo.isSelected()||!rb_ECInactivo.isSelected() && !rb_ECActivo.isSelected()||
                !rb_ESInactivo.isSelected() && !rb_ESActivo.isSelected()||!rb_ESInactivo.isSelected() && !rb_ESActivo.isSelected()||
                txt_factura.getText().equals("")||txt_observacionesInferior.getText().equals("")||txt_responsable.getText().equals("")||
                cb_tipoServicio.getValue() == null||cb_buscarAvion.getValue() == null||txt_avion.getText().equals("")) 
        {
           CargaGraficaMsg("Por favor complete los campos necesarios para crear el servicio");
        }
        else
            loginEncargado();
    }
    
    private void RealizarGuardar() {
        try{
            if (BotonGuardar == false) 
                ServicioSeleccionado = new ServicioDTO();

            vb_barraInferior.setPrefHeight(0);
            vb_barraInferior.setVisible(false);

            if (rb_ECInactivo.isSelected()) 
                ServicioSeleccionado.setEstadoCobro(false);
            else 
                ServicioSeleccionado.setEstadoCobro(true);
            
            if(rb_ESInactivo.isSelected()) 
                ServicioSeleccionado.setEstado(false);
            else
                ServicioSeleccionado.setEstado(true);

            ServicioSeleccionado.setFactura(txt_factura.getText());
            ServicioSeleccionado.setNombreResponsable(txt_responsable.getText());
            ServicioSeleccionado.setObservacion(txt_observacionesInferior.getText());

            if(cb_tipoServicio.getValue().equals("Carga de combustible")) 
                ServicioSeleccionado.setTipoServicio(TipoServicioWebService.getTipoServicioById(1, token));
            if(cb_tipoServicio.getValue().equals("Uso de hangares")) 
                ServicioSeleccionado.setTipoServicio(TipoServicioWebService.getTipoServicioById(2, token));
            if(cb_tipoServicio.getValue().equals("Mantenimiento preventivo")) 
                ServicioSeleccionado.setTipoServicio(TipoServicioWebService.getTipoServicioById(3, token));
            if(cb_tipoServicio.getValue().equals("Correctivos de aeronaves")) 
                ServicioSeleccionado.setTipoServicio(TipoServicioWebService.getTipoServicioById(4, token));

            if(cb_buscarAvion.getValue().equals("Por id")) {
                long Id = Long.parseLong(txt_avion.getText());
                ServicioSeleccionado.setAvion(AvionWebService.getAvionById(Id, token));
            }
            else
                ServicioSeleccionado.setAvion(AvionWebService.getAvionByMatricula(txt_avion.getText(), token).get(0));

            if(BotonGuardar == true) 
                ServicioWebService.updateServicio(ServicioSeleccionado, ServicioSeleccionado.getId(), token);
            if(BotonGuardar == false) 
                ServicioWebService.createServicio(ServicioSeleccionado, token);

        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void LimpiaBarraInferior()
    {
        txt_factura.setText("");
        txt_responsable.setText("");
        txt_observacionesInferior.setText("");
        rb_ESActivo.setSelected(false);
        rb_ESInactivo.setSelected(false);
        rb_ECActivo.setSelected(false);
        rb_ECInactivo.setSelected(false);
        cb_tipoServicio.setValue("");
        cb_buscarAvion.setValue("");  
        txt_avion.setText("");   
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
    
    private void CargaLogicaAvionPrecio(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            
            RealizarBusquedaAvionPrecio(); 
            
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
            CargaGraficaGuardar();
            
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    }
    
    private void CargaGraficaGuardar(){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            LimpiaBarraInferior();
            LimpiaDatos();
        }
        });
    }
    
    private void CargaGraficaMsg(String cuerpo){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
            msg.alerta(root, "Alerta", cuerpo);
        }
        });
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor 
    @ToString
    public class ServicioP {
    
        private Long Id;  
        private String NombreTipoServicio;
        private Long DuracionTipoServicio;
        private String Factura;
        private String Encargado;
        private String FechaRegistro;
        private String FechaModificacion;
        private String Estado;
        private String EstadoCobro;

        public ServicioP(long Id, String NombreTipoServicio, long DuracionTipoServicio, String Factura, String Encargado, String FechaRegistro, String FechaModificacion, String Estado, String EstadoCobro) {
            this.Id = Id;
            this.NombreTipoServicio = NombreTipoServicio;
            this.DuracionTipoServicio = DuracionTipoServicio;
            this.Factura = Factura;
            this.Encargado = Encargado;
            this.FechaRegistro = FechaRegistro;
            this.FechaModificacion = FechaModificacion;
            this.Estado = Estado;
            this.EstadoCobro = EstadoCobro;
        }
    }
    
    public void cuerpoLoginEncargado(){
        contenido.setHeading(new Text("Aprovación del Gerente"));
        
        cedula = new JFXTextField();
        password = new JFXPasswordField();
    
        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("Cedula: "));
        vbox.getChildren().add(cedula);
        vbox.getChildren().add(new Label("Contraseña: "));
        vbox.getChildren().add(password);
        vbox.setSpacing(20);
        
        contenido.setBody(vbox);
    }
    
    public void realizarLoginEncargado(){
        if(cedula.getText().equals("") || password.getText().equals(""))
            msg.alerta(root, "Alerta", "Por favor complete los campos necesarios");
        else{
            Thread thread = new Thread(new Runnable(){
            public void run(){
                cargando.setVisible(true);
                root.setDisable(true);
                try{
                    AuthenticationResponse authenticationResponse = AutenticationWebService.login(cedula.getText(), password.getText(), root);
                    if(authenticationResponse != null)
                        if(authenticationResponse.getUsuario().getId().equals(FlowController.getInstance().authenticationResponse.getUsuario().getUsuarioJefe().getId())){
                            CargaLogicaGuardar();
                            dialogo.close();
                        }
                        else{
                            CargaGraficaMsg("El usuario autenticado no corresponde a su jefe directo");
                        }
                            
                } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(Mensaje.class.getName()).log(Level.SEVERE, null, ex);}

                cargando.setVisible(false);
                root.setDisable(false);
                dialogo.close();
            }
            });
            thread.start();
        }
    }
    
     public void loginEncargado(){
        cuerpoLoginEncargado();
        
        dialogo = new JFXDialog(root, contenido, JFXDialog.DialogTransition.RIGHT);
        JFXButton botonAceptar = new JFXButton("Aceptar");
        JFXButton botonCancelar = new JFXButton("Cancelar");
        botonCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
               dialogo.close();
            }
        });
        botonAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                realizarLoginEncargado();
            }
        });
        contenido.setActions(botonCancelar,botonAceptar);
        dialogo.show();
    }
}
