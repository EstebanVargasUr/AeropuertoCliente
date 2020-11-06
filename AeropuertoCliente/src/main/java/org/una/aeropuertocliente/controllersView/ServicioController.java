package org.una.aeropuertocliente.controllersView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.*;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.*;
import org.una.aeropuertocliente.DTOs.PrecioDTO;
import org.una.aeropuertocliente.WebService.ServicioWebService;
import org.una.aeropuertocliente.DTOs.ServicioDTO;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.PrecioWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Adrian
 */
public class ServicioController extends Controller implements  Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField txt_buscar;
    @FXML
    private JFXTextField txt_buscarIdAvion;
    @FXML
    private JFXTextArea txt_observaciones;
    @FXML
    private JFXComboBox<String> cb_filtro;
    @FXML
    private JFXComboBox<String> cb_filtroEstado;
    @FXML
    private TableView<ServicioP> tablaServicios;
    @FXML
    private TableColumn<ServicioP, Long> tbc_id;
    @FXML
    private TableColumn<ServicioP, String>tbc_tipoServicio;
    @FXML
    private TableColumn<ServicioP, String>tbc_duracion;
    @FXML
    private TableColumn<ServicioP, String> tbc_factura;
    @FXML
    private TableColumn<ServicioP, String> tbc_encargado;
    @FXML
    private TableColumn<ServicioP, String> tbc_fechaRegistro;
    @FXML
    private TableColumn<ServicioP, String> tbc_fechaModificacion;
    @FXML
    private TableColumn<ServicioP, String> tbc_estado;
    @FXML
    private TableColumn<ServicioP, String> tbc_estadoCobro;    
    @FXML
    private DatePicker datePFechaInicial;
    @FXML
    private DatePicker datePFechaFinal;
    @FXML
    private Label lbl_id;
    @FXML
    private Label lbl_matricula;
    @FXML
    private Label lbl_estado;
    @FXML
    private Label lbl_FechaRegistro;
    @FXML
    private Label lbl_aerolinea;
    @FXML
    private Label lbl_monto;
    @FXML
    private Label lbl_fecha;   
    @FXML
    private JFXButton btn_modificar;
    @FXML
    private JFXButton btn_nuevo;
    @FXML
    private VBox vb_barraInferior;
    @FXML
    private JFXTextField txt_factura;
    @FXML
    private JFXRadioButton rb_ESActivo;
    @FXML
    private JFXRadioButton rb_ESInactivo;
    @FXML
    private JFXRadioButton rb_ECActivo;
    @FXML
    private JFXRadioButton rb_ECInactivo;
    @FXML
    private JFXTextField txt_responsable;
    @FXML
    private JFXComboBox<?> cb_tipoServicio;
    @FXML
    private JFXComboBox<?> cb_buscarAvion;
    @FXML
    private JFXTextField txt_avion;
    @FXML
    private JFXTextArea txt_observacionesInferior;
    
    String token;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            token = AutenticationWebService.login("admin", "Una2020");
        } catch (InterruptedException | ExecutionException | JsonProcessingException ex) 
        {Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);}
        
        cb_filtro.getItems().add("Id");
        cb_filtro.getItems().add("Estado");
        cb_filtro.getItems().add("Estado del cobro");
        cb_filtro.getItems().add("Fecha de registro");
        cb_filtro.getItems().add("Id del avion"); 
        cb_filtro.getItems().add("Id del tipo de servicio");
        cb_filtro.getItems().add("Id del tipo de servicio y Id del avion"); 
        cb_filtroEstado.getItems().add("True"); 
        cb_filtroEstado.getItems().add("False");    
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
    private void buscar(MouseEvent event) throws InterruptedException, ExecutionException, JsonProcessingException, IOException, ParseException 
    {
        LimpiaDatos();
        ObservableList<ServicioP> DatosServicios = FXCollections.observableArrayList();
        String EstadoServicio = "-";
        String EstadoCobro = "-";
        
        tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tbc_tipoServicio.setCellValueFactory(new PropertyValueFactory("NombreTipoServicio"));
        tbc_duracion.setCellValueFactory(new PropertyValueFactory("DuracionTipoServicio"));
        tbc_factura.setCellValueFactory(new PropertyValueFactory("Factura"));
        tbc_encargado.setCellValueFactory(new PropertyValueFactory("Encargado"));
        tbc_fechaRegistro.setCellValueFactory(new PropertyValueFactory("FechaRegistro"));
        tbc_fechaModificacion.setCellValueFactory(new PropertyValueFactory("FechaModificacion"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory("Estado"));
        tbc_estadoCobro.setCellValueFactory(new PropertyValueFactory("EstadoCobro"));

        if(cb_filtro.getValue().equals("Id")) {
           
            
            long Id = Long.parseLong(txt_buscar.getText());
            ServicioDTO servicio = ServicioWebService.getServicioById(Id, token);

            if (servicio.getEstado().toString().equals("true")) 
            EstadoServicio = "Activo";
            else EstadoServicio = "Inactivo";
            if (servicio.getEstadoCobro().toString().equals("true")) 
            EstadoCobro = "Activo";
            else EstadoCobro = "Inactivo";
            
            ServicioP servicio1 = new ServicioP(servicio.getId(),servicio.getTipoServicio().getNombre(),servicio.getTipoServicio().getDuracion(),
            servicio.getFactura(),servicio.getNombreResponsable(),servicio.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            servicio.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoServicio,EstadoCobro);

            DatosServicios.add(servicio1);
        }
        else {
            
            List<ServicioDTO> servicio = null;
            
            if (cb_filtro.getValue().equals("Estado")) {
        
                boolean Estado = Boolean.parseBoolean(cb_filtroEstado.getValue().toLowerCase());    
                servicio = ServicioWebService.getServicioByEstado(Estado, token);
            }
            if (cb_filtro.getValue().equals("Estado del cobro")) {
        
                boolean Estado = Boolean.parseBoolean(cb_filtroEstado.getValue().toLowerCase());    
                servicio = ServicioWebService.getServicioByEstadoCobro(Estado, token);
            }
             if (cb_filtro.getValue().equals("Id del tipo de servicio")) {
        
                long Id = Long.parseLong(txt_buscar.getText());    
                servicio = ServicioWebService.getServicioByTipoServicioId(Id, token);
             }
             if (cb_filtro.getValue().equals("Id del avion")) {
        
                long Id = Long.parseLong(txt_buscar.getText());    
                servicio = ServicioWebService.getServicioByAvionId(Id, token);
             }
             if (cb_filtro.getValue().equals("Id del tipo de servicio y Id del avion")) {
        
                long IdTipo = Long.parseLong(txt_buscar.getText());    
                long IdAvion = Long.parseLong(txt_buscarIdAvion.getText());    
                servicio = ServicioWebService.getServicioByTipoServicioIdAndAvionId(IdTipo, IdAvion, token);
             }
             
             if (cb_filtro.getValue().equals("Fecha de registro")) {

                LocalDate localDate = datePFechaInicial.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date inicio = Date.from(instant);

                LocalDate localDate2 = datePFechaFinal.getValue();
                Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
                Date fin = Date.from(instant2);

                servicio = ServicioWebService.getServicioByFechaRegistroBetween(inicio,fin, token);
             }
            
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
        tablaServicios.setItems(DatosServicios);
    }

    @FXML
    private void nuevoServicio(MouseEvent event) 
    {
        FlowController.getInstance().goView("CreacionServicio");
    }

    @FXML
    private void cb_filtroAction(ActionEvent event) 
    {
        txt_buscar.clear();
        txt_buscarIdAvion.clear();
        datePFechaInicial.setPrefWidth(0);  datePFechaInicial.setVisible(false);
        datePFechaFinal.setPrefWidth(0);    datePFechaFinal.setVisible(false);
        cb_filtroEstado.setPrefWidth(0);    cb_filtroEstado.setVisible(false);
        txt_buscar.setDisable(false);       txt_buscar.setPromptText("Digite lo que desea buscar");
        txt_buscarIdAvion.setPrefWidth(0);
        
        if (cb_filtro.getValue().equals("Estado") || cb_filtro.getValue().equals("Estado del cobro")) 
        {
            cb_filtroEstado.setPrefWidth(100);
            cb_filtroEstado.setVisible(true);
            txt_buscar.setDisable(true);
        }  
        if(cb_filtro.getValue().equals("Fecha de registro"))
        {
            datePFechaInicial.setPrefWidth(130);
            datePFechaInicial.setVisible(true);
            datePFechaFinal.setPrefWidth(130);
            datePFechaFinal.setVisible(true);
            txt_buscar.setDisable(true); 
        }
        if(cb_filtro.getValue().equals("Id del tipo de servicio y Id del avion"))
        {
            txt_buscarIdAvion.setPrefWidth(397);
            txt_buscar.setPromptText("Id del tipo de servicio");
        }
    }

    @FXML
    private void tablaServiciosClic(MouseEvent event) throws InterruptedException, ExecutionException, IOException 
    {
        String EstadoPrecio = "-";
        long Id = tablaServicios.getSelectionModel().selectedItemProperty().get().Id;
        ServicioDTO servicio = ServicioWebService.getServicioById(Id, token);
        List<PrecioDTO> precio = PrecioWebService.getPrecioByTipoServicioId(servicio.getTipoServicio().getId(), token);
        
        txt_observaciones.setText(servicio.getObservacion());
        lbl_id.setText(servicio.getAvion().getId().toString());
        lbl_matricula.setText(servicio.getAvion().getMatricula());
        lbl_FechaRegistro.setText(servicio.getAvion().getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
        lbl_FechaRegistro.setText(servicio.getAvion().getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
        lbl_aerolinea.setText(servicio.getAvion().getAerolinea().getNombreAerolinea());
        
        if (servicio.getAvion().getEstado().toString().equals("true")) 
        EstadoPrecio = "Activo";
        else EstadoPrecio = "Inactivo";

        lbl_estado.setText(EstadoPrecio);
        
        for (int i = 0; i < precio.toArray().length; i++) 
        {
            if (precio.get(i).getEstado().toString().equals("true")) 
            {
                lbl_monto.setText(precio.get(i).getMonto().toString());
                lbl_fecha.setText(precio.get(i).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                break;
            } 
        }     
    }

    @FXML
    private void btnBuscar(KeyEvent event) {}

    private void LimpiaDatos()
    {
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
    private void modificarServicio(MouseEvent event) {
    }

    @FXML
    private void cancelar(MouseEvent event) {
    }

    @FXML
    private void guardar(MouseEvent event) {
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
}
